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
	
	public static boolean exitsMigration() {
        ArrayList<StockExit> exitsList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlExits = "select id, sucursal_id, prod_id, cantidad, detalle, procesa_id, autoriza_id, estado, fecha from salidas;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlExits);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                StockExit exit = new StockExit();
                exit.setId(result.getInt("id"));
                exit.setBranchId(result.getInt("sucursal_id"));
                exit.setProductId(result.getInt("prod_id"));
                exit.setQuantity(result.getInt("cantidad"));
                exit.setExplanation(result.getString("detalle"));
                exit.setCreatedBy(result.getInt("procesa_id"));
                exit.setAuthorizedBy(result.getInt("autoriza_id"));
                exit.setStatus(result.getString("estado"));
                
                exit.setCreatedAt(result.getDate("fecha"));
                
                exitsList.add(exit);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSaveUser = "INSERT INTO stock_exits (id, branch_id, product_id, quantity, explanation, created_by, authorized_by,status, created_at)\n"
            		+"VALUES (?,?,?,?,?,?,?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveUser);
            
            double records=exitsList.size();
            double contador=0;
            double percent=0;
            
            for (StockExit exit : exitsList) {
            	
                int exitId = exit.id;
                int branchId = exit.branchId;
                int prodId = exit.productId;
                int qty=exit.quantity;                
                String explanation = exit.explanation;
                int createdBy=exit.createdBy;
                int authorizedBy=exit.authorizedBy;
                String status=exit.status;                
                Date createdAt = exit.createdAt;
                
                
                stmtsave.setInt(1, exitId);
                stmtsave.setInt(2, branchId);
                stmtsave.setInt(3, prodId);
                stmtsave.setInt(4, qty);
                stmtsave.setString(5, explanation);
                stmtsave.setInt(6, createdBy);
                stmtsave.setInt(7, authorizedBy);
                stmtsave.setString(8, status);
                
                stmtsave.setDate(9, createdAt);
                
                int rows = stmtsave.executeUpdate();
                
                contador+=1;
                percent = contador/records*100;
                
                if (rows > 0) {
                	
                   System.out.println("Porcentaje de Avance salidas "+percent+"%");                    
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('stock_exits', 'id'), (SELECT MAX(id) FROM stock_exits))");
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
