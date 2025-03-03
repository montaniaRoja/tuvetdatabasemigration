package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.CustomerPoints;
import models.DbConnection;

public class CustomerPointsMigrationHelper {

	public static boolean pointsMigration() {
        ArrayList<CustomerPoints> pointsList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlPoints = "select id, id_cliente, invoice_id, usuario_id, monto_factura,\n"
            		+ "monto_acumulado, monto_redimido, fecha\n"
            		+ "from puntos_clientes;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlPoints);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                CustomerPoints point = new CustomerPoints();
                point.setId(result.getInt("id"));
                
                point.setCustomerId(result.getInt("id_cliente"));
                point.setInvoiceId(result.getInt("invoice_id"));
                point.setUserId(result.getInt("usuario_id"));
                point.setInvoiceAmount(result.getDouble("monto_factura"));
                point.setEarnedPoints(result.getDouble("monto_acumulado"));
                point.setRedeemedPoints(result.getDouble("monto_redimido"));
                point.setCreatedAt(result.getDate("fecha"));                
                                
                pointsList.add(point);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSavebrand = "INSERT INTO customer_points (id, customer_id, invoice_id, user_id, invoice_amount, earned_points, redeemed_points,created_at )\n"
            		+"VALUES (?,?,?,?,?,?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSavebrand);
            
            double records=pointsList.size();
            double contador=0;
            double percent=0;
            
            for (CustomerPoints point : pointsList) {            	
                int pointId = point.id;
                int customerId=point.customerId;
                int invoiceId=point.invoiceId;
                int userId=point.userId;
                double invoiceAmount=point.invoiceAmount;
                double earnedPoints=point.earnedPoints;
                double redeemedPoints=point.redeemedPoints;
                Date pointCreatedAt = point.createdAt;                
                
                stmtsave.setInt(1, pointId);
                stmtsave.setInt(2, customerId);
                stmtsave.setInt(3, invoiceId);
                stmtsave.setInt(4, userId);
                stmtsave.setDouble(5, invoiceAmount);
                stmtsave.setDouble(6, earnedPoints);
                stmtsave.setDouble(7, redeemedPoints);
                stmtsave.setDate(8, pointCreatedAt);
                
                contador+=1;
                percent = contador/records*100;
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                	
                   System.out.println("transacciones de puntos migradas "+percent+"%");
                    
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('customer_points', 'id'), (SELECT MAX(id) FROM customer_points))");
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
