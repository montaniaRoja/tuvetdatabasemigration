package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Customer;
import models.DbConnection;

public class CustomerMigrationHelper {
	public static boolean customerMigration() {
        ArrayList<Customer> customerList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlCustomers = "select id, cliente_nombre, doc_type, cliente_nodoc, birth_date, esempresasn, cliente_giro,\n"
            		+ "concat(cliente_dir1,' ', cliente_dir2) as direccion, cliente_tel, cliente_correo, darcreditosn,\n"
            		+ "creadopor_id, gana_puntos, suc_id \n"
            		+ "from clientes;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlCustomers);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Customer customer = new Customer();
                customer.setId(result.getInt("id"));
                customer.setName(result.getString("cliente_nombre"));
                customer.setDocType(result.getInt("doc_type"));
                customer.setDocNumber(result.getString("cliente_nodoc"));
                customer.setBirthDate(result.getDate("birth_date"));
                customer.setCompany(result.getBoolean("esempresasn"));
                customer.setBussinesType(result.getString("cliente_giro"));
                customer.setAddress(result.getString("direccion"));
                customer.setPhone(result.getString("cliente_tel"));
                customer.setEmail(result.getString("cliente_correo"));
                customer.setHasCredit(result.getBoolean("darcreditosn"));
                customer.setCreatedBy(result.getInt("creadopor_id"));
                customer.setEarnPoints(result.getBoolean("gana_puntos"));
                customer.setCreationBranch(result.getInt("suc_id"));
                         
                customerList.add(customer);
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSavebrand = "INSERT INTO customers (id, name, doc_type, doc_number, birth_date, is_company, bussines_type, address, phone, email, has_credit, created_by, earn_points, creation_branch, created_at) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSavebrand);

            for (Customer customer : customerList) {            	
                int customerId = customer.id;
                String customerName = customer.name;
                int docType = customer.docType;
                String docNumber = customer.docNumber;
                Date birthDate=customer.birthDate;
                boolean isCompany=customer.isCompany;
                String bussinesType=customer.bussinesType;
                String address=customer.address;
                String phone=customer.phone;
                String email=customer.email;
                boolean hasCredit=customer.hasCredit;
                int createdBy=customer.createdBy;
                boolean earnPoints=customer.earnPoints;
                int branch=customer.creationBranch;
                Date createdAt=customer.createdAt;
                
                
                stmtsave.setInt(1, customerId);
                stmtsave.setString(2, customerName);
                stmtsave.setInt(3, docType);
                stmtsave.setString(4, docNumber);
                stmtsave.setDate(5, birthDate);
                stmtsave.setBoolean(6, isCompany);
                stmtsave.setString(7, bussinesType);
                stmtsave.setString(8, address);
                stmtsave.setString(9, phone);
                stmtsave.setString(10, email);
                stmtsave.setBoolean(11, hasCredit);
                stmtsave.setInt(12, createdBy);
                stmtsave.setBoolean(13, earnPoints);
                stmtsave.setInt(14, branch);
                stmtsave.setDate(15, createdAt);
                
                
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                    
                    System.out.println("cliente MIGRADA EXITOSAMENTE");
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('customers', 'id'), (SELECT MAX(id) FROM customers))");
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
