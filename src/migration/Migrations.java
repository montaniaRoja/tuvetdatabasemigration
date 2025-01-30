package migration;


import javax.swing.JOptionPane;

public class Migrations {

	public static void main(String[] args) {
		
		 if (BranchMigrationHelper.branchesMigration()) {
	            JOptionPane.showMessageDialog(null, "Sucursales migradas exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los sucursales");
	        }
		 if (CountryMigrationHelper.countriesMigration()) {
	            JOptionPane.showMessageDialog(null, "Paises migradas exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los paises");
	        }
		
		 if (UserMigrationHelper.usersMigration()) {
	            JOptionPane.showMessageDialog(null, "Usuarios migradas exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los usuarios");
	        }
		
		 if (SupplierMigrationHelper.suppliersMigration()) {
	            JOptionPane.showMessageDialog(null, "Proveedores migradas exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los proveedores");
	        }
		 
		 if (BrandMigrationHelper.brandsMigration()) {
	            JOptionPane.showMessageDialog(null, "Marcas migradas exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los marcas");
	        }
		 
		 if (TaxMigrationHelper.taxesMigration()) {
	            JOptionPane.showMessageDialog(null, "Impuestos migradas exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los impuestos");
	        }
		 
		 if (CategoryMigrationHelper.categoriesMigration()) {
	            JOptionPane.showMessageDialog(null, "Categorias migradas exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los Categorias");
	        }
		 
		 if (MolMigrationHelper.moleculesMigration()) {
	            JOptionPane.showMessageDialog(null, "Moleculas migradas exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los Moleculas");
	        }
		 
		 if (ProductMigrationHelper.productsMigration()) {
	            JOptionPane.showMessageDialog(null, "Productos migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los productos");
	        }
		 
		 if (StockMigrationHelper.levelsMigration()) {
	            JOptionPane.showMessageDialog(null, "existencias y reorden migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar existencia y reorden");
	        }
	        
		
		}	

}