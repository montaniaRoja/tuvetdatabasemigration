package views;

import javax.swing.JFrame;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JPanel;

import models.DbConnection;
import migration.Migrations;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JButton btnMigration, btnConnection;
	private JPanel panel;
	
	
	
	public MainWindow() {
		buttonConfig();
		panelConfig();
		frameConfig();		
		frame.setVisible(true);
		
		eventsConfig();
	}	

	private void eventsConfig() {
		btnConnection.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				if(DbConnection.probarConexionRemota() && DbConnection.probarConexionLocal()) {
					JOptionPane.showMessageDialog(null,"Ambas conexiones listas");
				}else {
					JOptionPane.showMessageDialog(null,"una conexion fallo");
				}
				*/
				
				DbConnection.probarConexionLocal();
				DbConnection.probarConexionRemota();
				
			}
			
		});
		
		btnMigration.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				Date startTime=new Date();
				Migrations.main(null);
				Date endTime=new Date();
				
				long totalTime=endTime.getTime()-startTime.getTime();
				JOptionPane.showMessageDialog(null,"tiempo total "+totalTime);
				
				
			}
			
		});
		
	}

	private void panelConfig() {
		panel=new JPanel();
		panel.setLayout(null);
		panel.add(btnConnection).setBounds(50, 80, 200, 40);
		panel.add(btnMigration).setBounds(50, 130, 200, 40);
		
	}

	private void buttonConfig() {
		btnMigration=new JButton("Ejecutar Migracion");
		btnConnection=new JButton("Probar Conexion");
		
	}

	private void frameConfig() {
		frame = new JFrame("Migracion Base de Datos");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(panel);        
		
	}      
	
	
}
