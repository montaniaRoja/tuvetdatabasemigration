package migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.PetAnalysis;
import models.DbConnection;

public class PetAnalysisMigrationHelper {
	public static boolean analysisMigration() {
        ArrayList<PetAnalysis> analysisList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlBreeds = "select id, historial_id, analisis_id from analisis_mascota;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlBreeds);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                PetAnalysis analysis = new PetAnalysis();
                analysis.setId(result.getInt("id"));
                analysis.setHistorialId(result.getInt("historial_id"));
                
                analysis.setAnalysisId(result.getInt("analisis_id"));
                
                                
                analysisList.add(analysis);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSavebrand = "INSERT INTO pet_analyses (id, history_id, analysis_id )\n"
            		+"VALUES (?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSavebrand);
            
            double records=analysisList.size();
            double contador=0;
            double percent=0;
            
            for (PetAnalysis analysis : analysisList) {            	
                int analysisId = analysis.id;
                int hId = analysis.historialId;
                int aId = analysis.analysisId;
                 
                
                
                stmtsave.setInt(1, analysisId);
                stmtsave.setInt(2, hId);               
                
                stmtsave.setInt(3, aId);
                
                contador+=1;
                percent = contador/records*100;
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                	
                   System.out.println("Porcentaje de Avance analyisis "+percent+"%");
                    
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('pet_analyses', 'id'), (SELECT MAX(id) FROM pet_analyses))");
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
