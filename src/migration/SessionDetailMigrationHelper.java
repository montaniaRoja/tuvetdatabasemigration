package migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.DbConnection;
import models.SessionDetail;

public class SessionDetailMigrationHelper {

	
	private static final int BATCH_SIZE = 500; // Ajusta según el rendimiento de la base de datos

    public static boolean sessionMigration() {
        ArrayList<SessionDetail> sessionList = new ArrayList<>();

        Connection remoteConn = null;
        Connection localConn = null;
        PreparedStatement stmtSave = null;
        ResultSet resultSet = null;

        try {
            // Conectar a la base de datos remota
            remoteConn = DbConnection.conectarseRemoto();
            String sqlPoints = "select id, id_sesion, id_servicioprestado from tbl_detallesesiones order by id desc;";
            
            PreparedStatement stmt = remoteConn.prepareStatement(sqlPoints);
            resultSet = stmt.executeQuery();
            
            while (resultSet.next()) {
                SessionDetail session = new SessionDetail();
                session.setId(resultSet.getInt("id"));
                session.setIdSesion(resultSet.getInt("id_sesion"));
                session.setIdServicioPrestado(resultSet.getInt("id_servicioprestado"));
                
                
                
                sessionList.add(session);
            }

            // Conectar a la base de datos local
            localConn = DbConnection.conectarseLocal();
            localConn.setAutoCommit(false); // Deshabilitar auto-commit para batch processing
            
            String sqlInsert = "INSERT INTO grooming_session_details " +
                               "(id, id_sesion, id_servicioprestado " +
                               ") VALUES (?,?,?)";
            
            stmtSave = localConn.prepareStatement(sqlInsert);
            int batchCounter = 0;

            for (SessionDetail session : sessionList) {
                stmtSave.setInt(1, session.getId());
                stmtSave.setInt(2, session.getIdSesion());
                stmtSave.setInt(3, session.getIdServicioPrestado());
                               

                stmtSave.addBatch();
                batchCounter++;

                if (batchCounter % BATCH_SIZE == 0) {
                    stmtSave.executeBatch();
                    localConn.commit();
                    System.out.println("Migrados " + batchCounter + " detalles de sesion...");
                }
            }

            // Ejecutar cualquier batch restante
            stmtSave.executeBatch();
            localConn.commit();

            // Actualizar secuencia en PostgreSQL
            Statement stmtUpdateSeq = localConn.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('grooming_session_details', 'id'), (SELECT MAX(id) FROM grooming_session_details))");
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
