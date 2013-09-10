package org.kesler.simplereg.gui.statistic;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
		tablePanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		JTable statisticTable = new JTable();
		JScrollPane tableScrollPane = new JScrollPane(statisticTable);
		tablePanel.add(tableScrollPane);


		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				setVisible(false);
			}
		});

		buttonPanel.add(okButton);


		mainPanel.add(filterPanel, BorderLayout.NORTH);
		mainPanel.add(tablePanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.add(mainPanel, BorderLayout.CENTER);

		this.setSize(500,500);
		this.setLocationRelativeTo(null);

	}

	// private class StatisticTableModel extends AbstractTableModel {

	// }

}