package migration;

import java.sql.*;
import java.util.ArrayList;
import models.Customer;
import models.DbConnection;

public class CustomerMigrationHelper {
    public static boolean customerMigration() {
        ArrayList<Customer> customerList = new ArrayList<>();
        String sqlCustomers = """
            SELECT id, cliente_nombre, doc_type, cliente_nodoc, birth_date, esempresasn, cliente_giro,
            CONCAT(cliente_dir1, ' ', cliente_dir2) AS direccion, cliente_tel, cliente_correo, darcreditosn,
            creadopor_id, gana_puntos, fecha_creacion, suc_id
            FROM clientes;
        """;

        try (
            Connection remoteConn = DbConnection.conectarseRemoto();
            PreparedStatement stmt = remoteConn.prepareStatement(sqlCustomers);
            ResultSet result = stmt.executeQuery()
        ) {
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
                customer.setCreatedAt(result.getDate("fecha_creacion"));

                customerList.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (customerList.isEmpty()) {
            System.out.println("No hay clientes para migrar.");
            return true;
        }

        String sqlInsert = """
            INSERT INTO customers (id, name, doc_type, doc_number, birth_date, is_company, bussines_type, address, phone,
            email, has_credit, created_by, earn_points, creation_branch, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (
            Connection localConn = DbConnection.conectarseLocal();
            PreparedStatement stmtInsert = localConn.prepareStatement(sqlInsert)
        ) {
            localConn.setAutoCommit(false);  // 🔥 Desactiva autocommit

            int batchSize = 500;
            int count = 0;

            for (Customer customer : customerList) {
                stmtInsert.setInt(1, customer.getId());
                stmtInsert.setString(2, customer.getName());
                stmtInsert.setInt(3, customer.getDocType());
                stmtInsert.setString(4, customer.getDocNumber());
                stmtInsert.setDate(5, customer.getBirthDate());
                stmtInsert.setBoolean(6, customer.isCompany());
                stmtInsert.setString(7, customer.getBussinesType());
                stmtInsert.setString(8, customer.getAddress());
                stmtInsert.setString(9, customer.getPhone());
                stmtInsert.setString(10, customer.getEmail());
                stmtInsert.setBoolean(11, customer.isHasCredit());
                stmtInsert.setInt(12, customer.getCreatedBy());
                stmtInsert.setBoolean(13, customer.isEarnPoints());
                stmtInsert.setInt(14, customer.getCreationBranch());
                stmtInsert.setDate(15, customer.getCreatedAt());
                stmtInsert.addBatch();

                if (++count % batchSize == 0) {
                    stmtInsert.executeBatch();
                }
            }
            stmtInsert.executeBatch();
            localConn.commit();

            // 🔥 Actualiza la secuencia de IDs
            try (Statement stmtUpdate = localConn.createStatement()) {
                stmtUpdate.execute("SELECT setval(pg_get_serial_sequence('customers', 'id'), (SELECT MAX(id) FROM customers))");
            }
            System.out.println("Migración completada exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
