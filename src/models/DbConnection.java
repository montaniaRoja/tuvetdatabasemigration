package models;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DbConnection {
	
	
	 
	private final static String ruta = "jdbc:postgresql://34.29.157.119/tuvetdb";
	private final static String usuario = "tuvet-user";
	private final static String contrasenia = "ofloda01";
	
	private final static String rutaLocal = "jdbc:postgresql://127.0.0.1/tuvetnewdb";
	private final static String usuarioLocal = "adolfo";
	private final static String contraseniaLocal = "ofloda01";
	  
	 /*
	
	private final static String ruta = "jdbc:postgresql://localhost/tuvetdb";
	private final static String usuario = "adolfo";
	private final static String contrasenia = "ofloda01";
	
	private final static String rutaLocal = "jdbc:postgresql://localhost/newdbtuvet";
	private final static String usuarioLocal = "adolfo";
	private final static String contraseniaLocal = "ofloda01";
	
	*/
	
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
			JOptionPane.showMessageDialog(null,"Conexion Google exitosa");
			return true;
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,"Conexion Google Fallo");
			return false;
		}
		
	}	

}
