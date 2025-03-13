package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;

import models.Invoice;
import models.DbConnection;

public class InvoiceMigrationHelper {
	public static boolean invoiceMigration() {
        ArrayList<Invoice> invoiceList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlInvoices = "SELECT id, numero_impreso, id_sucursal, id_cliente, createdby_id, updatedby_id, anulledby_id, \n"
            		+ "       subtotal, monto_gravado, impuesto, monto_nogravado, total_linea, descuentos, costo, \n"
            		+ "       pago_efectivo, pago_tarjeta, pago_credito, pago_puntos, numero_autorizacion, \n"
            		+ "       pagada_sn, anulada, fecha_anulacion, condicion_pago, hora_inicio, hora_final, \n"
            		+ "       numero_pedido, fecha_creacion, fecha_cobro\n"
            		+ "FROM hfacturas\n"
            		+ "WHERE \n"
            		+ "pagada_sn=1 -- Solo facturas hasta ayer\n"
            		+ "ORDER BY id;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlInvoices);
            ResultSet result = stmt.executeQuery();
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
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSaveInvoice = "INSERT INTO invoices \n"
                    + "(id, invoice_number, customer_id, branch_id, created_by, processed_by,\n"
                    + "anulled_by, sub_total, taxed_amount, tax_amount, untaxed_amount, total,\n"
                    + "discount_amount, sale_cost, cash_paid, card_paid, credit_paid, points_paid,\n"
                    + "authorization_number, already_paid, is_anulled, anullation_date, payment_condition, start_time,\n"
                    + "end_time, order_number, created_at, updated_at) \n"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

            PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveInvoice);
            
            double records=invoiceList.size();
            double contador=0;
            double percent=0;

            for (Invoice invoice : invoiceList) {            
                int invoiceId = invoice.id;
                String invoiceNo = invoice.invoiceNumber;
                int customerId = invoice.customerId;
                int branchId = invoice.branchId;
                int createdBy = invoice.createdBy;
                int processedBy = invoice.processedBy;                
                Integer anulledBy = invoice.anulledBy;  // Puede ser nulo
                double subtotal = invoice.subTotal;
                double taxedAmount = invoice.taxedAmount;
                double taxAmount = invoice.taxAmount;
                double untaxedAmount = invoice.untaxedAmount;
                double total = invoice.total;
                double discountAmount = invoice.discountAmount;
                double saleCost = invoice.saleCost;
                double cash = invoice.cashPaid;
                double card = invoice.cardPaid;
                double credit = invoice.creditPaid;
                double points = invoice.pointsPaid;
                String authorizationNumber = invoice.authorizationNumber;
                boolean alreadyPaid = invoice.alreadyPaid;
                boolean isAnulled = invoice.isAnulled;
                Date anullationDate = invoice.anullationDate;
                String paymentCondition = invoice.paymentCondition;
                Time startTime = invoice.startTime;
                Time endTime = invoice.endTime;
                String orderNumber = invoice.orderNumber;
                Date createdAt = invoice.createdAt;
                Date updatedAt = invoice.updatedAt;

                stmtsave.setInt(1, invoiceId);
                stmtsave.setString(2, invoiceNo);
                stmtsave.setInt(3, customerId);
                stmtsave.setInt(4, branchId);
                stmtsave.setInt(5, createdBy);
                stmtsave.setInt(6, processedBy);
                
               
                    stmtsave.setInt(7, anulledBy);
               

                stmtsave.setDouble(8, subtotal);
                stmtsave.setDouble(9, taxedAmount);
                stmtsave.setDouble(10, taxAmount);
                stmtsave.setDouble(11, untaxedAmount);
                stmtsave.setDouble(12, total);
                stmtsave.setDouble(13, discountAmount);
                stmtsave.setDouble(14, saleCost);
                stmtsave.setDouble(15, cash);
                stmtsave.setDouble(16, card);
                stmtsave.setDouble(17, credit);
                stmtsave.setDouble(18, points);
                stmtsave.setString(19, authorizationNumber);
                stmtsave.setBoolean(20, alreadyPaid);
                stmtsave.setBoolean(21, isAnulled);

                
                    stmtsave.setDate(22, anullationDate);
                

                stmtsave.setString(23, paymentCondition);
                stmtsave.setTime(24, startTime);
                stmtsave.setTime(25, endTime);
                stmtsave.setString(26, orderNumber);
                stmtsave.setDate(27, createdAt);
                stmtsave.setDate(28, updatedAt);

                contador+=1;
                percent = contador/records*100;
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                	
                   System.out.println("Porcentaje de Avance Invoices "+percent+"%");
                    
                }
            }

            
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('invoices', 'id'), (SELECT MAX(id) FROM invoices))");
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
