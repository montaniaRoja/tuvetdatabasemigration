package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.DbConnection;
import models.Groomer;

public class GroomerMigrationHelper {
	public static boolean groomersMigration() {
        ArrayList<Groomer> groomerList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlUsers = "select id, nombre_gromista, sucursal_asignada, fecha_creacion, activo_sn from tbl_gromistas;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlUsers);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Groomer groomer = new Groomer();
                groomer.setId(result.getInt("id"));
                groomer.setName(result.getString("nombre_gromista"));
                
                groomer.setBranchId(result.getInt("sucursal_asignada"));
                groomer.setCreatedAt(result.getDate("fecha_creacion"));
                
                groomer.setActive(result.getBoolean("activo_sn"));
                groomerList.add(groomer);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSaveUser = "INSERT INTO groomers (id, name, branch_id, created_by, is_active, created_at)\n"
            		+"VALUES (?,?,?,?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveUser);
            
            double records=groomerList.size();
            double contador=0;
            double percent=0;
            
            for (Groomer groomer : groomerList) {
            	
                int groomerId = groomer.id;
                String groomerName = groomer.name;
                
                int groomerBranch = groomer.branchId;
                int createdBy=1;
                
                
                boolean groomerIsActive=true;
                Date userCreatedAt = groomer.createdAt;
                
                
                stmtsave.setInt(1, groomerId);
                stmtsave.setString(2, groomerName);
                
                stmtsave.setInt(3, groomerBranch);
                stmtsave.setInt(4, createdBy);
                
                stmtsave.setBoolean(5, groomerIsActive);
                stmtsave.setDate(6, userCreatedAt);
                
                int rows = stmtsave.executeUpdate();
                
                contador+=1;
                percent = contador/records*100;
                
                if (rows > 0) {
                	
                   System.out.println("Porcentaje de groomers "+percent+"%");                    
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('groomers', 'id'), (SELECT MAX(id) FROM groomers))");
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
