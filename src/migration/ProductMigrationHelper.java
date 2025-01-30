package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            int productCounter=0;
            
            for (Product product : productList) {     
                productCounter += 1;
                int prodId = product.id;
                String prodCode = product.barcode;
                String prodName = product.name;
                int brandId = product.brand_id;
                int catId = product.category_id;
                int minPurchase = product.min_purchase;
                int minDist = product.min_dist;
                int taxId = product.tax_id;
                String scales = product.scales;

                // Convertimos a BigDecimal y forzamos 2 decimales
                BigDecimal avgCost = BigDecimal.valueOf(product.avg_cost).setScale(2, RoundingMode.HALF_UP);
                BigDecimal markup = BigDecimal.valueOf(product.markup).setScale(2, RoundingMode.HALF_UP);
                BigDecimal price = BigDecimal.valueOf(product.customer_price).setScale(2, RoundingMode.HALF_UP);
                BigDecimal discountPercent = BigDecimal.valueOf(product.discount_percent).setScale(2, RoundingMode.HALF_UP);
                BigDecimal unitDiscount = BigDecimal.valueOf(product.unit_discount).setScale(2, RoundingMode.HALF_UP);
                BigDecimal lastCost = BigDecimal.valueOf(product.last_cost).setScale(2, RoundingMode.HALF_UP);
                BigDecimal concentration = BigDecimal.valueOf(product.concentration).setScale(2, RoundingMode.HALF_UP);
                BigDecimal supplierDiscount = BigDecimal.valueOf(product.supplier_discount).setScale(2, RoundingMode.HALF_UP);
                BigDecimal competitorPrice = BigDecimal.valueOf(product.competitor_price).setScale(2, RoundingMode.HALF_UP);

                int prodCreatedBy = product.created_by;
                boolean isInventory = product.is_inventory;
                boolean isActive = product.is_active;
                boolean isChronical = product.is_chronical;
                int minSale = product.min_sale;
                boolean fromStore = product.from_store;
                int fractionUnit = product.fraction_unit;
                String foodType = product.food_type;
                String measureContent = product.measure_content;
                Date createdAt = product.created_at;
                int inventory = product.inventory_sum;
                
                // Conexión y consulta SQL
                Connection guardar = DbConnection.conectarseLocal();
                String sqlSaveProds = "INSERT INTO products (id, barcode, name, brand_id, category_id, min_purchase, min_dist, "
                        + "tax_id, scales, avg_cost, markup, customer_price, discount_percent, unit_discount, "
                        + "created_by, is_inventory, last_cost, is_active, is_chronical, min_sale, from_store, "
                        + "fraction_unit, concentration, food_type, measure_content, supplier_discount, competitor_price, created_at, inventory_sum) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                
                PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveProds);
                stmtsave.setInt(1, prodId);
                stmtsave.setString(2, prodCode);
                stmtsave.setString(3, prodName);
                stmtsave.setInt(4, brandId);
                stmtsave.setInt(5, catId);
                stmtsave.setInt(6, minPurchase);
                stmtsave.setInt(7, minDist);
                stmtsave.setInt(8, taxId);
                stmtsave.setString(9, scales);
                stmtsave.setBigDecimal(10, avgCost);
                stmtsave.setBigDecimal(11, markup);
                stmtsave.setBigDecimal(12, price);
                stmtsave.setBigDecimal(13, discountPercent);
                stmtsave.setBigDecimal(14, unitDiscount);
                stmtsave.setInt(15, prodCreatedBy);
                stmtsave.setBoolean(16, isInventory);
                stmtsave.setBigDecimal(17, lastCost);
                stmtsave.setBoolean(18, isActive);
                stmtsave.setBoolean(19, isChronical);
                stmtsave.setInt(20, minSale);
                stmtsave.setBoolean(21, fromStore);
                stmtsave.setInt(22, fractionUnit);
                stmtsave.setBigDecimal(23, concentration);
                stmtsave.setString(24, foodType);
                stmtsave.setString(25, measureContent);
                stmtsave.setBigDecimal(26, supplierDiscount);
                stmtsave.setBigDecimal(27, competitorPrice);
                stmtsave.setDate(28, createdAt);
                stmtsave.setInt(29, inventory);
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                    System.out.println("Producto guardado exitosamente " + productCounter);
                }
                
                guardar.close();
            
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

}