package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import models.BranchLevel;
import models.DbConnection;

public class StockMigrationHelper {
    
    public static boolean levelsMigration() {
        ArrayList<BranchLevel> branchLevelList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        Connection guardar = null;
        PreparedStatement stmtsave = null;
        Statement stmtUpdateSeq = null;

        try {
            // Conectarse a la base de datos remota
            connection = DbConnection.conectarseRemoto();
            String sqlLevel = "SELECT \n"
                    + "    e.id_sucursal,\n"
                    + "    e.id_producto,\n"
                    + "    p.ptoreorden,\n"
                    + "    e.existencia,\n"
                    + "    e.porc_descuento\n"
                    + "FROM \n"
                    + "    existencias e\n"
                    + "LEFT JOIN \n"
                    + "    puntosdereorden p ON e.id_sucursal = p.id_sucursal AND e.id_producto = p.id_producto;";
            
            // Preparar y ejecutar la consulta
            stmt = connection.prepareStatement(sqlLevel);
            result = stmt.executeQuery();
            while (result.next()) {
                BranchLevel level = new BranchLevel();
                level.setBranch_id(result.getInt("id_sucursal"));
                level.setProduct_id(result.getInt("id_producto"));
                level.setReorder_point(result.getInt("ptoreorden"));
                level.setStock_amount(result.getInt("existencia"));
                level.setProduct_discount(result.getDouble("porc_descuento"));
                level.setCreated_at(LocalDateTime.now());
                level.setUpdated_at(LocalDateTime.now());
                
                branchLevelList.add(level);
            }
            
            // Conectarse a la base de datos local para insertar datos
            guardar = DbConnection.conectarseLocal();
            String sqlSaveLevel = "INSERT INTO stocklevels ( branch_id, product_id, reorder_point, stock_amount, product_discount, created_at, updated_at) VALUES (?,?,?,?,?,?,?);";
            stmtsave = guardar.prepareStatement(sqlSaveLevel);
            
            double records=branchLevelList.size();
            double contador=0;
            double percent=0;
            
            
            for (BranchLevel level : branchLevelList) {
              

                stmtsave.setInt(1, level.getBranch_id());
                stmtsave.setInt(2, level.getProduct_id());
                stmtsave.setInt(3, level.getReorder_point());
                stmtsave.setInt(4, level.getStock_amount());
                stmtsave.setDouble(5, level.getProduct_discount());
                stmtsave.setDate(6, Date.valueOf(level.getCreated_at().toLocalDate()));
                stmtsave.setDate(7, Date.valueOf(level.getUpdated_at().toLocalDate()));

                int rows = stmtsave.executeUpdate();
                
                contador+=1;
                percent = contador/records*100;
                
                if (rows > 0) {
                	
                   System.out.println("Porcentaje de Avance Stock "+percent+"%");                    
                }
            }
            
            // Actualizar la secuencia una vez al final
            stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('stocklevels', 'id'), (SELECT MAX(id) FROM stocklevels))");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            // Cerrar todos los recursos en el bloque finally
            try {
                if (stmt != null) stmt.close();
                if (result != null) result.close();
                if (guardar != null) guardar.close();
                if (stmtsave != null) stmtsave.close();
                if (stmtUpdateSeq != null) stmtUpdateSeq.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return true;
    }
}
