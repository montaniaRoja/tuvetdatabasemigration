package migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import models.DbConnection;
import models.PurchaseDetail;

public class PurchaseDetailMigrationHelper {
    private static final int BATCH_SIZE = 500; // Tamaño del batch para optimizar la carga

    public static boolean purchasesMigration() {
        ArrayList<PurchaseDetail> purchaseDetailList = new ArrayList<>();

        Connection remoteConn = null;
        Connection localConn = null;
        PreparedStatement stmtSave = null;
        ResultSet resultSet = null;

        try {
            // Conectar a la base de datos remota
            remoteConn = DbConnection.conectarseRemoto();
            String sqlPurchaseDetail = "SELECT id, compra_id, prod_id, producto_cantidad, " +
                                       "producto_bonificacion, producto_cantidadtotal, " +
                                       "producto_costo, producto_porciva " +
                                       "FROM comprasdetalle;";

            PreparedStatement stmt = remoteConn.prepareStatement(sqlPurchaseDetail);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                PurchaseDetail purchase = new PurchaseDetail();
                purchase.setId(resultSet.getInt("id"));
                purchase.setPurchase_id(resultSet.getInt("compra_id"));
                purchase.setProduct_id(resultSet.getInt("prod_id"));
                purchase.setQuantity(resultSet.getInt("producto_cantidad"));
                purchase.setBonification(resultSet.getInt("producto_bonificacion"));
                purchase.setTotal(resultSet.getInt("producto_cantidadtotal"));
                purchase.setUnit_cost(resultSet.getDouble("producto_costo"));
                purchase.setTax_amount(resultSet.getDouble("producto_porciva"));

                purchaseDetailList.add(purchase);
            }

            // Conectar a la base de datos local
            localConn = DbConnection.conectarseLocal();
            localConn.setAutoCommit(false); // Deshabilita auto-commit para mejorar rendimiento

            String sqlInsert = "INSERT INTO detail_purchases " +
                               "(id, purchase_id, product_id, quantity, bonification, total, unit_cost, tax_amount) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            stmtSave = localConn.prepareStatement(sqlInsert);
            int batchCounter = 0;

            for (PurchaseDetail purchase : purchaseDetailList) {
                stmtSave.setInt(1, purchase.getId());
                stmtSave.setInt(2, purchase.getPurchase_id());
                stmtSave.setInt(3, purchase.getProduct_id());
                stmtSave.setInt(4, purchase.getQuantity());
                stmtSave.setInt(5, purchase.getBonification());
                stmtSave.setInt(6, purchase.getTotal());
                stmtSave.setDouble(7, purchase.getUnit_cost());
                stmtSave.setDouble(8, purchase.getTax_amount());

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
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('detail_purchases', 'id'), (SELECT MAX(id) FROM detail_purchases))");
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
