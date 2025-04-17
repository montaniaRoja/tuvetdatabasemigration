package migration;

import java.sql.*;
import java.util.ArrayList;
import models.DbConnection;
import models.InvoiceDetail;

public class InvoiceDetailMigrationHelper {
    public static boolean invoiceMigration() {
        ArrayList<InvoiceDetail> invoiceList = new ArrayList<>();
        String sqlInvoices = """
            SELECT id, id_hfactura, prod_id, sucursal_id, cantidad, monto_costo, precio, subtotal, Iva,
            monto_descuento, total_linea, validado_sn, anulada_sn, fecha
            FROM dfacturas;
        """;

        try (
            Connection remoteConn = DbConnection.conectarseRemoto();
            PreparedStatement stmt = remoteConn.prepareStatement(sqlInvoices);
            ResultSet result = stmt.executeQuery()
        ) {
            while (result.next()) {
                InvoiceDetail invoice = new InvoiceDetail();
                invoice.setId(result.getInt("id"));
                invoice.setInvoiceId(result.getInt("id_hfactura"));
                invoice.setProductId(result.getInt("prod_id"));
                invoice.setBranchId(result.getInt("sucursal_id"));
                invoice.setQuantity(result.getInt("cantidad"));
                invoice.setUnitCost(result.getDouble("monto_costo"));
                invoice.setUnitPrice(result.getDouble("precio"));
                invoice.setSubtotal(result.getDouble("subtotal"));
                invoice.setTaxAmount(result.getDouble("Iva"));
                invoice.setDiscountAmount(result.getDouble("monto_descuento"));
                invoice.setLineTotal(result.getDouble("total_linea"));
                invoice.setStockUpdated(result.getBoolean("validado_sn"));
                invoice.setAnulled(result.getBoolean("anulada_sn"));
                invoice.setCreatedAt(result.getDate("fecha"));
                invoice.setUpdatedAt(result.getDate("fecha"));

                invoiceList.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (invoiceList.isEmpty()) {
            System.out.println("No hay facturas para migrar.");
            return true;
        }

        String sqlInsert = """
            INSERT INTO invoice_details (id, invoice_id, product_id, branch_id, quantity, total_cost,
            unit_price, subtotal, tax_amount, discount_amount, line_total, stock_updated,
            is_anulled, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (
            Connection localConn = DbConnection.conectarseLocal();
            PreparedStatement stmtInsert = localConn.prepareStatement(sqlInsert)
        ) {
            localConn.setAutoCommit(false);  // 🔥 Desactiva autocommit para mejorar rendimiento

            int batchSize = 500;
            int count = 0;
            int totalRecords = invoiceList.size();

            for (InvoiceDetail invoice : invoiceList) {
                stmtInsert.setInt(1, invoice.getId());
                stmtInsert.setInt(2, invoice.getInvoiceId());
                stmtInsert.setInt(3, invoice.getProductId());
                stmtInsert.setInt(4, invoice.getBranchId());
                stmtInsert.setInt(5, invoice.getQuantity());
                stmtInsert.setDouble(6, invoice.getUnitCost());
                stmtInsert.setObject(7, invoice.getUnitPrice() != 0 ? invoice.getUnitPrice() : null, Types.DOUBLE);  // Manejo de NULL
                stmtInsert.setDouble(8, invoice.getSubtotal());
                stmtInsert.setDouble(9, invoice.getTaxAmount());
                stmtInsert.setDouble(10, invoice.getDiscountAmount());
                stmtInsert.setDouble(11, invoice.getLineTotal());
                stmtInsert.setBoolean(12, invoice.isStockUpdated());
                stmtInsert.setBoolean(13, invoice.isAnulled());
                stmtInsert.setDate(14, invoice.getCreatedAt());
                stmtInsert.setDate(15, invoice.getUpdatedAt());
                stmtInsert.addBatch();

                if (++count % batchSize == 0) {
                    stmtInsert.executeBatch();
                    System.out.printf("Porcentaje de avance Facturas: %.2f%%%n", (count / (double) totalRecords) * 100);
                }
            }
            stmtInsert.executeBatch();
            localConn.commit();

            // 🔥 Actualiza la secuencia de IDs en PostgreSQL
            try (Statement stmtUpdate = localConn.createStatement()) {
                stmtUpdate.execute("SELECT setval(pg_get_serial_sequence('invoice_details', 'id'), (SELECT MAX(id) FROM invoice_details))");
            }

            System.out.println("Migración de facturas completada exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
