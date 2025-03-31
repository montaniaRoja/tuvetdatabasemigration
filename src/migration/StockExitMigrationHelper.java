package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import models.DbConnection;
import models.StockExit;

public class StockExitMigrationHelper {
    private static final int BATCH_SIZE = 500; // Define el tamaño de lote para mejorar la eficiencia

    public static boolean exitsMigration() {
        ArrayList<StockExit> exitsList = new ArrayList<>();

        Connection remoteConn = null;
        Connection localConn = null;
        PreparedStatement stmtSave = null;
        ResultSet resultSet = null;

        try {
            // Conectar a la base de datos remota
            remoteConn = DbConnection.conectarseRemoto();
            String sqlExits = "SELECT id, sucursal_id, prod_id, cantidad, detalle, procesa_id, autoriza_id, estado, fecha FROM salidas";
            
            PreparedStatement stmt = remoteConn.prepareStatement(sqlExits);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                StockExit exit = new StockExit();
                exit.setId(resultSet.getInt("id"));
                exit.setBranchId(resultSet.getInt("sucursal_id"));
                exit.setProductId(resultSet.getInt("prod_id"));
                exit.setQuantity(resultSet.getInt("cantidad"));
                exit.setExplanation(resultSet.getString("detalle"));
                exit.setCreatedBy(resultSet.getInt("procesa_id"));
                exit.setAuthorizedBy(resultSet.getInt("autoriza_id"));
                exit.setStatus(resultSet.getString("estado"));
                exit.setCreatedAt(resultSet.getDate("fecha"));

                exitsList.add(exit);
            }

            // Conectar a la base de datos local
            localConn = DbConnection.conectarseLocal();
            localConn.setAutoCommit(false); // Deshabilita auto-commit para mejorar rendimiento

            String sqlInsert = "INSERT INTO stock_exits (id, branch_id, product_id, quantity, explanation, created_by, authorized_by, status, created_at) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmtSave = localConn.prepareStatement(sqlInsert);
            int batchCounter = 0;

            for (StockExit exit : exitsList) {
                stmtSave.setInt(1, exit.getId());
                stmtSave.setInt(2, exit.getBranchId());
                stmtSave.setInt(3, exit.getProductId());
                stmtSave.setInt(4, exit.getQuantity());
                stmtSave.setString(5, exit.getExplanation());
                stmtSave.setInt(6, exit.getCreatedBy());
                stmtSave.setInt(7, exit.getAuthorizedBy());
                stmtSave.setString(8, exit.getStatus());
                stmtSave.setDate(9, exit.getCreatedAt());

                stmtSave.addBatch();
                batchCounter++;

                if (batchCounter % BATCH_SIZE == 0) {
                    stmtSave.executeBatch();
                    localConn.commit();
                    System.out.printf("Migradas %d salidas...\n", batchCounter);
                }
            }

            // Ejecutar el batch restante
            stmtSave.executeBatch();
            localConn.commit();

            // Actualizar secuencia en PostgreSQL
            Statement stmtUpdateSeq = localConn.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('stock_exits', 'id'), (SELECT MAX(id) FROM stock_exits))");
            stmtUpdateSeq.close();

            System.out.println("Migración de salidas completada exitosamente.");
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
