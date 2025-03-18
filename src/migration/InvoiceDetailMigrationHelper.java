package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.DbConnection;
import models.InvoiceDetail;

public class InvoiceDetailMigrationHelper {
	public static boolean invoiceMigration() {
        ArrayList<InvoiceDetail> invoiceList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlInvoices = "select id, id_hfactura, prod_id, sucursal_id, cantidad, monto_costo, precio, subtotal,Iva, \n"
            		+ "monto_descuento, total_linea, validado_sn, anulada_sn, fecha\n"
            		+ "from dfacturas where fecha < CURRENT_DATE;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlInvoices);
            ResultSet result = stmt.executeQuery();
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
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSaveInvoice = "INSERT INTO invoice_details \n"
                    + "(id, invoice_id, product_id, branch_id, quantity, total_cost,\n"
                    + "unit_price, subtotal, tax_amount, discount_amount, line_total, stock_updated,\n"
                    + "is_anulled, created_at, updated_at )\n"                    
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

            PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveInvoice);
            
            
            double records=invoiceList.size();
            double contador=0;
            double percent=0;

            for (InvoiceDetail invoice : invoiceList) {            
                int id = invoice.id;
                int invoiceId = invoice.invoiceId;
                int prodId = invoice.productId;
                int branchId = invoice.branchId;
                int qty = invoice.quantity;                
                double unitcost = invoice.unitCost;                
                double unitPrice = invoice.unitPrice;  // Puede ser nulo
                double subtotal = invoice.subtotal;
                double taxAmount = invoice.taxAmount;
                double discountAmount = invoice.discountAmount;
                double lineTotal = invoice.lineTotal;
                
                boolean stockUpdated = invoice.stockUpdated;
                boolean isAnulled = invoice.isAnulled;
                
                Date createdAt = invoice.createdAt;
                Date updatedAt = invoice.updatedAt;
                

                stmtsave.setInt(1, id);
                stmtsave.setInt(2, invoiceId);
                stmtsave.setInt(3, prodId);
                stmtsave.setInt(4, branchId);
                stmtsave.setInt(5, qty);
                stmtsave.setDouble(6, unitcost);
                stmtsave.setDouble(7, unitPrice);
                stmtsave.setDouble(8, subtotal);                
                stmtsave.setDouble(9, taxAmount);
                stmtsave.setDouble(10, discountAmount);
                stmtsave.setDouble(11, lineTotal);
                
                stmtsave.setBoolean(12, stockUpdated);
                stmtsave.setBoolean(13, isAnulled);

                stmtsave.setDate(14, createdAt);
                stmtsave.setDate(15, updatedAt);


                contador+=1;
                percent = contador/records*100;
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                	
                   System.out.println("Porcentaje de Avance Facturas "+percent+"%");
                    
                }
            }

            
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('invoice_details', 'id'), (SELECT MAX(id) FROM invoice_details))");
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
