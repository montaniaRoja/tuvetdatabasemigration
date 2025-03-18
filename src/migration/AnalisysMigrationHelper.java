package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Analysis;
import models.DbConnection;

public class AnalisysMigrationHelper {
	public static boolean analysisMigration() {
        ArrayList<Analysis> analysisList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlBreeds = "select id, nombre_analisis, fecha_creacion from analisis;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlBreeds);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Analysis analysis = new Analysis();
                analysis.setId(result.getInt("id"));
                analysis.setName(result.getString("nombre_analisis"));
                
                analysis.setCreatedAt(result.getDate("fecha_creacion"));
                
                                
                analysisList.add(analysis);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSavebrand = "INSERT INTO analyses (id, name, created_at )\n"
            		+"VALUES (?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSavebrand);
            
            double records=analysisList.size();
            double contador=0;
            double percent=0;
            
            for (Analysis analysis : analysisList) {            	
                int analysisId = analysis.id;
                String analysisName = analysis.name;
                
                Date createdAt= analysis.createdAt;         
                
                
                
                stmtsave.setInt(1, analysisId);
                stmtsave.setString(2, analysisName);               
                
                stmtsave.setDate(3, createdAt);
                
                contador+=1;
                percent = contador/records*100;
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                	
                   System.out.println("Porcentaje de Avance analyisis "+percent+"%");
                    
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('analyses', 'id'), (SELECT MAX(id) FROM analyses))");
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