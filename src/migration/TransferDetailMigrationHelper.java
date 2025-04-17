package migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.DbConnection;
import models.TransferDetail;

public class TransferDetailMigrationHelper {

    public static boolean transferDetailsMigration() {
        ArrayList<TransferDetail> detailList = new ArrayList<>();
        
        // Obtener datos desde la base de datos remota
        try (Connection connection = DbConnection.conectarseRemoto();
             PreparedStatement stmt = connection.prepareStatement("SELECT id, id_traslado, prod_id, cantidad FROM dtraslados");
             ResultSet result = stmt.executeQuery()) {

            while (result.next()) {
                TransferDetail detail = new TransferDetail();
                detail.setId(result.getInt("id"));
                detail.setTransferId(result.getInt("id_traslado"));
                detail.setProductId(result.getInt("prod_id"));
                detail.setQuantity(result.getInt("cantidad"));
                
                detailList.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (detailList.isEmpty()) {
            System.out.println("No hay datos para migrar en transfer_details.");
            return true;
        }

        // Insertar datos en la base de datos local
        try (Connection guardar = DbConnection.conectarseLocal();
             PreparedStatement stmtsave = guardar.prepareStatement(
                 "INSERT INTO transfer_details (id, transfer_id, product_id, sent_qty) VALUES (?,?,?,?)");
             Statement stmtUpdateSeq = guardar.createStatement()) {

            int batchSize = 100;
            int count = 0;
            int totalRecords = detailList.size();

            for (TransferDetail detail : detailList) {
                stmtsave.setInt(1, detail.getId());
                stmtsave.setInt(2, detail.getTransferId());
                stmtsave.setInt(3, detail.getProductId());
                stmtsave.setInt(4, detail.getQuantity());

                stmtsave.addBatch();
                count++;

                if (count % batchSize == 0 || count == totalRecords) {
                    stmtsave.executeBatch();
                    System.out.printf("Progreso de migración: %.2f%%%n", (count / (double) totalRecords) * 100);
                }
            }

            // Actualizar la secuencia del ID en PostgreSQL
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('transfer_details', 'id'), COALESCE((SELECT MAX(id) FROM transfer_details), 1))");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        System.out.println("Migración de transfer_details completada.");
        return true;
    }
}
