package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.DbConnection;
import models.MedicalHistory;

public class HistoryMigrationHelper {
	public static boolean historyMigration() {
        ArrayList<MedicalHistory> historyList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlHistory = "select id, mascota_id, medico_id, sucursal_atendio, \n"
            		+ "motivo_visita, anamnesis, sintomas_mascota, habitat,\n"
            		+ "temperatura_mascota, dieta, peso_mascota, diagnostico,\n"
            		+ "fecha_visita, proxima_cita\n"
            		+ "from historial_mascotas;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlHistory);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                MedicalHistory history = new MedicalHistory();
                history.setId(result.getInt("id"));
                history.setPetId(result.getInt("mascota_id"));
                history.setVetId(result.getInt("medico_id"));
                history.setBranchId(result.getInt("sucursal_atendio"));
                history.setReason(result.getString("motivo_visita"));
                history.setAnamnesis(result.getString("anamnesis"));
                history.setSymptoms(result.getString("sintomas_mascota"));
                history.setHabitat(result.getString("habitat"));
                history.setTemperature(result.getString("temperatura_mascota"));
                history.setDiet(result.getString("dieta"));
                history.setWeight(result.getString("peso_mascota"));
                history.setDiagnosis(result.getString("diagnostico"));
                history.setCreatedAt(result.getDate("fecha_visita"));
                history.setNextVisit(result.getDate("proxima_cita"));               
               
                                
                historyList.add(history);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSaveHistory = "INSERT INTO pet_histories (id, pet_id, vet_id, branch_id, reason, anamnesis, symptoms, habitat, temperature,\n"
            		+"diet, weight, diagnosis, next_visit, created_at)\n"
            		+"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveHistory);
            
            double records=historyList.size();
            double contador=0;
            double percent=0;
            
            for (MedicalHistory history : historyList) {            	
                int historyId = history.id;
                int petId=history.petId;
                int vetId = history.vetId;
                int branchId = history.branchId;
                String reason = history.reason;
                String anamnesis = history.anamnesis;
                
                String symptoms=history.symptoms;
                String habitat=history.habitat;
                String temp=history.temperature;
                String diet=history.diet;
                String weight=history.weight;
                		String diagnosis=history.diagnosis;
                		Date nextV=history.nextVisit;
                		Date createdAt=history.createdAt; 
                
                
                stmtsave.setInt(1, historyId);
                stmtsave.setInt(2, petId);
                stmtsave.setInt(3, vetId);
                stmtsave.setInt(4, branchId);
                
                stmtsave.setString(5, reason);
                
                stmtsave.setString(6, anamnesis);
                stmtsave.setString(7, symptoms);
                stmtsave.setString(8, habitat);
                stmtsave.setString(9, temp);
                stmtsave.setString(10, diet);
                stmtsave.setString(11, weight);
                stmtsave.setString(12, diagnosis);
                stmtsave.setDate(13, nextV);
                stmtsave.setDate(14, createdAt);
                 
                
                
                contador+=1;
                percent = contador/records*100;
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                	
                   System.out.println("Porcentaje de Avance historiales "+percent+"%");
                    
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('pet_histories', 'id'), (SELECT MAX(id) FROM pet_histories))");
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
