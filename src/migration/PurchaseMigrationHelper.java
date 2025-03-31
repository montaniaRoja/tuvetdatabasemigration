package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import models.Purchase;
import models.DbConnection;

public class PurchaseMigrationHelper {
    private static final int BATCH_SIZE = 500; // Tamaño del batch para optimizar la carga

    public static boolean purchasesMigration() {
        ArrayList<Purchase> purchaseList = new ArrayList<>();

        Connection remoteConn = null;
        Connection localConn = null;
        PreparedStatement stmtSave = null;
        ResultSet resultSet = null;

        try {
            // Conectar a la base de datos remota
            remoteConn = DbConnection.conectarseRemoto();
            String sqlPurchases = "SELECT id, proveedor_id, compra_nodoc, compra_condicion, compra_subtotal, " +
                                  "compra_montoiva, compra_montoretencion, compra_total, sucursal_id, compra_fecha " +
                                  "FROM comprasproductos;";

            PreparedStatement stmt = remoteConn.prepareStatement(sqlPurchases);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Purchase purchase = new Purchase();
                purchase.setId(resultSet.getInt("id"));
                purchase.setSupplier_id(resultSet.getInt("proveedor_id"));
                purchase.setInvoice_number(resultSet.getString("compra_nodoc"));
                purchase.setPayment_condition(resultSet.getString("compra_condicion"));
                purchase.setSubtotal_amount(resultSet.getDouble("compra_subtotal"));
                purchase.setTax_amount(resultSet.getDouble("compra_montoiva"));
                purchase.setRetention_amount(resultSet.getDouble("compra_montoretencion"));
                purchase.setTotal(resultSet.getDouble("compra_total"));
                purchase.setBranch_id(resultSet.getInt("sucursal_id"));
                purchase.setCreated_by(1);
                purchase.setCreated_at(resultSet.getDate("compra_fecha"));

                purchaseList.add(purchase);
            }

            // Conectar a la base de datos local
            localConn = DbConnection.conectarseLocal();
            localConn.setAutoCommit(false); // Deshabilita auto-commit para mejorar rendimiento

            String sqlInsert = "INSERT INTO purchases " +
                               "(id, supplier_id, invoice_number, payment_condition, subtotal_amount, " +
                               "tax_amount, retention_amount, total, branch_id, created_by, created_at) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            stmtSave = localConn.prepareStatement(sqlInsert);
            int batchCounter = 0;

            for (Purchase purchase : purchaseList) {
                stmtSave.setInt(1, purchase.getId());
                stmtSave.setInt(2, purchase.getSupplier_id());
                stmtSave.setString(3, purchase.getInvoice_number());
                stmtSave.setString(4, purchase.getPayment_condition());
                stmtSave.setDouble(5, purchase.getSubtotal_amount());
                stmtSave.setDouble(6, purchase.getTax_amount());
                stmtSave.setDouble(7, purchase.getRetention_amount());
                stmtSave.setDouble(8, purchase.getTotal());
                stmtSave.setInt(9, purchase.getBranch_id());
                stmtSave.setInt(10, purchase.getCreated_by());
                stmtSave.setDate(11, purchase.getCreated_at());

                stmtSave.addBatch();
                batchCounter++;

                if (batchCounter % BATCH_SIZE == 0) {
                    stmtSave.executeBatch();
                    localConn.commit();
                    System.out.println("Migradas " + batchCounter + " compras...");
                }
            }

            // Ejecutar batch restante
            stmtSave.executeBatch();
            localConn.commit();

            // Actualizar secuencia en PostgreSQL
            Statement stmtUpdateSeq = localConn.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('purchases', 'id'), (SELECT MAX(id) FROM purchases))");
            stmtUpdateSeq.close();

            System.out.println("Migración completada exitosamente.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (localConn != null) {
                try {
                    localConn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return false;

        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (stmtSave != null) stmtSave.close();
                if (localConn != null) localConn.setAutoCommit(true);
                if (localConn != null) localConn.close();
                if (remoteConn != null) remoteConn.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
}
