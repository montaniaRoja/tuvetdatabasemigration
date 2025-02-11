package migration;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.DbConnection;
import models.TransferDetail;

public class TransferDetailMigrationHelper {
	

	public static boolean transferDetailsMigration() {
        ArrayList<TransferDetail> detailList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlTransfers = "select id, id_traslado, prod_id, cantidad\n"
            		+ "from dtraslados;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlTransfers);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                TransferDetail detail = new TransferDetail();
                detail.setId(result.getInt("id"));
                detail.setTransferId(result.getInt("id_traslado"));
                detail.setProductId(result.getInt("prod_id"));
                detail.setQuantity(result.getInt("cantidad"));
                
                detailList.add(detail);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSavePurchase = "INSERT INTO transfer_details (id, transfer_id, product_id, quantity) VALUES (?,?,?,?);";
            		
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSavePurchase);
            
            for (TransferDetail detail : detailList) {            	
                int detailId = detail.id;
                int transferId = detail.transferId;
                int productId = detail.productId;
                int qty = detail.quantity;
                                
                
                stmtsave.setInt(1, detailId);
                stmtsave.setInt(2, transferId);
                stmtsave.setInt(3, productId);
                stmtsave.setInt(4, qty);
                
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                    System.out.println("detalle traslado migrada exitosamente");
                    
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('transfer_details', 'id'), (SELECT MAX(id) FROM transfer_details))");
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
