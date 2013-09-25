package org.kesler.simplereg.gui.reestr;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import java.awt.BorderLayout;

import org.kesler.simplereg.logic.Reception;


public class ReestrView extends JFrame {
	
	private ReestrViewController controller;

	private JTable reestrTable;
	private ReestrTableModel reestrTableModel;

	public ReestrView(ReestrViewController controller) {
		super("Реестр запросов");
		this.controller = controller;
		createGUI();
	}
	

	

	private void createGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = new JPanel();


		reestrTableModel = new ReestrTableModel();
		reestrTable = new JTable(reestrTableModel);

		dataPanel.add(reestrTable);


		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");

		buttonPanel.add(okButton);

		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.pack();
		this.setLocationRelativeTo(null);
	}

	class ReestrTableModel extends AbstractTableModel {

		public int getRowCount() {
			return controller.getReceptionsList().size();
		}

		public int getColumnCount() {
			return 5;
		}

		public Object getValueAt(int row, int column) {
			Reception reception = controller.getReceptionsList().get(row);
			Object value = null;
			switch (column) {
				case 0:	value = row + 1; break;

				case 1: value = reception.getOpenDate(); break;
				
				case 2:	value = reception.getApplicatorsNames(); break;

				case 3: value = reception.getServiceName(); break;	

			}

			return value;
		}

	}

}	