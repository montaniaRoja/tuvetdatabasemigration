package models;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DbConnection {
	
	
	 /*
	  private final static String ruta = "jdbc:postgresql://svrdev.c9dhibrycbdz.us-east-2.rds.amazonaws.com/dbtuvet";
	private final static String usuario = "apptuvet";
	private final static String contrasenia = "T3mP0r412O23*_";
	
	private final static String rutaLocal = "jdbc:postgresql://localhost/tuvetdb";
	private final static String usuarioLocal = "adolfo";
	private final static String contraseniaLocal = "ofloda01";
	  
	 */
	
	private final static String ruta = "jdbc:postgresql://localhost/tuvet";
	private final static String usuario = "adolfo";
	private final static String contrasenia = "ofloda01";
	
	private final static String rutaLocal = "jdbc:postgresql://localhost/tuvetdb";
	private final static String usuarioLocal = "adolfo";
	private final static String contraseniaLocal = "ofloda01";
	
	
	
	public static Connection conectarseRemoto() throws SQLException{
		Connection remoteConexion = DriverManager.getConnection(ruta, usuario, contrasenia);
			
		return remoteConexion;	
			
	}
	
	public static Connection conectarseLocal() throws SQLException{
		Connection remoteConexion = DriverManager.getConnection(rutaLocal, usuarioLocal, contraseniaLocal);
			
		return remoteConexion;	
			
	}
	
	
	
	public static boolean probarConexionLocal() {
		try {
			
			Connection connection=conectarseLocal();
			JOptionPane.showMessageDialog(null,"Conexion Local exitosa");
			return true;
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,"Conexion Local Fallo");
			return false;
		}
		
	}
	
	public static boolean probarConexionRemota() {
		try {
			
			Connection connection=conectarseRemoto();
			JOptionPane.showMessageDialog(null,"Conexion AWS exitosa");
			return true;
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,"Conexion AWS Fallo");
			return false;
		}
		
	}	

}
