package migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Country;
import models.DbConnection;

public class CountryMigrationHelper {	
	public static boolean countriesMigration() {
        ArrayList<Country> countryList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlCountries = "select * from paises order by id;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlCountries);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Country country = new Country();
                country.setId(result.getInt("id"));
                country.setName(result.getString("nombre_pais"));
                
                countryList.add(country);
                
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSaveCountry = "INSERT INTO countries (id, name)\n"
            		+"VALUES (?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveCountry);
            
            for (Country country : countryList) {            	
                int countryId = country.id;
                String countryName = country.name;
                
                
               
                stmtsave.setInt(1, countryId);
                stmtsave.setString(2, countryName);
                                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                    
                    
                }
            }
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('countries', 'id'), (SELECT MAX(id) FROM countries))");
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