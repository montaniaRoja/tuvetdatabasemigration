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
		 
		 if (PurchaseMigrationHelper.purchasesMigration()) {
	            JOptionPane.showMessageDialog(null, "encaabezados de compras migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar encabezados de compra");
	        }
		 
		 if (PurchaseDetailMigrationHelper.purchasesMigration()) {
	            JOptionPane.showMessageDialog(null, "detalles de compras migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar detalles de compra");
	        }
		 
		 if (TransferMigrationHelper.transfersMigration()) {
	            JOptionPane.showMessageDialog(null, "traslados migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar traslados");
	        }
		 
		 if (TransferDetailMigrationHelper.transferDetailsMigration()) {
	            JOptionPane.showMessageDialog(null, "detalles de traslados migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar traslados");
	        }
		 
		 if (StockEntryMigrationHelper.entriesMigration()) {
	            JOptionPane.showMessageDialog(null, "entradas migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar entradas");
	        }
		 
		 if (StockExitMigrationHelper.exitsMigration()) {
	            JOptionPane.showMessageDialog(null, "salidas migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar salidas");
	        }
	        
		 if (CustomerMigrationHelper.customerMigration()) {
	            JOptionPane.showMessageDialog(null, "clientes migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar clientes");
	        }
	        
	    
		if (InvoiceMigrationHelper.invoiceMigration()) {
            JOptionPane.showMessageDialog(null, "invoices migrados exitosamente");
        }else {
        	JOptionPane.showMessageDialog(null, "hubo errores al migrar invoices");
        }
		
		if (InvoiceDetailMigrationHelper.invoiceMigration()) {
            JOptionPane.showMessageDialog(null, "detalle de invoices migrados exitosamente");
        }else {
        	JOptionPane.showMessageDialog(null, "hubo errores al migrar detalle de invoices");
        }
		
		if (CustomerPointsMigrationHelper.pointsMigration()) {
            JOptionPane.showMessageDialog(null, "transacciones de puntos migrados exitosamente");
        }else {
        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los puntos de los clientes");
        }
		
		}	

}