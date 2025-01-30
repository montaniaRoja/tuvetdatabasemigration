package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import models.Category;
import models.DbConnection;

public class CategoryMigrationHelper {
	public static boolean categoriesMigration() {
        ArrayList<Category> categoryList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlCats = "select id, cat_nombre, cat_descripcion,activosn,  fecha_creacion\n"
            		+ "from categorias;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlCats);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Category cat = new Category();
                cat.setId(result.getInt("id"));
                cat.setName(result.getString("cat_nombre"));
                cat.setDescription(result.getString("cat_descripcion"));
                cat.setIs_active(result.getBoolean("activosn"));
                cat.setCreated_by(1);
                cat.setCreated_at(result.getDate("fecha_creacion"));
                
                                
                categoryList.add(cat);
            }
            
            for (Category cat : categoryList) {            	
                int catId = cat.id;
                String catName = cat.name;
                String catDescription = cat.description;
                boolean catIsActive=cat.is_active;
                int catCreatedBy = cat.created_by;
                Date catCreatedAt = cat.created_at;
                
                Connection guardar = DbConnection.conectarseLocal();
                String sqlSaveCat = "INSERT INTO categories (id, name, description, is_active, created_by,created_at )\n"
                		+"VALUES (?,?,?,?,?,?);";
                PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveCat);
                stmtsave.setInt(1, catId);
                stmtsave.setString(2, catName);
                stmtsave.setString(3, catDescription);
                stmtsave.setBoolean(4, catIsActive);                
                stmtsave.setInt(5, catCreatedBy);
                stmtsave.setDate(6, catCreatedAt);
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                    System.out.println("categoria guardado exitosamente");
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