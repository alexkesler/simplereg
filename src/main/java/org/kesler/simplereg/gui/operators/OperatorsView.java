package org.kesler.simplereg.gui.operators;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;

import org.kesler.simplereg.logic.Operator;


public class OperatorsView extends JFrame {

	private OperatorsViewController controller;
	private OperatorsTableModel tableModel;

	public OperatorsView(OperatorsViewController controller) {
		super("Операторы");
		this.controller = controller;
		tableModel = new OperatorsTableModel();
		createGUI();

	}

	private void createGUI() {
		this.setSize(500,500);

		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel tablePanel = new JPanel(new GridLayout(1,0));

		JTable operatorsTable = new JTable(tableModel);
		TableColumn column = null;
		for (int i=0; i<5; i++) {
			column = operatorsTable.getColumnModel().getColumn(i);
			if (i == 1) {
				column.setPreferredWidth(200);
			} else {
				column.setMaxWidth(50);
			}
		}


		JScrollPane tableScrollPane = new JScrollPane(operatorsTable);

		tablePanel.add(tableScrollPane);

		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				setVisible(false);
			}
		});

		JButton saveButton = new JButton("Сохранить");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.saveOperators();
			}
		});

		buttonPanel.add(okButton);
		buttonPanel.add(saveButton);


		mainPanel.add(BorderLayout.CENTER, tablePanel);
		mainPanel.add(BorderLayout.SOUTH, buttonPanel);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		this.add(mainPanel,BorderLayout.CENTER);
		this.setLocationRelativeTo(null);


	}

	public void setOperators(List<Operator> operators) {
		tableModel.setOperators(operators);
	}

	private class OperatorsTableModel extends AbstractTableModel {

		private List<Operator> operators;

		public OperatorsTableModel () {
			operators = new ArrayList<Operator>();
		}

		public void setOperators(List<Operator> operators) {
			this.operators = operators;
			fireTableDataChanged();
		}

		@Override 
		public int getRowCount() {
			return operators.size();
		}


		@Override
		public int getColumnCount() {
			return 5;
		}

		@Override
		public String getColumnName(int column) {
			String columnName = "Не опр.";
			switch (column) {
				case 0: columnName="№";
				break;
				case 1: columnName="ФИО";
				break;
				case 2: columnName="Контр";
				break;
				case 3: columnName="Админ";
				break;
				case 4: columnName="Действ";
				break;
			}
			return columnName;
		}

		@Override
		public Object getValueAt(int row, int column) {
			Operator operator = operators.get(row);
			Object value = null;
			switch (column) {
				case 0: value=operator.getId();
				break;
				case 1: value=operator.getFIO();
				break;
				case 2: value=operator.getIsControler();
				break;
				case 3: value=operator.getIsAdmin();
				break;
				case 4: value=operator.getEnabled();
				break;
			}
			return value;

		}

		@Override
		public Class getColumnClass(int column) {
			
			return getValueAt(0, column).getClass();

		}

		@Override
		public boolean isCellEditable(int row, int column) {
			boolean editable = true;
			if (column == 0) {
				editable = false;
			}

			return editable;
		}

		@Override
		public void setValueAt(Object aValue, int row, int column) {
			Operator operator = operators.get(row);
			switch (column) {
				case 1: operator.setFIO((String)aValue);
				break;
				case 2: operator.setIsControler((Boolean)aValue);
				break;
				case 3: operator.setIsAdmin((Boolean)aValue);
				break;
				case 4: operator.setEnabled((Boolean)aValue);
				break;
			}
			fireTableCellUpdated(row, column);
		} 

	}

}