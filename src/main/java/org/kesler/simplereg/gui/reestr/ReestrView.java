package org.kesler.simplereg.gui.reestr;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.Reception;


public class ReestrView extends JFrame {
	
	private ReestrViewController controller;

	private JTable reestrTable;
	private ReestrTableModel reestrTableModel;

	private JLabel filterLabel;

	public ReestrView(ReestrViewController controller) {
		super("Реестр запросов");
		this.controller = controller;
		createGUI();
	}
	

	

	private void createGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel filterPanel = new JPanel(new MigLayout("fill, nogrid"));

		filterLabel = new JLabel();
		JButton setFilterButton = new JButton("Изменить");

		filterPanel.add(new JLabel("Фильтр: "));
		filterPanel.add(filterLabel, "growx");
		filterPanel.add(setFilterButton);

		JPanel dataPanel = new JPanel(new MigLayout("fill, nogrid"));


		reestrTableModel = new ReestrTableModel();
		reestrTable = new JTable(reestrTableModel);
		JScrollPane reestrTableScrollPane = new JScrollPane(reestrTable);

		dataPanel.add(reestrTableScrollPane, "push, grow");


		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				setVisible(false);
			}
		});

		buttonPanel.add(okButton);

		mainPanel.add(filterPanel, BorderLayout.NORTH);
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

				case 4: value = reception.getStatusName(); break;

			}

			return value;
		}

	}

}	