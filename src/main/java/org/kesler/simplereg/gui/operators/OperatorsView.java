package org.kesler.simplereg.gui.operators;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
import java.util.List;
import java.util.ArrayList;

import org.kesler.simplereg.logic.Operator;


public class OperatorsView extends JFrame {

	private OperatorsViewController controller

	public OperatorsView(OperatorsViewController controller) {
		super("Операторы");
		this.controller = controller;
		createGUI();

	}

	private void createGUI() {
		this.setSize(300,400);

		JPanel mainPanel = new JPanel();

		JPanel tablePanel = new JPanel();

		JTable operatorsTable = new JTable();
		JScrollPane tableScrollPane = new JScrollPane(operatorsTable);

		tablePanel.add(tableScrollPane);

		mainPanel.add(tablePanel);

		this.add(mainPanel,BorderLayout.CENTER);
		this.setLocationRelativeTo(null);


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
			operators.getSize();
		}


		@Override
		public int getColumnCount() {
			return 5;
		}

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

		public Object getValueAt(int row, int column) {

		}


	}

}