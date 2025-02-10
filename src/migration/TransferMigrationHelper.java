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
	public static boolean transfersMigration() {
        ArrayList<Transfer> transferList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlTransfers = "select id, cod_origen, cod_destino, id_usuario_crea,\n"
            		+ "id_usuario_autoriza, id_usuario_recibe, estado, fecha_enviado,\n"
            		+ "fecha_recibido\n"
            		+ "from htraslado;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlTransfers);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Transfer transfer = new Transfer();
                transfer.setId(result.getInt("id"));
                transfer.setFrom_id(result.getInt("cod_origen"));
                transfer.setTo_id(result.getInt("cod_destino"));
                transfer.setCreated_by(result.getInt("id_usuario_crea"));
                transfer.setAuthorized_by(result.getInt("id_usuario_autoriza"));
                transfer.setReceived_by(result.getInt("id_usuario_recibe"));
                
                String estado = result.getString("estado");
                if (estado.equals("Pendiente")) {
                    transfer.setStatus("Creado");
                } else if (estado.equals("Aprobado")) {
                    transfer.setStatus("Autorizado");
                } else if (estado.equals("Ingresado")) {
                    transfer.setStatus("Recibido");
                } else if (estado.equals("Cancelado")) {
                    transfer.setStatus("Cancelado");
                }              
                
                                
                transfer.setCreated_at(result.getDate("fecha_enviado"));
                transfer.setUpdated_at(result.getDate("fecha_recibido"));
                                
                transferList.add(transfer);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSaveTransfer = "INSERT INTO transfers (id, from_id, to_id, created_by, authorized_by, received_by, status, created_at, updated_at )\n"
            		+"VALUES (?,?,?,?,?,?,?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveTransfer);
            
            for (Transfer transfer : transferList) {            	
                int tId = transfer.id;
                int tFromId=transfer.from_id;
                int tToId=transfer.to_id;
                int tCreatedById=transfer.created_by;
                int tAuthorizedById=transfer.authorized_by;
                int tReceivedBy=transfer.received_by;   
                String tStatus=transfer.status;
                Date tCreatedAt = transfer.created_at;
                Date tUpdatedAt=transfer.updated_at;
                
                
                stmtsave.setInt(1, tId);
                stmtsave.setInt(2, tFromId);
                stmtsave.setInt(3, tToId);
                stmtsave.setInt(4, tCreatedById);
                stmtsave.setInt(5, tAuthorizedById);
                stmtsave.setInt(6, tReceivedBy);
                stmtsave.setString(7,tStatus);
                stmtsave.setDate(8, tCreatedAt);
                stmtsave.setDate(9, tUpdatedAt);
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                    
                    
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('transfers', 'id'), (SELECT MAX(id) FROM transfers))");
            stmtUpdateSeq.close();
            guardar.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

}
