package org.kesler.simplereg.gui.reestr;

import java.util.List;
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

import org.kesler.simplereg.logic.reception.Reception;
import org.kesler.simplereg.gui.reestr.column.ReestrColumn;
import org.kesler.simplereg.gui.reestr.column.ReestrColumns;

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
		setFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.openFilterDialog();
			}
		});

		JButton resetFilterButton = new JButton("Очистить");
		resetFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.resetFilter();
			}
		});

		JButton applyFilterButton = new JButton("Применить");
		applyFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.applyFilter();
			}
		});

		JButton selectColumnsButton = new JButton("Колонки");
		selectColumnsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.openColumnsDialog();
				reestrTableModel.fireTableStructureChanged();
			}
		});

		// Собираем панель фильтра
		filterPanel.add(new JLabel("Фильтр: "));
		filterPanel.add(filterLabel, "growx");
		filterPanel.add(setFilterButton, "wrap");
		filterPanel.add(applyFilterButton);
		filterPanel.add(selectColumnsButton);


		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill, nogrid"));


		reestrTableModel = new ReestrTableModel();
		reestrTable = new JTable(reestrTableModel);
		JScrollPane reestrTableScrollPane = new JScrollPane(reestrTable);

		dataPanel.add(reestrTableScrollPane, "push, grow");

		// Панель кнопок
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
			return controller.getReceptions().size();
		}

		public int getColumnCount() {
			return ReestrColumns.getInstance().getActiveColumns().size() + 1;
		}

		public String getColumnName(int column) {
			List<ReestrColumn> reestrColumns = ReestrColumns.getInstance().getActiveColumns();			

			String name = "Не опр";

			if (column == 0) {
				name = "№";
			} else {
				name = reestrColumns.get(column - 1).getName();
			}

		return name;
		}

		public Object getValueAt(int row, int column) {
			Reception reception = controller.getReceptions().get(row);
			
			List<ReestrColumn> reestrColumns = ReestrColumns.getInstance().getActiveColumns();			


			Object value = null;

			if (column == 0) {
				value = row + 1;
			} else {
				value = reestrColumns.get(column - 1).getValue(reception);
			}

			return value;
		}

	}

}	