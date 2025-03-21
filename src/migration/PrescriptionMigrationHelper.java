package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Prescription;
import models.DbConnection;

public class PrescriptionMigrationHelper {
	public static boolean recetasMigration() {
        ArrayList<Prescription> recetasList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlRecetas = "select id, historial_id, medicamento, dosis from recetas;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlRecetas);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Prescription receta = new Prescription();
                receta.setId(result.getInt("id"));
                
                receta.setHistory_id(result.getInt("historial_id"));
                
                
                receta.setMedicine(result.getString("medicamento"));
                receta.setDose(result.getString("dosis"));
                
                
                                
                recetasList.add(receta);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSavebrand = "INSERT INTO prescriptions (id, history_id, medicine, dose )\n"
            		+"VALUES (?,?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSavebrand);
            
            double records=recetasList.size();
            double contador=0;
            double percent=0;
            
            for (Prescription receta : recetasList) {            	
                int recetaId = receta.id;
                int historyId = receta.history_id;
                String medicine = receta.medicine;
                String dosage = receta.dose;
                
                         
                
                
                
                stmtsave.setInt(1, recetaId);
                stmtsave.setInt(2, historyId);
                stmtsave.setString(3, medicine);
                stmtsave.setString(4, dosage);
                
                
                
                contador+=1;
                percent = contador/records*100;
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                	
                   System.out.println("Porcentaje de recetas migradas "+percent+"%");
                    
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('prescriptions', 'id'), (SELECT MAX(id) FROM prescriptions))");
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
