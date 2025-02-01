package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Molecule;
import models.DbConnection;

public class MolMigrationHelper {
	public static boolean moleculesMigration() {
        ArrayList<Molecule> molList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlMols = "select * from tbl_ingredientes;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlMols);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Molecule mol = new Molecule();
                mol.setId(result.getInt("id"));
                mol.setName(result.getString("nombre_ingrediente"));
                mol.setIs_active(true);
                mol.setCreated_by(7);                                
                mol.setCreated_at(result.getDate("fecha_creacion"));                                
                molList.add(mol);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSaveMol = "INSERT INTO molecules (id, name, is_active, created_by, created_at )\n"
            		+"VALUES (?,?,?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveMol);
            
            for (Molecule mol : molList) {            	
                int molId = mol.id;
                String molName = mol.name;
                boolean molIsActive=mol.is_active;
                int molCreatedBy = mol.created_by;
                Date molCreatedAt=mol.created_at;                
                
                stmtsave.setInt(1, molId);
                stmtsave.setString(2, molName);
                stmtsave.setBoolean(3, molIsActive);
                
                stmtsave.setInt(4, molCreatedBy);
                
                stmtsave.setDate(5, molCreatedAt);
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                    
                    
                }
            }
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('molecules', 'id'), (SELECT MAX(id) FROM molecules))");
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