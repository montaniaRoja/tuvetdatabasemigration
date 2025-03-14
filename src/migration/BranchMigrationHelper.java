package migration;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import models.Branch;
import models.DbConnection;

public class BranchMigrationHelper {
    
    public static boolean branchesMigration() {
        ArrayList<Branch> branchList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlBranch = "SELECT id, sucursal_nombre, sucursal_direccion, sucursal_tel, creada_por, fecha_creacion FROM sucursales;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlBranch);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Branch branch = new Branch();
                branch.setId(result.getInt("id"));
                branch.setName(result.getString("sucursal_nombre"));
                branch.setAddress(result.getString("sucursal_direccion"));
                branch.setPhone(result.getString("sucursal_tel"));
                branch.setCreated_by(1);
                branch.setCreated_at(result.getDate("fecha_creacion"));
                branchList.add(branch);
            }
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSaveBranch = "INSERT INTO branches (id, name, address, phone, created_by, created_at) VALUES (?,?,?,?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveBranch);
            
            double records=branchList.size();
            double contador=0;
            double percent=0;
            
            for (Branch branch : branchList) {
                System.out.print(branch.id + " ");
                System.out.print(branch.name + " ");
                System.out.print(branch.address + " ");
                System.out.print(branch.phone + " ");
                System.out.print(branch.created_by + " ");
                System.out.print(branch.created_at + " ");
                System.out.println();
                int branchId = branch.id;
                String branchName = branch.name;
                String branchAddress = branch.address;
                String branchPhone = branch.phone;
                int branchCreatedBy = branch.created_by;
                Date branchCreatedAt = branch.created_at;
                
               
                stmtsave.setInt(1, branchId);
                stmtsave.setString(2, branchName);
                stmtsave.setString(3, branchAddress);
                stmtsave.setString(4, branchPhone);
                stmtsave.setInt(5, branchCreatedBy);
                stmtsave.setDate(6, branchCreatedAt);
                
                contador+=1;
                percent = contador/records*100;
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                	
                   System.out.println("Porcentaje de Avance Sucursales "+percent+"%");
                    
                }
            }
            
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('branches', 'id'), (SELECT MAX(id) FROM branches))");
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