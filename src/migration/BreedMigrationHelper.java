package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Breed;
import models.DbConnection;

public class BreedMigrationHelper {
	public static boolean breedsMigration() {
        ArrayList<Breed> breedList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlBreeds = "select id, nombre_raza, nombre_especie, fecha_creacion from razas;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlBreeds);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Breed breed = new Breed();
                breed.setId(result.getInt("id"));
                breed.setName(result.getString("nombre_raza"));
                breed.setSpecie(result.getString("nombre_especie"));
                breed.setCreatedAt(result.getDate("fecha_creacion"));
                
                                
                breedList.add(breed);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSavebrand = "INSERT INTO breeds (id, name, specie, created_by, created_at )\n"
            		+"VALUES (?,?,?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSavebrand);
            
            double records=breedList.size();
            double contador=0;
            double percent=0;
            
            for (Breed breed : breedList) {            	
                int breedId = breed.id;
                String breedName = breed.name;
                String specie = breed.specie;
                int createdBy = 1;
                Date createdAt= breed.createdAt;         
                
                
                
                stmtsave.setInt(1, breedId);
                stmtsave.setString(2, breedName);
                stmtsave.setString(3, specie);
                
                stmtsave.setInt(4, createdBy);
                
                stmtsave.setDate(5, createdAt);
                
                contador+=1;
                percent = contador/records*100;
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                	
                   System.out.println("Porcentaje de Avance Raza "+percent+"%");
                    
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('breeds', 'id'), (SELECT MAX(id) FROM breeds))");
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