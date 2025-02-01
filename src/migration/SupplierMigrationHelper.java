package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.DbConnection;
import models.Supplier;

public class SupplierMigrationHelper {
	public static boolean suppliersMigration() {
        ArrayList<Supplier> supplierList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlSuppliers = "SELECT \n"
            		+ "    id, \n"
            		+ "    prov_nombre, \n"
            		+ "    prov_nit, \n"
            		+ "    prov_nrc,\n"
            		+ "    prov_nombrecheque, \n"
            		+ "    (COALESCE(prov_ldireccion1, '') || ' ' || COALESCE(prov_ldireccion2, '')) AS direccion,\n"
            		+ "    prov_telefono, \n"
            		+ "    prov_correo, \n"
            		+ "    prov_nombrecontacto,\n"
            		+ "    activosn, \n"
            		+ "    fecha_creacion\n"
            		+ "FROM proveedores;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlSuppliers);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Supplier supplier = new Supplier();
                supplier.setId(result.getInt("id"));
                supplier.setName(result.getString("prov_nombre"));
                supplier.setEmail(result.getString("prov_correo"));
                supplier.setNit(result.getString("prov_nit"));
                supplier.setNrc(result.getString("prov_nrc"));
                supplier.setCreated_at(result.getDate("fecha_creacion"));
                supplier.setPayee_name(result.getString("prov_nombrecheque"));
                supplier.setContact_name(result.getString("prov_nombrecontacto"));
                supplier.setAddress(result.getString("direccion"));
                supplier.setCreated_by_id(1);
                supplier.setIsactive(result.getBoolean("activosn"));
                supplier.setPhone(result.getString("prov_telefono"));
                supplierList.add(supplier);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSavesupplier = "INSERT INTO suppliers (id, name, nit, nrc, payee_name, address, phone,email, contact_name, created_by_id, is_active, created_at )\n"
            		+"VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSavesupplier);
            
            for (Supplier supplier : supplierList) {
            	
                int supplierId = supplier.id;
                String supplierName = supplier.name;
                String supplierNit=supplier.nit;
                String supplierNrc=supplier.nrc;
                String supplierPayee=supplier.payee_name;
                String supplierAddress=supplier.address;
                String supplierPhone=supplier.phone;
                String supplierEmail = supplier.email;
                String supplierContact=supplier.contact_name;
                int supplierCreatedBy = supplier.created_by_id;
                boolean supplierIsActive=supplier.isactive;
                Date supplierCreatedAt = supplier.created_at;
                
                
                stmtsave.setInt(1, supplierId);
                stmtsave.setString(2, supplierName);
                stmtsave.setString(3, supplierNit);
                stmtsave.setString(4, supplierNrc);
                stmtsave.setString(5, supplierPayee);
                stmtsave.setString(6, supplierAddress);
                stmtsave.setString(7, supplierPhone);
                stmtsave.setString(8, supplierEmail);
                stmtsave.setString(9, supplierContact);
                stmtsave.setInt(10, supplierCreatedBy);
                stmtsave.setBoolean(11, supplierIsActive);
                stmtsave.setDate(12, supplierCreatedAt);
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                    System.out.println("proveedor guardado exitosamente");
                    
                }
            }
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('suppliers', 'id'), (SELECT MAX(id) FROM suppliers))");
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