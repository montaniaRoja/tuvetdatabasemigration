package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Purchase;
import models.DbConnection;

public class PurchaseMigrationHelper {
	public static boolean purchasesMigration() {
        ArrayList<Purchase> purchaseList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlPurchases = "select id, proveedor_id, compra_nodoc, compra_condicion, compra_subtotal,\n"
            		+ "compra_montoiva, compra_montoretencion, compra_total,sucursal_id,compra_fecha\n"
            		+ "from comprasproductos;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlPurchases);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Purchase purchase = new Purchase();
                purchase.setId(result.getInt("id"));
                purchase.setSupplier_id(result.getInt("proveedor_id"));
                purchase.setInvoice_number(result.getString("compra_nodoc"));
                purchase.setPayment_condition(result.getString("compra_condicion"));                
                purchase.setSubtotal_amount(result.getDouble("compra_subtotal"));                
                purchase.setTax_amount(result.getDouble("compra_montoiva"));
                purchase.setRetention_amount(result.getDouble("compra_montoretencion"));
                purchase.setTotal(result.getDouble("compra_total"));
                purchase.setBranch_id(result.getInt("sucursal_id"));
                purchase.setCreated_by(1);
                purchase.setCreated_at(result.getDate("compra_fecha"));
                purchaseList.add(purchase);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSavePurchase = "INSERT INTO purchases (id, supplier_id, invoice_number, payment_condition,\n"
            		+"subtotal_amount, tax_amount, retention_amount, total, branch_id, created_by,created_at )\n"
            		+"VALUES (?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSavePurchase);
            
            double records=purchaseList.size();
            double contador=0;
            double percent=0;
            
            for (Purchase purchase : purchaseList) {            	
                int purchaseId = purchase.id;
                int supplierId = purchase.supplier_id;
                String invoiceNumber = purchase.invoice_number;
                String paymentCondition = purchase.payment_condition;
                Double subTotal = purchase.subtotal_amount;
                Double tax = purchase.tax_amount;
                Double retention = purchase.retention_amount;
                Double total = purchase.total;                
                int branch = purchase.branch_id;
                int createdBy = purchase.created_by;                
                Date brandCreatedAt = purchase.created_at;                
                
                stmtsave.setInt(1, purchaseId);
                stmtsave.setInt(2, supplierId);
                stmtsave.setString(3, invoiceNumber);
                stmtsave.setString(4, paymentCondition);
                stmtsave.setDouble(5, subTotal);
                stmtsave.setDouble(6, tax);
                stmtsave.setDouble(7, retention);
                stmtsave.setDouble(8, total);
                stmtsave.setInt(9, branch);
                stmtsave.setInt(10, createdBy);                
                stmtsave.setDate(11, brandCreatedAt);
                
                int rows = stmtsave.executeUpdate();
                
                contador+=1;
                percent = contador/records*100;
                
                if (rows > 0) {
                	
                   System.out.println("Porcentaje de Avance compras "+percent+"%");                    
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('purchases', 'id'), (SELECT MAX(id) FROM purchases))");
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
