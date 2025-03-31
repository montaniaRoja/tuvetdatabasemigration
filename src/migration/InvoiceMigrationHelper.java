package migration;

import java.sql.*;
import java.util.ArrayList;
import models.Invoice;
import models.DbConnection;

public class InvoiceMigrationHelper {
    private static final int BATCH_SIZE = 1000; // Ajustable según rendimiento

    public static boolean invoiceMigration() {
        ArrayList<Invoice> invoiceList = new ArrayList<>();

        try (Connection remoteConn = DbConnection.conectarseRemoto();
             Connection localConn = DbConnection.conectarseLocal()) {

            // 1. Leer las facturas de la base de datos remota
            String sqlInvoices = """
                SELECT id, numero_impreso, id_sucursal, id_cliente, createdby_id, updatedby_id, anulledby_id,
                       subtotal, monto_gravado, impuesto, monto_nogravado, total_linea, descuentos, costo,
                       pago_efectivo, pago_tarjeta, pago_credito, pago_puntos, numero_autorizacion,
                       pagada_sn, anulada, fecha_anulacion, condicion_pago, hora_inicio, hora_final,
                       numero_pedido, fecha_creacion, fecha_cobro
                FROM hfacturas
                WHERE pagada_sn = 1
                ORDER BY id;
                """;

            try (PreparedStatement stmt = remoteConn.prepareStatement(sqlInvoices);
                 ResultSet result = stmt.executeQuery()) {

                while (result.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setId(result.getInt("id"));
                    invoice.setInvoiceNumber(result.getString("numero_impreso"));
                    invoice.setBranchId(result.getInt("id_sucursal"));
                    invoice.setCustomerId(result.getInt("id_cliente"));
                    invoice.setCreatedBy(result.getInt("createdby_id"));
                    invoice.setProcessedBy(result.getInt("updatedby_id"));
                    invoice.setAnulledBy(result.getInt("anulledby_id"));
                    invoice.setSubTotal(result.getDouble("subtotal"));
                    invoice.setTaxedAmount(result.getDouble("monto_gravado"));
                    invoice.setTaxAmount(result.getDouble("impuesto"));
                    invoice.setUntaxedAmount(result.getDouble("monto_nogravado"));
                    invoice.setTotal(result.getDouble("total_linea"));
                    invoice.setDiscountAmount(result.getDouble("descuentos"));
                    invoice.setSaleCost(result.getDouble("costo"));
                    invoice.setCashPaid(result.getDouble("pago_efectivo"));
                    invoice.setCardPaid(result.getDouble("pago_tarjeta"));
                    invoice.setCreditPaid(result.getDouble("pago_credito"));
                    invoice.setPointsPaid(result.getDouble("pago_puntos"));
                    invoice.setAuthorizationNumber(result.getString("numero_autorizacion"));
                    invoice.setAlreadyPaid(result.getBoolean("pagada_sn"));
                    invoice.setAnulled(result.getBoolean("anulada"));
                    invoice.setAnullationDate(result.getDate("fecha_anulacion"));
                    invoice.setPaymentCondition(result.getString("condicion_pago"));
                    invoice.setStartTime(result.getTime("hora_inicio"));
                    invoice.setEndTime(result.getTime("hora_final"));
                    invoice.setOrderNumber(result.getString("numero_pedido"));
                    invoice.setCreatedAt(result.getDate("fecha_creacion"));
                    invoice.setUpdatedAt(result.getDate("fecha_cobro"));

                    invoiceList.add(invoice);
                }
            }

            // 2. Insertar las facturas en la base de datos local usando Batch
            String sqlInsert = """
                INSERT INTO invoices 
                (id, invoice_number, customer_id, branch_id, created_by, processed_by, 
                 anulled_by, sub_total, taxed_amount, tax_amount, untaxed_amount, total, 
                 discount_amount, sale_cost, cash_paid, card_paid, credit_paid, points_paid, 
                 authorization_number, already_paid, is_anulled, anullation_date, payment_condition, 
                 start_time, end_time, order_number, created_at, updated_at)
                VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                """;

            localConn.setAutoCommit(false); // Comenzar transacción
            try (PreparedStatement insertStmt = localConn.prepareStatement(sqlInsert)) {
                int count = 0;
                for (Invoice invoice : invoiceList) {
                    insertStmt.setInt(1, invoice.getId());
                    insertStmt.setString(2, invoice.getInvoiceNumber());
                    insertStmt.setInt(3, invoice.getCustomerId());
                    insertStmt.setInt(4, invoice.getBranchId());
                    insertStmt.setInt(5, invoice.getCreatedBy());
                    insertStmt.setInt(6, invoice.getProcessedBy());
                    insertStmt.setObject(7, invoice.getAnulledBy(), Types.INTEGER); // Manejar nulos
                    insertStmt.setDouble(8, invoice.getSubTotal());
                    insertStmt.setDouble(9, invoice.getTaxedAmount());
                    insertStmt.setDouble(10, invoice.getTaxAmount());
                    insertStmt.setDouble(11, invoice.getUntaxedAmount());
                    insertStmt.setDouble(12, invoice.getTotal());
                    insertStmt.setDouble(13, invoice.getDiscountAmount());
                    insertStmt.setDouble(14, invoice.getSaleCost());
                    insertStmt.setDouble(15, invoice.getCashPaid());
                    insertStmt.setDouble(16, invoice.getCardPaid());
                    insertStmt.setDouble(17, invoice.getCreditPaid());
                    insertStmt.setDouble(18, invoice.getPointsPaid());
                    insertStmt.setString(19, invoice.getAuthorizationNumber());
                    insertStmt.setBoolean(20, invoice.isAlreadyPaid());
                    insertStmt.setBoolean(21, invoice.isAnulled());
                    insertStmt.setObject(22, invoice.getAnullationDate(), Types.DATE); // Manejar nulos
                    insertStmt.setString(23, invoice.getPaymentCondition());
                    insertStmt.setTime(24, invoice.getStartTime());
                    insertStmt.setTime(25, invoice.getEndTime());
                    insertStmt.setString(26, invoice.getOrderNumber());
                    insertStmt.setDate(27, invoice.getCreatedAt());
                    insertStmt.setDate(28, invoice.getUpdatedAt());

                    insertStmt.addBatch(); // Agregar al batch
                    count++;

                    if (count % BATCH_SIZE == 0) {
                        insertStmt.executeBatch(); // Ejecutar batch cada X registros
                        localConn.commit(); // Confirmar cambios
                        System.out.println("Procesados: " + count + " registros...");
                    }
                }

                insertStmt.executeBatch(); // Insertar los registros restantes
                localConn.commit(); // Confirmar transacción final
                System.out.println("Migración completada. Total: " + count + " registros.");
            } catch (SQLException e) {
                localConn.rollback(); // Revertir cambios en caso de error
                throw e;
            } finally {
                localConn.setAutoCommit(true); // Restaurar estado de la conexión
            }

            // 3. Actualizar secuencia de IDs en la base de datos local
            try (Statement stmtUpdateSeq = localConn.createStatement()) {
                stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('invoices', 'id'), (SELECT MAX(id) FROM invoices))");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
