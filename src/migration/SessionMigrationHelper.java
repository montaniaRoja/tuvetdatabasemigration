package migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Session;
import models.DbConnection;

public class SessionMigrationHelper {
	
	private static final int BATCH_SIZE = 500; // Ajusta según el rendimiento de la base de datos

    public static boolean sessionMigration() {
        ArrayList<Session> sessionList = new ArrayList<>();

        Connection remoteConn = null;
        Connection localConn = null;
        PreparedStatement stmtSave = null;
        ResultSet resultSet = null;

        try {
            // Conectar a la base de datos remota
            remoteConn = DbConnection.conectarseRemoto();
            String sqlPoints = "select id, id_gromista, id_mascota, fecha, sesion_status, id_cliente, diferencia, \n"
            		+ "hora_inicio, hora_final, hora_cambio_status, minutos_recepcion from tbl_sesionesgrooming;";
            
            PreparedStatement stmt = remoteConn.prepareStatement(sqlPoints);
            resultSet = stmt.executeQuery();
            
            while (resultSet.next()) {
                Session session = new Session();
                session.setId(resultSet.getInt("id"));
                session.setIdGromista(resultSet.getInt("id_gromista"));
                session.setIdMascota(resultSet.getInt("id_mascota"));
                session.setFecha(resultSet.getDate("fecha"));
                session.setSesionStatus(resultSet.getString("sesion_status"));
                session.setIdCliente(resultSet.getInt("id_cliente"));
                session.setDiference(resultSet.getInt("diferencia"));
                session.setHoraInicio(resultSet.getTimestamp("hora_inicio"));
                session.setHoraFinal(resultSet.getTimestamp("hora_final"));
                session.setHoraCambioStatus(resultSet.getTimestamp("hora_cambio_status"));
                session.setMinutosRecepcion(resultSet.getInt("minutos_recepcion"));
                
                
                sessionList.add(session);
            }

            // Conectar a la base de datos local
            localConn = DbConnection.conectarseLocal();
            localConn.setAutoCommit(false); // Deshabilitar auto-commit para batch processing
            
            String sqlInsert = "INSERT INTO grooming_sessions " +
                               "(id, id_gromista, id_mascota, fecha, sesion_status, id_cliente, " +
                               "diference, hora_inicio, hora_final, hora_cambio_status, minutos_recepcion) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            
            stmtSave = localConn.prepareStatement(sqlInsert);
            int batchCounter = 0;

            for (Session session : sessionList) {
                stmtSave.setInt(1, session.getId());
                stmtSave.setInt(2, session.getIdGromista());
                stmtSave.setInt(3, session.getIdMascota());
                stmtSave.setDate(4, session.getFecha());
                stmtSave.setString(5, session.getSesionStatus());
                stmtSave.setInt(6, session.getIdCliente());
                stmtSave.setInt(7, session.getDiference());
                stmtSave.setTimestamp(8, session.getHoraInicio());
                stmtSave.setTimestamp(9, session.getHoraFinal());
                stmtSave.setTimestamp(10, session.getHoraCambioStatus());
                stmtSave.setInt(11, session.getMinutosRecepcion());
                

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
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('grooming_sessions', 'id'), (SELECT MAX(id) FROM grooming_sessions))");
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
