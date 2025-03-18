package migration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Ip;
import models.DbConnection;

public class IpMigrationHelper {
	public static boolean ipsMigration() {
        ArrayList<Ip> ipList = new ArrayList<>();
        try {
            Connection connection = DbConnection.conectarseRemoto();
            String sqlIps = "select * from paises order by id;";
            
            PreparedStatement stmt = connection.prepareStatement(sqlIps);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Ip ip = new Ip();
                ip.setId(result.getInt("ip_id"));
                ip.setIp(result.getString("ip_asignada"));
                ip.setBranchId(result.getInt("id_sucursal"));
                ip.setCreatedAt(result.getDate("fecha_creacion"));
                
                ipList.add(ip);
                
            }
            
            Connection guardar = DbConnection.conectarseLocal();
            String sqlSaveCountry = "INSERT INTO authorized_ips (id, ip,branch_id, created_by, created_at)\n"
            		+"VALUES (?,?,?,?,?);";
            PreparedStatement stmtsave = guardar.prepareStatement(sqlSaveCountry);
            
            double records=ipList.size();
            double contador=0;
            double percent=0;
            
            for (Ip ip : ipList) {            	
                int ipId = ip.id;
                String ipIp = ip.ip;
                int branch=ip.branchId;
                int createdBy=1;
                Date createdAt=ip.createdAt;
                
                
               
                stmtsave.setInt(1, ipId);
                stmtsave.setString(2, ipIp);
                stmtsave.setInt(3, branch);
                stmtsave.setInt(4, createdBy);
                stmtsave.setDate(5, createdAt);
                                

                contador+=1;
                percent = contador/records*100;
                
                int rows = stmtsave.executeUpdate();
                
                if (rows > 0) {
                	
                   System.out.println("Porcentaje de Avance ips "+percent+"%");
                    
                }
            }
            Statement stmtUpdateSeq = guardar.createStatement();
            stmtUpdateSeq.execute("SELECT setval(pg_get_serial_sequence('authorized_ips', 'id'), (SELECT MAX(id) FROM authorized_ips))");
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
