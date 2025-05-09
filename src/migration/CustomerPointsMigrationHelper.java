package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import models.CustomerPoints;
import models.DbConnection;

public class CustomerPointsMigrationHelper {

    private static final int BATCH_SIZE = 500; // Ajusta según el rendimiento de la base de datos

    public static boolean pointsMigration() {
        ArrayList<CustomerPoints> pointsList = new ArrayList<>();

        Connection remoteConn = null;
        Connection localConn = null;
        PreparedStatement stmtSave = null;
        ResultSet resultSet = null;

        try {
            // Conectar a la base de datos remota
            remoteConn = DbConnection.conectarseRemoto();
            String sqlPoints = "SELECT id, id_cliente, invoice_id, usuario_id, monto_factura, " +
                               "monto_acumulado, monto_redimido, fecha FROM puntos_clientes;";
            
            PreparedStatement stmt = remoteConn.prepareStatement(sqlPoints);
            resultSet = stmt.executeQuery();
            
            while (resultSet.next()) {
                CustomerPoints point = new CustomerPoints();
                point.setId(resultSet.getInt("id"));
                point.setCustomerId(resultSet.getInt("id_cliente"));
                point.setInvoiceId(resultSet.getInt("invoice_id"));
                point.setUserId(resultSet.getInt("usuario_id"));
                point.setInvoiceAmount(resultSet.getDouble("monto_factura"));
                point.setEarnedPoints(resultSet.getDouble("monto_acumulado"));
                point.setRedeemedPoints(resultSet.getDouble("monto_redimido"));
                point.setCreatedAt(resultSet.getDate("fecha"));
                
                pointsList.add(point);
            }

            // Conectar a la base de datos local
            localConn = DbConnection.conectarseLocal();
            localConn.setAutoCommit(false); // Deshabilitar auto-commit para batch processing
            
            String sqlInsert = "INSERT INTO customer_points " +
                               "(id, customer_id, invoice_id, user_id, invoice_amount, earned_points, " +
                               "redeemed_points, created_at) VALUES (?,?,?,?,?,?,?,?)";
            
            stmtSave = localConn.prepareStatement(sqlInsert);
            int batchCounter = 0;

            for (CustomerPoints point : pointsList) {
                stmtSave.setInt(1, point.getId());
                stmtSave.setInt(2, point.getCustomerId());
                stmtSave.setInt(3, point.getInvoiceId());
                stmtSave.setInt(4, point.getUserId());
                stmtSave.setDouble(5, point.getInvoiceAmount());
                stmtSave.setDouble(6, point.getEarnedPoints());
                stmtSave.setDouble(7, point.getRedeemedPoints());
                stmtSave.setDate(8, point.getCreatedAt());

                stmtSave.addBatch();
                batchCounter++;

                if (batchCounter % BATCH_SIZE == 0) {
                    stmtSave.executeBatch();
                    localConn.commit();
                    System.out.println("Migrados " + batchCounter + " registros de puntos...");
                }
            }

            // Ejecutar cualquier batch restante
            stmtSave.executeBatch();
            localConn.commit();

            // Actualizar secuencia en PostgreSQL
            Statement stmtUpdateSeq = localConn.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('customer_points', 'id'), (SELECT MAX(id) FROM customer_points))");
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
