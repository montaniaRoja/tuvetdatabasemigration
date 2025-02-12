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
	
	public static boolean entriesMigration() {
        ArrayList<StockEntry> entriesList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlEntries = "select id, fecha, sucursal_id, prod_id, cantidad, detalle, procesa_id, autoriza_id, estado\n"
            		+ "from entradas\n"
            		+ "where procesa_id is not null;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlEntries);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                StockEntry entry = new StockEntry();
                entry.setId(result.getInt("id"));
                entry.setBranchId(result.getInt("sucursal_id"));
                entry.setProductId(result.getInt("prod_id"));
                entry.setQuantity(result.getInt("cantidad"));
                entry.setExplanation(result.getString("detalle"));
                entry.setCreatedBy(result.getInt("procesa_id"));
                entry.setAuthorizedBy(result.getInt("autoriza_id"));
                entry.setStatus(result.getString("estado"));
                
                entry.setCreatedAt(result.getDate("fecha"));
                
                entriesList.add(entry);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSaveUser = "INSERT INTO stock_entries (id, branch_id, product_id, quantity, explanation, created_by, authorized_by,status, created_at)\n"
            		+"VALUES (?,?,?,?,?,?,?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveUser);
            
            for (StockEntry entry : entriesList) {
            	
                int entryId = entry.id;
                int branchId = entry.branchId;
                int prodId = entry.productId;
                int qty=entry.quantity;                
                String explanation = entry.explanation;
                int createdBy=entry.createdBy;
                int authorizedBy=entry.authorizedBy;
                String status=entry.status;                
                Date createdAt = entry.createdAt;
                
                
                stmtsave.setInt(1, entryId);
                stmtsave.setInt(2, branchId);
                stmtsave.setInt(3, prodId);
                stmtsave.setInt(4, qty);
                stmtsave.setString(5, explanation);
                stmtsave.setInt(6, createdBy);
                stmtsave.setInt(7, authorizedBy);
                stmtsave.setString(8, status);
                
                stmtsave.setDate(9, createdAt);
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                    System.out.println("entrada guardada milagrosamente");
                    
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('stock_entries', 'id'), (SELECT MAX(id) FROM stock_entries))");
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
