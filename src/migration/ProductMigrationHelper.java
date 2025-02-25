package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;

import models.Product;
import models.DbConnection;

public class ProductMigrationHelper {
	public static boolean productsMigration() {
        ArrayList<Product> productList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlProducts = "SELECT \n"
            		+ "    p.id as prodid, \n"
            		+ "    prod_codbarra, \n"
            		+ "    prod_nombre, \n"
            		+ "    id_marca, \n"
            		+ "    id_categorias, \n"
            		+ "    prod_compraminima, \n"
            		+ "    prod_distminima,\n"
            		+ "    codigo_impuesto, \n"
            		+ "    escalas, \n"
            		+ "    prod_costopromedio, \n"
            		+ "    prod_markup, \n"
            		+ "    prod_preciopublico, \n"
            		+ "    prod_porcdescuento,\n"
            		+ "    descuento_unidad, \n"
            		+ "    prod_esinventariosn, \n"
            		+ "    prod_ultimocosto, \n"
            		+ "    activosn, \n"
            		+ "    cronico_sn, \n"
            		+ "    prod_ventaminima,\n"
            		+ "    bodegasn, \n"
            		+ "    unidad_fraccion, \n"
            		+ "    concentracion, \n"
            		+ "    tipo_alimento, \n"
            		+ "    contenido, \n"
            		+ "    prov_descuento, \n"
            		+ "    precio_competencia, \n"
            		+ "    fecha_creacion,\n"
            		+ "    SUM(e.existencia) AS existencia\n"
            		+ "FROM \n"
            		+ "    productos p\n"
            		+ "JOIN \n"
            		+ "    existencias e\n"
            		+ "ON \n"
            		+ "    e.codbarra = prod_codbarra\n"
            		+ "WHERE \n"
            		+ "    prod_nombre IS NOT NULL\n"
            		+ "GROUP BY \n"
            		+ "    p.id\n"
            		+ "ORDER BY \n"
            		+ "    p.prod_nombre;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlProducts);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Product product = new Product();
                product.setId(result.getInt("prodid"));
                product.setBarcode(result.getString("prod_codbarra"));
                product.setName(result.getString("prod_nombre"));
                product.setBrand_id(result.getInt("id_marca"));
                product.setCategory_id(result.getInt("id_categorias"));
                product.setMin_purchase(result.getInt("prod_compraminima"));
                product.setMin_dist(result.getInt("prod_distminima"));                
                product.setTax_id(result.getInt("codigo_impuesto"));                
                product.setScales(result.getString("escalas"));
                product.setAvg_cost(result.getFloat("prod_costopromedio"));
                product.setMarkup(result.getFloat("prod_markup"));
                product.setCustomer_price(result.getFloat("prod_preciopublico"));
                product.setDiscount_percent(result.getFloat("prod_porcdescuento"));
                product.setUnit_discount(result.getFloat("descuento_unidad"));
                product.setCreated_by(1);
                product.setIs_inventory(result.getBoolean("prod_esinventariosn"));
                product.setLast_cost(result.getFloat("prod_ultimocosto"));
                product.setIs_active(result.getBoolean("activosn"));
                product.setIs_chronical(result.getBoolean("cronico_sn"));
                product.setMin_sale(result.getInt("prod_ventaminima"));
                product.setFrom_store(result.getBoolean("bodegasn"));
                product.setFraction_unit(result.getInt("unidad_fraccion"));
                product.setConcentration(result.getFloat("concentracion"));
                product.setFood_type(result.getString("tipo_alimento"));
                product.setMeasure_content(result.getString("contenido"));
                product.setSupplier_discount(result.getFloat("prov_descuento"));
                product.setCompetitor_price(result.getFloat("precio_competencia"));
                product.setCreated_at(result.getDate("fecha_creacion"));
                product.setInventory_sum(result.getInt("existencia"));
                
                productList.add(product);
            }
            
         // Conexión única
            Connection guardar = DbConnection.conectarseLocal();

            String sqlSaveProds = "INSERT INTO products (id, barcode, name, brand_id, category_id, min_purchase, min_dist, "
                    + "tax_id, scales, avg_cost, markup, customer_price, discount_percent, unit_discount, "
                    + "created_by, is_inventory, last_cost, is_active, is_chronical, min_sale, from_store, "
                    + "fraction_unit, concentration, food_type, measure_content, supplier_discount, competitor_price, created_at, inventory_sum) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

            // Preparamos el statement una sola vez
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveProds);

            double records=productList.size();
            double contador=0;
            double percent=0;
            
            
            for (Product product : productList) {     
                

                stmtsave.setInt(1, product.id);
                stmtsave.setString(2, product.barcode);
                stmtsave.setString(3, product.name);
                stmtsave.setInt(4, product.brand_id);
                stmtsave.setInt(5, product.category_id);
                stmtsave.setInt(6, product.min_purchase);
                stmtsave.setInt(7, product.min_dist);
                stmtsave.setInt(8, product.tax_id);
                stmtsave.setString(9, product.scales);

                // Convertimos a BigDecimal con 2 decimales
                stmtsave.setBigDecimal(10, BigDecimal.valueOf(product.avg_cost).setScale(2, RoundingMode.HALF_UP));
                stmtsave.setBigDecimal(11, BigDecimal.valueOf(product.markup).setScale(2, RoundingMode.HALF_UP));
                stmtsave.setBigDecimal(12, BigDecimal.valueOf(product.customer_price).setScale(2, RoundingMode.HALF_UP));
                stmtsave.setBigDecimal(13, BigDecimal.valueOf(product.discount_percent).setScale(2, RoundingMode.HALF_UP));
                stmtsave.setBigDecimal(14, BigDecimal.valueOf(product.unit_discount).setScale(2, RoundingMode.HALF_UP));

                stmtsave.setInt(15, product.created_by);
                stmtsave.setBoolean(16, product.is_inventory);
                stmtsave.setBigDecimal(17, BigDecimal.valueOf(product.last_cost).setScale(2, RoundingMode.HALF_UP));
                stmtsave.setBoolean(18, product.is_active);
                stmtsave.setBoolean(19, product.is_chronical);
                stmtsave.setInt(20, product.min_sale);
                stmtsave.setBoolean(21, product.from_store);
                stmtsave.setInt(22, product.fraction_unit);
                stmtsave.setBigDecimal(23, BigDecimal.valueOf(product.concentration).setScale(2, RoundingMode.HALF_UP));
                stmtsave.setString(24, product.food_type);
                stmtsave.setString(25, product.measure_content);
                stmtsave.setBigDecimal(26, BigDecimal.valueOf(product.supplier_discount).setScale(2, RoundingMode.HALF_UP));
                stmtsave.setBigDecimal(27, BigDecimal.valueOf(product.competitor_price).setScale(2, RoundingMode.HALF_UP));

                // Convertimos java.util.Date a java.sql.Date
                Date createdAt = product.created_at;
                stmtsave.setDate(28, createdAt);
                
                stmtsave.setInt(29, product.inventory_sum);

                int rows = stmtsave.executeUpdate();
                
                contador+=1;
                percent = contador/records*100;              
               
                
                if (rows > 0) {
                	
                   System.out.println("Porcentaje de Avance Productos "+percent+"%");
                    
                }
                
            }

            // 🔹 Solo actualizamos la secuencia UNA VEZ después de importar todos los productos
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('products', 'id'), (SELECT MAX(id) FROM products))");
            stmtUpdateSeq.close();

            // Cerramos recursos
            stmtsave.close();
            guardar.close();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

}