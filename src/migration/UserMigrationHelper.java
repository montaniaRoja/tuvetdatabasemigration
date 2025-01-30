package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import models.User;
import models.DbConnection;

public class UserMigrationHelper {
	 public static boolean usersMigration() {
	        ArrayList<User> userList = new ArrayList<>();
	        try {
	            Connection connection = DbConnection.conectarseRemoto();
	            String sqlUsers = "select e.id, e.nombre_empleado, s.correousuario, \n"
	            		+ "e.passwd, e.sucursal_asignada, e.id_creador, e.activo_sn, e.rol_id,\n"
	            		+ "e.fecha_creacion\n"
	            		+ "from tbl_empleados e\n"
	            		+ "join solicitudcontrasenia s\n"
	            		+ "on e.id_solicitud=s.id;";
	            
	            PreparedStatement stmt = connection.prepareStatement(sqlUsers);
	            ResultSet result = stmt.executeQuery();
	            while (result.next()) {
	                User user = new User();
	                user.setId(result.getInt("id"));
	                user.setName(result.getString("nombre_empleado"));
	                user.setEmail(result.getString("correousuario"));
	                user.setPassword(result.getString("passwd"));
	                user.setRol_id(result.getInt("rol_id"));
	                user.setCreated_at(result.getDate("fecha_creacion"));
	                user.setBranch_id(result.getInt("sucursal_asignada"));
	                user.setAuthorized(true);
	                user.setAuthorized_by(result.getInt("id_creador"));
	                user.setIs_active(result.getBoolean("activo_sn"));
	                userList.add(user);
	            }
	            
	            for (User user : userList) {
	            	System.out.println(user.email+" "+user.name);
	                int userId = user.id;
	                String userName = user.name;
	                String userEmail = user.email;
	                String userPassword = user.password;
	                int userRol = user.rol_id;
	                int userBranch=user.branch_id;
	                boolean userAuthorized=user.authorized;
	                int userAuthBy=user.authorized_by;
	                boolean userIsActive=user.is_active;
	                Date userCreatedAt = user.created_at;
	                
	                Connection guardar = DbConnection.conectarseLocal();
	                String sqlSaveUser = "INSERT INTO users (id, name, email, password, rol_id, branch_id, authorized,authorized_by, is_active, created_at)\n"
	                		+"VALUES (?,?,?,?,?,?,?,?,?,?);";
	                PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveUser);
	                stmtsave.setInt(1, userId);
	                stmtsave.setString(2, userName);
	                stmtsave.setString(3, userEmail);
	                stmtsave.setString(4, userPassword);
	                stmtsave.setInt(5, userRol);
	                stmtsave.setInt(6, userBranch);
	                stmtsave.setBoolean(7, userAuthorized);
	                stmtsave.setInt(8, userAuthBy);
	                stmtsave.setBoolean(9, userIsActive);
	                stmtsave.setDate(10, userCreatedAt);
	                
	                int rows = stmtsave.executeUpdate();
	                
	                if (rows > 0) {
	                    System.out.println("usuario guardado exitosamente");
	                    guardar.close();
	                }
	            }
	            connection.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	        
	        return true;
	    }

}