package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.DbConnection;
import models.Brand;

public class BrandMigrationHelper {
	public static boolean brandsMigration() {
        ArrayList<Brand> brandList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlBrands = "select \n"
            		+ "id, marca_nombre, proveedor_id, fecha_creacion, pais_id, activosn\n"
            		+ "from marcas;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlBrands);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Brand brand = new Brand();
                brand.setId(result.getInt("id"));
                brand.setName(result.getString("marca_nombre"));
                brand.setCountry_id(result.getInt("pais_id"));
                brand.setSupplier_id(result.getInt("proveedor_id"));                
                brand.setCreated_at(result.getDate("fecha_creacion"));                
                brand.setCreated_by(1);
                brand.setIs_active(result.getBoolean("activosn"));                
                brandList.add(brand);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSavebrand = "INSERT INTO brands (id, name, country_id, supplier_id, created_by, is_active, created_at )\n"
            		+"VALUES (?,?,?,?,?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSavebrand);
            
            for (Brand brand : brandList) {            	
                int brandId = brand.id;
                String brandName = brand.name;
                int brandCountry = brand.country_id;
                int brandSup = brand.supplier_id;
                int brandCreatedBy = brand.created_by;
                boolean brandIsActive=brand.is_active;
                Date brandCreatedAt = brand.created_at;
                
                
                stmtsave.setInt(1, brandId);
                stmtsave.setString(2, brandName);
                stmtsave.setInt(3, brandCountry);
                stmtsave.setInt(4, brandSup);
                stmtsave.setInt(5, brandCreatedBy);
                stmtsave.setBoolean(6, brandIsActive);
                stmtsave.setDate(7, brandCreatedAt);
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                    
                    
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('brands', 'id'), (SELECT MAX(id) FROM brands))");
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