package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Tax;
import models.DbConnection;

public class TaxMigrationHelper {
	public static boolean taxesMigration() {
        ArrayList<Tax> taxList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlTaxes = "select imp_id, imp_nombre, imp_porc, fecha_creacion, activosn from impuestos;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlTaxes);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Tax tax = new Tax();
                tax.setId(result.getInt("imp_id"));
                tax.setName(result.getString("imp_nombre"));
                tax.setPercentage(result.getFloat("imp_porc"));
                tax.setCreated_at(result.getDate("fecha_creacion"));
                tax.setIs_active(result.getBoolean("activosn"));               
                
                taxList.add(tax);
                
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSaveCountry = "INSERT INTO taxes (id, name, percentage, is_active, created_at, created_by)\n"
            		+"VALUES (?,?,?,?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveCountry);
            
            double records=taxList.size();
            double contador=0;
            double percent=0;
            
            for (Tax tax : taxList) {            	
                int taxId = tax.id;
                String taxName = tax.name;
                float taxPercent=tax.percentage;
                boolean taxIsActive=tax.is_active;
                Date taxCreatedAt=tax.created_at;
                int taxCreatedBy=tax.created_by;
                
                
                
                stmtsave.setInt(1, taxId);
                stmtsave.setString(2, taxName);
                stmtsave.setFloat(3, taxPercent);
                stmtsave.setBoolean(4, taxIsActive);
                stmtsave.setDate(5, taxCreatedAt);
                stmtsave.setInt(6, taxCreatedBy);
                                
                int rows = stmtsave.executeUpdate();
                
                contador+=1;
                percent = contador/records*100;
                
                if (rows > 0) {
                	
                   System.out.println("Porcentaje de Avance impuestos "+percent+"%");                    
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('taxes', 'id'), (SELECT MAX(id) FROM taxes))");
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