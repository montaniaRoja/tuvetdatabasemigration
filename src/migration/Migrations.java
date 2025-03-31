package migration;


import javax.swing.JOptionPane;

public class Migrations {

	public static void main(String[] args) {
		
		
		 if (BranchMigrationHelper.branchesMigration()) {
	            System.out.println("Sucursales migradas exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los sucursales");
	        }
		 if (CountryMigrationHelper.countriesMigration()) {
			 System.out.println("Paises migradas exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los paises");
	        }
		
		 if (UserMigrationHelper.usersMigration()) {
			 System.out.println("Usuarios migradas exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los usuarios");
	        }
		
		 if (SupplierMigrationHelper.suppliersMigration()) {
			 System.out.println("Proveedores migradas exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los proveedores");
	        }
		 
		 if (BrandMigrationHelper.brandsMigration()) {
			 System.out.println("Marcas migradas exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los marcas");
	        }
		 
		 if (TaxMigrationHelper.taxesMigration()) {
			 System.out.println("Impuestos migradas exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los impuestos");
	        }
		 
		 if (CategoryMigrationHelper.categoriesMigration()) {
			 System.out.println("Categorias migradas exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los Categorias");
	        }
		 
		 if (MolMigrationHelper.moleculesMigration()) {
			 System.out.println("Moleculas migradas exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los Moleculas");
	        }
		 
		 if (ProductMigrationHelper.productsMigration()) {
			 System.out.println("Productos migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los productos");
	        }
		 
		 if (StockMigrationHelper.levelsMigration()) {
			 System.out.println("existencias y reorden migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar existencia y reorden");
	        }
		 
		 if (PurchaseMigrationHelper.purchasesMigration()) {
			 System.out.println("encaabezados de compras migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar encabezados de compra");
	        }
		 
		 if (PurchaseDetailMigrationHelper.purchasesMigration()) {
			 System.out.println("detalles de compras migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar detalles de compra");
	        }
		 
		 if (TransferMigrationHelper.transfersMigration()) {
			 System.out.println("traslados migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar traslados");
	        }
		 
		 if (TransferDetailMigrationHelper.transferDetailsMigration()) {
			 System.out.println("detalles de traslados migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar traslados");
	        }
		 
		 if (StockEntryMigrationHelper.entriesMigration()) {
			 System.out.println("entradas migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar entradas");
	        }
		 
		 if (StockExitMigrationHelper.exitsMigration()) {
			 System.out.println("salidas migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar salidas");
	        }
	        
		 if (CustomerMigrationHelper.customerMigration()) {
			 System.out.println("clientes migrados exitosamente");
	        }else {
	        	JOptionPane.showMessageDialog(null, "hubo errores al migrar clientes");
	        }
	        
	    
		if (InvoiceMigrationHelper.invoiceMigration()) {
			System.out.println("invoices migrados exitosamente");
        }else {
        	JOptionPane.showMessageDialog(null, "hubo errores al migrar invoices");
        }
		
		if (InvoiceDetailMigrationHelper.invoiceMigration()) {
			System.out.println("detalle de invoices migrados exitosamente");
        }else {
        	JOptionPane.showMessageDialog(null, "hubo errores al migrar detalle de invoices");
        }
		
		if (CustomerPointsMigrationHelper.pointsMigration()) {
			System.out.println("transacciones de puntos migrados exitosamente");
        }else {
        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los puntos de los clientes");
        }
		
		if (GroomerMigrationHelper.groomersMigration()) {
			System.out.println("gromistas migrados exitosamente");
        }else {
        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los gromistas");
        }
		
		if (BreedMigrationHelper.breedsMigration()) {
			System.out.println("Razas migrados exitosamente");
        }else {
        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los razas");
        }
		
		if (AnalisysMigrationHelper.analysisMigration()) {
			System.out.println("analisis migrados exitosamente");
        }else {
        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los analisis");
        }
		
		if (PetMigrationHelper.petsMigration()) {
			System.out.println("mascotas migrados exitosamente");
        }else {
        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los mascotas");
        }
		

		if (HistoryMigrationHelper.historyMigration()) {
			System.out.println("historiales migrados exitosamente");
        }else {
        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los historiales");
        }
		
		if (PrescriptionMigrationHelper.recetasMigration()) {
			System.out.println("recetas migrados exitosamente");
        }else {
        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los recetas");
        }
		
		if (PetAnalysisMigrationHelper.analysisMigration()) {
			System.out.println("analisis migrados exitosamente");
        }else {
        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los analisis");
        }
        
        
		if (IpMigrationHelper.ipsMigration()) {
			System.out.println("ips migrados exitosamente");
        }else {
        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los ip");
        }
		
		
		if (SessionMigrationHelper.sessionMigration()) {
			System.out.println("sesiones migrados exitosamente");
        }else {
        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los sesiones");
        }
		
		if (SessionDetailMigrationHelper.sessionMigration()) {
			System.out.println("detalles sesiones migrados exitosamente");
        }else {
        	JOptionPane.showMessageDialog(null, "hubo errores al migrar los detalles");
        }
		
		}	

}