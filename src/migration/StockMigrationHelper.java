package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDateTime;


import models.BranchLevel;
import models.DbConnection;

public class StockMigrationHelper {
	
	
	public static boolean levelsMigration() {
        ArrayList<BranchLevel> branchLevelList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
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
            
            PreparedStatement stmt = connection.prepareStatement(sqlLevel);
            ResultSet result = stmt.executeQuery();
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
            int counter=0;
            for (BranchLevel level : branchLevelList) {
                counter+=1;
                
                int branchId = level.branch_id;
                int productId=level.product_id;
                int reorderPoint=level.reorder_point;
                int stockAmount=level.stock_amount;
                double discount=level.product_discount;
                LocalDateTime createdAt=level.created_at;
                LocalDateTime updatedAt=level.updated_at;
                
                Connection guardar = DbConnection.conectarseLocal();
                String sqlSaveLevel = "INSERT INTO stocklevels ( branch_id, product_id, reorder_point, stock_amount, product_discount, created_at, updated_at) VALUES (?,?,?,?,?,?,?);";
                PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveLevel);
                stmtsave.setInt(1, branchId);
                stmtsave.setInt(2, productId);
                stmtsave.setInt(3, reorderPoint);
                stmtsave.setInt(4, stockAmount);
                stmtsave.setDouble(5, discount);
                stmtsave.setDate(6, Date.valueOf(createdAt.toLocalDate()));
                stmtsave.setDate(7, Date.valueOf(updatedAt.toLocalDate()));
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                    System.out.println("level guardada exitosamente "+counter);
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