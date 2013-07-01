package org.kesler.simplereg.gui;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JPanel;

import org.kesler.simplereg.logic.

public class MainView extends JFrame {
	private MainViewReceptionsTableModel tableModel = null;

	public MainViev(MainViewReceptionsTableModel tableModel) {
		super("Регистрация заявителей в Росреестре");
		this.setSize(500,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.tableModel = tableModel;

		createGUI();


	}

	private void createGUI () {
		JPanel mainPanel = new JPanel(new BorderLayout());


		JTable receptionTable = new JTable(tableModel);
		JScrollPane receptionTableScrollPane = new JScrollPane(receptionTable);
		JPanel tablePanel = new JPanel(new GridLayout(1,0));
		tablePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		tablePanel.add(receptionTableScrollPane);

		mainPanel.add(BorderLayout.CENTER,tablePanel);

		this.add(mainPanel, BorderLayout.CENTER);	

		this.pack();	
	}

	public void setTableModel (MainViewReceptionsTableModel tableModel) {
		this.tableModel = tableModel;
	}

}