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
            String sqlBreeds = "SELECT id, historial_id, analisis_id FROM analisis_mascota;";
            
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
            String sqlSavebrand = "INSERT INTO pet_analyses (id, history_id, analysis_id) VALUES (?, ?, ?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSavebrand);
            
            double records = analysisList.size();
            double contador = 0;
            double percent = 0;
            int batchSize = 1000; // Tamaño del batch
            int batchCount = 0;
            
            for (PetAnalysis analysis : analysisList) {
                stmtsave.setInt(1, analysis.getId());
                stmtsave.setInt(2, analysis.getHistorialId());               
                stmtsave.setInt(3, analysis.getAnalysisId());
                
                stmtsave.addBatch();
                batchCount++;
                contador++;
                percent = (contador / records) * 100;
                
                if (batchCount % batchSize == 0) {
                    stmtsave.executeBatch();
                    System.out.println("Porcentaje de Avance analysis: " + percent + "%");
                    batchCount = 0;
                }
            }
            
            if (batchCount > 0) {
                stmtsave.executeBatch(); // Ejecutar los restantes
                System.out.println("Porcentaje de Avance analysis: 100%");
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('pet_analyses', 'id'), (SELECT MAX(id) FROM pet_analyses))");
            stmtUpdateSeq.close();
            
            stmtsave.close();
            guardar.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
}
