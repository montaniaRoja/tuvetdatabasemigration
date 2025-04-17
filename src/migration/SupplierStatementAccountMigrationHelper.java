package migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.DbConnection;
import models.SupplierStatementAccount;

public class SupplierStatementAccountMigrationHelper {
	
	public static boolean statementMigration() {
        ArrayList<SupplierStatementAccount> statementList = new ArrayList<>();
        
        // Obtener datos desde la base de datos remota
        try (Connection connection = DbConnection.conectarseRemoto();
             PreparedStatement stmt = connection.prepareStatement("select id, prov_id, doc_numero, cargo, abono, \n"
             		+ "sucursal, fecha\n"
             		+ "from extractosproveedores;");
             ResultSet result = stmt.executeQuery()) {

            while (result.next()) {
                SupplierStatementAccount detail = new SupplierStatementAccount();
                detail.setId(result.getInt("id"));
                detail.setSupplierId(result.getInt("prov_id"));
                detail.setInvoiceNumber(result.getString("doc_numero"));
                detail.setInvoiceAmount(result.getDouble("cargo"));
                detail.setPaymentAmount(result.getDouble("abono"));
                detail.setCreatedBy(3);
                detail.setBranchId(7);
                detail.setStatus("Pendiente");
                detail.setCreatedAt(result.getDate("fecha"));
                
                
                statementList.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (statementList.isEmpty()) {
            System.out.println("No hay datos para migrar en transfer_details.");
            return true;
        }

        // Insertar datos en la base de datos local
        try (Connection guardar = DbConnection.conectarseLocal();
             PreparedStatement stmtsave = guardar.prepareStatement(
                 "INSERT INTO suppliers_statement_account (id, supplier_id, invoice_number, invoice_amount, payment_amount, created_by, branch_id, status, created_at)\n"
                 + "VALUES (?,?,?,?,?,?,?,?,?)");
             Statement stmtUpdateSeq = guardar.createStatement()) {

            int batchSize = 100;
            int count = 0;
            int totalRecords = statementList.size();

            for (SupplierStatementAccount detail : statementList) {
                stmtsave.setInt(1, detail.getId());
                stmtsave.setInt(2, detail.getSupplierId());
                stmtsave.setString(3, detail.getInvoiceNumber());
                stmtsave.setDouble(4, detail.getInvoiceAmount());
                stmtsave.setDouble(5, detail.getPaymentAmount());
                stmtsave.setInt(6, detail.getCreatedBy());
                stmtsave.setInt(7, detail.getBranchId());
                stmtsave.setString(8, detail.getStatus());
                stmtsave.setDate(9, detail.getCreatedAt());

                stmtsave.addBatch();
                count++;

                if (count % batchSize == 0 || count == totalRecords) {
                    stmtsave.executeBatch();
                    System.out.printf("Progreso de migración: %.2f%%%n", (count / (double) totalRecords) * 100);
                }
            }

            // Actualizar la secuencia del ID en PostgreSQL
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('suppliers_statement_account', 'id'), COALESCE((SELECT MAX(id) FROM suppliers_statement_account), 1))");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        System.out.println("Migración de estados de cuenta proveedores completada.");
        return true;
    }

}
