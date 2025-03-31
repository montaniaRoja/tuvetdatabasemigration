package migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import models.Prescription;
import models.DbConnection;

public class PrescriptionMigrationHelper {
    private static final int BATCH_SIZE = 500; // Tamaño del batch para optimizar la carga

    public static boolean recetasMigration() {
        ArrayList<Prescription> recetasList = new ArrayList<>();

        Connection remoteConn = null;
        Connection localConn = null;
        PreparedStatement stmtSave = null;
        ResultSet resultSet = null;

        try {
            // Conectar a la base de datos remota
            remoteConn = DbConnection.conectarseRemoto();
            String sqlRecetas = "SELECT id, historial_id, medicamento, dosis FROM recetas;";

            PreparedStatement stmt = remoteConn.prepareStatement(sqlRecetas);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Prescription receta = new Prescription();
                receta.setId(resultSet.getInt("id"));
                receta.setHistory_id(resultSet.getInt("historial_id"));
                receta.setMedicine(resultSet.getString("medicamento"));
                receta.setDose(resultSet.getString("dosis"));

                recetasList.add(receta);
            }

            // Conectar a la base de datos local
            localConn = DbConnection.conectarseLocal();
            localConn.setAutoCommit(false); // Deshabilita auto-commit para mejorar rendimiento

            String sqlInsert = "INSERT INTO prescriptions (id, history_id, medicine, dose) VALUES (?, ?, ?, ?)";
            stmtSave = localConn.prepareStatement(sqlInsert);
            int batchCounter = 0;

            for (Prescription receta : recetasList) {
                stmtSave.setInt(1, receta.getId());
                stmtSave.setInt(2, receta.getHistory_id());
                stmtSave.setString(3, receta.getMedicine());
                stmtSave.setString(4, receta.getDose());

                stmtSave.addBatch();
                batchCounter++;

                if (batchCounter % BATCH_SIZE == 0) {
                    stmtSave.executeBatch();
                    localConn.commit();
                    System.out.println("Migradas " + batchCounter + " recetas...");
                }
            }

            // Ejecutar batch restante
            stmtSave.executeBatch();
            localConn.commit();

            // Actualizar secuencia en PostgreSQL
            Statement stmtUpdateSeq = localConn.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('prescriptions', 'id'), (SELECT MAX(id) FROM prescriptions))");
            stmtUpdateSeq.close();

            System.out.println("Migración de recetas completada exitosamente.");
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
