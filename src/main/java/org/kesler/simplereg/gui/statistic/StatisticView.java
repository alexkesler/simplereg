package org.kesler.simplereg.gui.statistic;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;

public class StatisticView extends JFrame {

	// private StatisticTableModel tableModel;

	public StatisticView() {
		super("Статистика принятых заявителей");

		createGUI();

	} 

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());
	
		JPanel filterPanel = new JPanel();

		JPanel tablePanel = new JPanel(new BorderLayout());

		JTable statisticTable = new JTable();
		JScrollPane tableScrollPane = new JScrollPane(statisticTable);



		JPanel buttonPanel = new JPanel();

		this.setSize(500,500);

	}

	// private class StatisticTableModel extends AbstractTableModel {

	// }

}