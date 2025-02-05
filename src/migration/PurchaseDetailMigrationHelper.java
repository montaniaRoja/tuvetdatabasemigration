package migration;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.DbConnection;
import models.PurchaseDetail;

public class PurchaseDetailMigrationHelper {
	public static boolean purchasesMigration() {
        ArrayList<PurchaseDetail> purchaseDetailList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlPurchaseDetail = "select id, compra_id, prod_id, producto_cantidad,\n"
            		+ "producto_bonificacion, producto_cantidadtotal,\n"
            		+ "producto_costo, producto_porciva\n"
            		+ "from comprasdetalle;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlPurchaseDetail);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                PurchaseDetail purchase = new PurchaseDetail();
                purchase.setId(result.getInt("id"));                
                purchase.setPurchase_id(result.getInt("compra_id"));
                purchase.setProduct_id(result.getInt("prod_id"));
                purchase.setQuantity(result.getInt("producto_cantidad"));                
                purchase.setBonification(result.getInt("producto_bonificacion"));                
                purchase.setTotal(result.getInt("producto_cantidadtotal"));
                purchase.setUnit_cost(result.getDouble("producto_costo"));
                purchase.setTax_amount(result.getDouble("producto_porciva"));
                
                purchaseDetailList.add(purchase);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSavePurchase = "INSERT INTO detail_purchases (id, purchase_id, product_id, quantity,\n"
            		+"bonification, total, unit_cost, tax_amount)\n"
            		+"VALUES (?,?,?,?,?,?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSavePurchase);
            
            for (PurchaseDetail purchase : purchaseDetailList) {            	
                int purchaseDetailId = purchase.id;
                int purchaseId = purchase.purchase_id;
                int prodId = purchase.product_id;
                int qty = purchase.quantity;
                int bonus = purchase.bonification;
                int total = purchase.total;                
                Double costo = purchase.unit_cost;
                Double tax = purchase.tax_amount;
                                
                
                stmtsave.setInt(1, purchaseDetailId);
                stmtsave.setInt(2, purchaseId);
                stmtsave.setInt(3, prodId);
                stmtsave.setInt(4, qty);
                stmtsave.setInt(5, bonus);
                stmtsave.setInt(6, total);                
                stmtsave.setDouble(7, costo);
                stmtsave.setDouble(8, tax);
                
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                	System.out.println("detalle compra guardado exitosamente");
                    
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('detail_purchases', 'id'), (SELECT MAX(id) FROM detail_purchases))");
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
