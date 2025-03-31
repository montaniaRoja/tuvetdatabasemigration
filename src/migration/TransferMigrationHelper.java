package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import models.Transfer;
import models.DbConnection;

public class TransferMigrationHelper {
    private static final int BATCH_SIZE = 500; // Tamaño del batch para optimizar la carga

    public static boolean transfersMigration() {
        ArrayList<Transfer> transferList = new ArrayList<>();

        Connection remoteConn = null;
        Connection localConn = null;
        PreparedStatement stmtSave = null;
        ResultSet resultSet = null;

        try {
            // Conectar a la base de datos remota
            remoteConn = DbConnection.conectarseRemoto();
            String sqlTransfers = "SELECT id, cod_origen, cod_destino, id_usuario_crea, " +
                                  "id_usuario_autoriza, id_usuario_recibe, estado, fecha_enviado, fecha_recibido " +
                                  "FROM htraslado;";

            PreparedStatement stmt = remoteConn.prepareStatement(sqlTransfers);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Transfer transfer = new Transfer();
                transfer.setId(resultSet.getInt("id"));
                transfer.setFrom_id(resultSet.getInt("cod_origen"));
                transfer.setTo_id(resultSet.getInt("cod_destino"));
                transfer.setCreated_by(resultSet.getInt("id_usuario_crea"));
                transfer.setAuthorized_by(resultSet.getInt("id_usuario_autoriza"));
                transfer.setReceived_by(resultSet.getInt("id_usuario_recibe"));

                // Convertir estados de la base de datos
                switch (resultSet.getString("estado")) {
                    case "Pendiente": transfer.setStatus("Creado"); break;
                    case "Aprobado": transfer.setStatus("Autorizado"); break;
                    case "Ingresado": transfer.setStatus("Recibido"); break;
                    case "Cancelado": transfer.setStatus("Cancelado"); break;
                    default: transfer.setStatus("Desconocido"); break;
                }

                transfer.setCreated_at(resultSet.getDate("fecha_enviado"));
                transfer.setUpdated_at(resultSet.getDate("fecha_recibido"));

                transferList.add(transfer);
            }

            // Conectar a la base de datos local
            localConn = DbConnection.conectarseLocal();
            localConn.setAutoCommit(false); // Deshabilita auto-commit para mejorar rendimiento

            String sqlInsert = "INSERT INTO transfers " +
                               "(id, from_id, to_id, created_by, authorized_by, received_by, status, created_at, updated_at) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            stmtSave = localConn.prepareStatement(sqlInsert);
            int batchCounter = 0;

            for (Transfer transfer : transferList) {
                stmtSave.setInt(1, transfer.getId());
                stmtSave.setInt(2, transfer.getFrom_id());
                stmtSave.setInt(3, transfer.getTo_id());
                stmtSave.setInt(4, transfer.getCreated_by());
                stmtSave.setInt(5, transfer.getAuthorized_by());
                stmtSave.setInt(6, transfer.getReceived_by());
                stmtSave.setString(7, transfer.getStatus());
                stmtSave.setDate(8, transfer.getCreated_at());
                stmtSave.setDate(9, transfer.getUpdated_at());

                stmtSave.addBatch();
                batchCounter++;

                if (batchCounter % BATCH_SIZE == 0) {
                    stmtSave.executeBatch();
                    localConn.commit();
                    System.out.println("Migradas " + batchCounter + " transferencias...");
                }
            }

            // Ejecutar batch restante
            stmtSave.executeBatch();
            localConn.commit();

            // Actualizar secuencia en PostgreSQL
            Statement stmtUpdateSeq = localConn.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('transfers', 'id'), (SELECT MAX(id) FROM transfers))");
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
