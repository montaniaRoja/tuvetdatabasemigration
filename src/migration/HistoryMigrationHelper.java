package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import models.DbConnection;
import models.MedicalHistory;

public class HistoryMigrationHelper {
    private static final int BATCH_SIZE = 500; // Definir el tamaño de los lotes

    public static boolean historyMigration() {
        ArrayList<MedicalHistory> historyList = new ArrayList<>();
        Connection remoteConn = null;
        Connection localConn = null;
        PreparedStatement stmtSave = null;
        ResultSet resultSet = null;

        try {
            // Conectar a la base de datos remota
            remoteConn = DbConnection.conectarseRemoto();
            String sqlHistory = "SELECT id, mascota_id, medico_id, sucursal_atendio, motivo_visita, anamnesis, sintomas_mascota, habitat, " +
                                "temperatura_mascota, dieta, peso_mascota, diagnostico, fecha_visita, proxima_cita FROM historial_mascotas";
            
            PreparedStatement stmt = remoteConn.prepareStatement(sqlHistory);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                MedicalHistory history = new MedicalHistory();
                history.setId(resultSet.getInt("id"));
                history.setPetId(resultSet.getInt("mascota_id"));
                history.setVetId(resultSet.getInt("medico_id"));
                history.setBranchId(resultSet.getInt("sucursal_atendio"));
                history.setReason(resultSet.getString("motivo_visita"));
                history.setAnamnesis(resultSet.getString("anamnesis"));
                history.setSymptoms(resultSet.getString("sintomas_mascota"));
                history.setHabitat(resultSet.getString("habitat"));
                history.setTemperature(resultSet.getString("temperatura_mascota"));
                history.setDiet(resultSet.getString("dieta"));
                history.setWeight(resultSet.getString("peso_mascota"));
                history.setDiagnosis(resultSet.getString("diagnostico"));
                history.setCreatedAt(resultSet.getDate("fecha_visita"));
                history.setNextVisit(resultSet.getDate("proxima_cita"));
                
                historyList.add(history);
            }

            // Conectar a la base de datos local
            localConn = DbConnection.conectarseLocal();
            localConn.setAutoCommit(false); // Deshabilita auto-commit para mejorar rendimiento

            String sqlInsert = "INSERT INTO pet_histories (id, pet_id, vet_id, branch_id, reason, anamnesis, symptoms, habitat, temperature, " +
                               "diet, weight, diagnosis, next_visit, created_at) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmtSave = localConn.prepareStatement(sqlInsert);
            int batchCounter = 0;

            for (MedicalHistory history : historyList) {
                stmtSave.setInt(1, history.getId());
                stmtSave.setInt(2, history.getPetId());
                stmtSave.setInt(3, history.getVetId());
                stmtSave.setInt(4, history.getBranchId());
                stmtSave.setString(5, history.getReason());
                stmtSave.setString(6, history.getAnamnesis());
                stmtSave.setString(7, history.getSymptoms());
                stmtSave.setString(8, history.getHabitat());
                stmtSave.setString(9, history.getTemperature());
                stmtSave.setString(10, history.getDiet());
                stmtSave.setString(11, history.getWeight());
                stmtSave.setString(12, history.getDiagnosis());
                stmtSave.setDate(13, history.getNextVisit());
                stmtSave.setDate(14, history.getCreatedAt());

                stmtSave.addBatch();
                batchCounter++;

                // Ejecutar el batch después de alcanzar el tamaño definido
                if (batchCounter % BATCH_SIZE == 0) {
                    stmtSave.executeBatch();
                    localConn.commit();
                    System.out.printf("Migradas %d historiales...\n", batchCounter);
                }
            }

            // Ejecutar el batch restante
            stmtSave.executeBatch();
            localConn.commit();

            // Actualizar la secuencia en PostgreSQL
            Statement stmtUpdateSeq = localConn.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('pet_histories', 'id'), (SELECT MAX(id) FROM pet_histories))");
            stmtUpdateSeq.close();

            System.out.println("Migración de historiales completada exitosamente.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (localConn != null) {
                try {
                    localConn.rollback(); // Realiza rollback en caso de error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return false;

        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (stmtSave != null) stmtSave.close();
                if (localConn != null) localConn.setAutoCommit(true); // Restaurar el auto-commit
                if (localConn != null) localConn.close();
                if (remoteConn != null) remoteConn.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
}
