package migration;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Pet;
import models.DbConnection;

public class PetMigrationHelper {
	public static boolean petsMigration() {
        ArrayList<Pet> petsList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlBreeds = "select id, nombre_mascota, propietario_id, especie, raza_id, edad, sexo, tipo_pelo, color,\n"
            		+ "tamanio, caracteristicas, estado_reproductivo, habitat, dieta,  activa_sn, sucursal_crea\n"
            		+ "from mascotas;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlBreeds);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Pet pet = new Pet();
                pet.setId(result.getInt("id"));
                pet.setName(result.getString("nombre_mascota"));
                pet.setOwnerId(result.getInt("propietario_id"));
                pet.setSpecie(result.getString("especie"));
                pet.setBreedId(result.getInt("raza_id"));
                pet.setAge(result.getString("edad"));
                pet.setGender(result.getString("sexo"));
                pet.setHairType(result.getString("tipo_pelo"));
                pet.setColor(result.getString("color"));
                pet.setSize(result.getString("tamanio"));
                pet.setParticularSigns(result.getString("caracteristicas"));
                pet.setReproductiveStatus(result.getString("estado_reproductivo"));
                pet.setHasChip(false);
                pet.setHabitat(result.getString("habitat"));
                pet.setDiet(result.getString("dieta"));
                pet.setActive(true);
                pet.setBranchId(result.getInt("sucursal_crea"));
                pet.setCreatedBy(1);
                                
                petsList.add(pet);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSavebrand = "INSERT INTO pets (id, name, owner_id, specie, age, breed_id, male_female, hair_type, color,\n"
            		+"size, particular_signs, reproductive_status, has_chip, habitat, diet, branch_id,created_by )\n"
            		+"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSavebrand);
            
            double records=petsList.size();
            double contador=0;
            double percent=0;
            
            for (Pet pet : petsList) {            	
                int petId = pet.id;
                String petName = pet.name;
                int ownerId = pet.ownerId;
                String specie = pet.specie;
                String age = pet.age;
                int breedId = pet.breedId;
                String gender=pet.gender;
                String hairType=pet.hairType;
                String color=pet.color;
                String size=pet.size;
                String ps=pet.particularSigns;
                String rs=pet.reproductiveStatus;
                boolean hasChip=pet.hasChip;
                String habitat=pet.habitat;
                String diet=pet.diet;
                int branchId = pet.branchId;
                int createdBy=pet.createdBy;
                
                
                
                stmtsave.setInt(1, petId);
                stmtsave.setString(2, petName);
                stmtsave.setInt(3, ownerId);                
                stmtsave.setString(4, specie);
                stmtsave.setString(5, age);
                stmtsave.setInt(6, breedId);
                stmtsave.setString(7, gender);
                stmtsave.setString(8, hairType);
                stmtsave.setString(9, color);
                stmtsave.setString(10, size);
                stmtsave.setString(11, ps);
                stmtsave.setString(12, rs);
                stmtsave.setBoolean(13, hasChip);
                stmtsave.setString(14, habitat);
                stmtsave.setString(15, diet);
                stmtsave.setInt(16, branchId);
                stmtsave.setInt(17, createdBy);
                
                
                
                contador+=1;
                percent = contador/records*100;
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                	
                   System.out.println("Porcentaje de Avance mascotas "+percent+"%");
                    
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('pets', 'id'), (SELECT MAX(id) FROM pets))");
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