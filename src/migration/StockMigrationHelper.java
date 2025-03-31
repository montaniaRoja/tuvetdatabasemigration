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
            // Conexión a la base remota
            connection = DbConnection.conectarseRemoto();
            String sqlLevel = "SELECT e.id_sucursal, e.id_producto, p.ptoreorden, e.existencia, e.porc_descuento " +
                              "FROM existencias e " +
                              "LEFT JOIN puntosdereorden p ON e.id_sucursal = p.id_sucursal AND e.id_producto = p.id_producto";

            stmt = connection.prepareStatement(sqlLevel);
            result = stmt.executeQuery();

            while (result.next()) {
                BranchLevel level = new BranchLevel();
                level.setBranch_id(result.getInt("id_sucursal"));
                level.setProduct_id(result.getInt("id_producto"));
                level.setReorder_point(result.getObject("ptoreorden") != null ? result.getInt("ptoreorden") : 0);
                level.setStock_amount(result.getInt("existencia"));
                level.setProduct_discount(result.getDouble("porc_descuento"));
                level.setCreated_at(LocalDateTime.now());
                level.setUpdated_at(LocalDateTime.now());

                branchLevelList.add(level);
            }

            // Si no hay datos, salir
            if (branchLevelList.isEmpty()) {
                System.out.println("No hay datos para migrar.");
                return false;
            }

            // Conexión a la base local
            guardar = DbConnection.conectarseLocal();
            String sqlSaveLevel = "INSERT INTO stocklevels (branch_id, product_id, reorder_point, stock_amount, product_discount, created_at, updated_at) " +
                                  "VALUES (?, ?, ?, ?, ?, ?, ?)";

            stmtsave = guardar.prepareStatement(sqlSaveLevel);

            int batchSize = 1000;  // Ejecutar batch cada 1000 registros
            int count = 0;

            for (BranchLevel level : branchLevelList) {
                stmtsave.setInt(1, level.getBranch_id());
                stmtsave.setInt(2, level.getProduct_id());
                stmtsave.setInt(3, level.getReorder_point());
                stmtsave.setInt(4, level.getStock_amount());
                stmtsave.setDouble(5, level.getProduct_discount());
                stmtsave.setDate(6, Date.valueOf(level.getCreated_at().toLocalDate()));
                stmtsave.setDate(7, Date.valueOf(level.getUpdated_at().toLocalDate()));

                stmtsave.addBatch();
                count++;

                if (count % batchSize == 0) {
                    stmtsave.executeBatch();
                }
            }

            stmtsave.executeBatch();  // Ejecutar los registros restantes

            // Actualizar la secuencia solo si se insertaron registros
            stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('stocklevels', 'id'), COALESCE((SELECT MAX(id) FROM stocklevels), 1), false)");

            System.out.println("Migración de stock completada con éxito.");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            // Liberar recursos
            try {
                if (result != null) result.close();
                if (stmt != null) stmt.close();
                if (stmtsave != null) stmtsave.close();
                if (stmtUpdateSeq != null) stmtUpdateSeq.close();
                if (guardar != null) guardar.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return true;
    }
}
