package migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            
            for (Country country : countryList) {            	
                int countryId = country.id;
                String countryName = country.name;
                
                
                Connection guardar = DbConnection.conectarseLocal();
                String sqlSaveCountry = "INSERT INTO countries (id, name)\n"
                		+"VALUES (?,?);";
                PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveCountry);
                stmtsave.setInt(1, countryId);
                stmtsave.setString(2, countryName);
                                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                    System.out.println("pais guardado exitosamente");
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