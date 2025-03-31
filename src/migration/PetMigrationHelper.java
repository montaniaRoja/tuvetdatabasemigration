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
    private static final int BATCH_SIZE = 500; // Define el tamaño del batch para mejorar el rendimiento

    public static boolean petsMigration() {
        ArrayList<Pet> petsList = new ArrayList<>();

        Connection remoteConn = null;
        Connection localConn = null;
        PreparedStatement stmtSave = null;
        ResultSet resultSet = null;

        try {
            // Conectar a la base de datos remota
            remoteConn = DbConnection.conectarseRemoto();
            String sqlBreeds = "SELECT id, nombre_mascota, propietario_id, especie, raza_id, edad, sexo, tipo_pelo, color, " +
                               "tamanio, caracteristicas, estado_reproductivo, habitat, dieta, activa_sn, sucursal_crea " +
                               "FROM mascotas;";
            
            PreparedStatement stmt = remoteConn.prepareStatement(sqlBreeds);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Pet pet = new Pet();
                pet.setId(resultSet.getInt("id"));
                pet.setName(resultSet.getString("nombre_mascota"));
                pet.setOwnerId(resultSet.getInt("propietario_id"));
                pet.setSpecie(resultSet.getString("especie"));
                pet.setBreedId(resultSet.getInt("raza_id"));
                pet.setAge(resultSet.getString("edad"));
                pet.setGender(resultSet.getString("sexo"));
                pet.setHairType(resultSet.getString("tipo_pelo"));
                pet.setColor(resultSet.getString("color"));
                pet.setSize(resultSet.getString("tamanio"));
                pet.setParticularSigns(resultSet.getString("caracteristicas"));
                pet.setReproductiveStatus(resultSet.getString("estado_reproductivo"));
                pet.setHasChip(false);
                pet.setHabitat(resultSet.getString("habitat"));
                pet.setDiet(resultSet.getString("dieta"));
                pet.setActive(true);
                pet.setBranchId(resultSet.getInt("sucursal_crea"));
                pet.setCreatedBy(1);
                                
                petsList.add(pet);
            }

            // Conectar a la base de datos local
            localConn = DbConnection.conectarseLocal();
            localConn.setAutoCommit(false); // Deshabilitar auto-commit para mejorar rendimiento
            
            String sqlInsert = "INSERT INTO pets " +
                               "(id, name, owner_id, specie, age, breed_id, male_female, hair_type, color, " +
                               "size, particular_signs, reproductive_status, has_chip, habitat, diet, branch_id, created_by) " +
                               "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
            stmtSave = localConn.prepareStatement(sqlInsert);
            int batchCounter = 0;

            for (Pet pet : petsList) {
                stmtSave.setInt(1, pet.getId());
                stmtSave.setString(2, pet.getName());
                stmtSave.setInt(3, pet.getOwnerId());                
                stmtSave.setString(4, pet.getSpecie());
                stmtSave.setString(5, pet.getAge());
                stmtSave.setInt(6, pet.getBreedId());
                stmtSave.setString(7, pet.getGender());
                stmtSave.setString(8, pet.getHairType());
                stmtSave.setString(9, pet.getColor());
                stmtSave.setString(10, pet.getSize());
                stmtSave.setString(11, pet.getParticularSigns());
                stmtSave.setString(12, pet.getReproductiveStatus());
                stmtSave.setBoolean(13, pet.isHasChip());
                stmtSave.setString(14, pet.getHabitat());
                stmtSave.setString(15, pet.getDiet());
                stmtSave.setInt(16, pet.getBranchId());
                stmtSave.setInt(17, pet.getCreatedBy());

                stmtSave.addBatch();
                batchCounter++;

                if (batchCounter % BATCH_SIZE == 0) {
                    stmtSave.executeBatch();
                    localConn.commit();
                    System.out.println("Migradas " + batchCounter + " mascotas...");
                }
            }

            // Ejecutar batch restante
            stmtSave.executeBatch();
            localConn.commit();

            // Actualizar secuencia en PostgreSQL
            Statement stmtUpdateSeq = localConn.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('pets', 'id'), (SELECT MAX(id) FROM pets))");
            stmtUpdateSeq.close();

            System.out.println("Migración completada exitosamente.");

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (localConn != null) {
                try {
                    localConn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return false;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (stmtSave != null) stmtSave.close();
                if (localConn != null) localConn.setAutoCommit(true);
                if (localConn != null) localConn.close();
                if (remoteConn != null) remoteConn.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
}
