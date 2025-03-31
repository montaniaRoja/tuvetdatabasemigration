package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import models.DbConnection;
import models.StockEntry;

public class StockEntryMigrationHelper {
    private static final int BATCH_SIZE = 500; // Tamaño del batch para optimizar la migración

    public static boolean entriesMigration() {
        ArrayList<StockEntry> entriesList = new ArrayList<>();

        Connection remoteConn = null;
        Connection localConn = null;
        PreparedStatement stmtSave = null;
        ResultSet resultSet = null;

        try {
            // Conectar a la base de datos remota
            remoteConn = DbConnection.conectarseRemoto();
            String sqlEntries = "SELECT id, fecha, sucursal_id, prod_id, cantidad, detalle, procesa_id, autoriza_id, estado " +
                                "FROM entradas WHERE procesa_id IS NOT NULL";

            PreparedStatement stmt = remoteConn.prepareStatement(sqlEntries);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                StockEntry entry = new StockEntry();
                entry.setId(resultSet.getInt("id"));
                entry.setBranchId(resultSet.getInt("sucursal_id"));
                entry.setProductId(resultSet.getInt("prod_id"));
                entry.setQuantity(resultSet.getInt("cantidad"));
                entry.setExplanation(resultSet.getString("detalle"));
                entry.setCreatedBy(resultSet.getInt("procesa_id"));
                entry.setAuthorizedBy(resultSet.getInt("autoriza_id"));
                entry.setStatus(resultSet.getString("estado"));
                entry.setCreatedAt(resultSet.getDate("fecha"));

                entriesList.add(entry);
            }

            // Conectar a la base de datos local
            localConn = DbConnection.conectarseLocal();
            localConn.setAutoCommit(false); // Deshabilita auto-commit para mejor rendimiento

            String sqlInsert = "INSERT INTO stock_entries (id, branch_id, product_id, quantity, explanation, created_by, authorized_by, status, created_at) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmtSave = localConn.prepareStatement(sqlInsert);
            int batchCounter = 0;

            for (StockEntry entry : entriesList) {
                stmtSave.setInt(1, entry.getId());
                stmtSave.setInt(2, entry.getBranchId());
                stmtSave.setInt(3, entry.getProductId());
                stmtSave.setInt(4, entry.getQuantity());
                stmtSave.setString(5, entry.getExplanation());
                stmtSave.setInt(6, entry.getCreatedBy());
                stmtSave.setInt(7, entry.getAuthorizedBy());
                stmtSave.setString(8, entry.getStatus());
                stmtSave.setDate(9, entry.getCreatedAt());

                stmtSave.addBatch();
                batchCounter++;

                if (batchCounter % BATCH_SIZE == 0) {
                    stmtSave.executeBatch();
                    localConn.commit();
                    System.out.printf("Migradas %d entradas...\n", batchCounter);
                }
            }

            // Ejecutar el batch restante
            stmtSave.executeBatch();
            localConn.commit();

            // Actualizar secuencia en PostgreSQL
            Statement stmtUpdateSeq = localConn.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('stock_entries', 'id'), (SELECT MAX(id) FROM stock_entries))");
            stmtUpdateSeq.close();

            System.out.println("Migración de entradas completada exitosamente.");
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
