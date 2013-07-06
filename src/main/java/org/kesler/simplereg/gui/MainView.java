package org.kesler.simplereg.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MainView extends JFrame {
	private MainViewReceptionsTableModel tableModel = null;

	public MainView() {
		super("Регистрация заявителей в Росреестре");
		this.setSize(500,400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.tableModel = new MainViewReceptionsTableModel();

		createGUI();


	}

	private void createGUI () {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JMenuBar menuBar = new JMenuBar();
		// Основное меню
		JMenu mainMenu = new JMenu("Логин");
		// Пункт меню 
		JMenuItem loginMenuItem = new JMenuItem("Подключиться");
		loginMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev) {

			}
		});
		// Пункт меню
		JMenuItem exitMenuItem = new JMenuItem("Выход");
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

			}
		});
		// Формируем основное меню
		mainMenu.add(loginMenuItem);
		mainMenu.add(exitMenuItem);

		// Меню статистики
		JMenu statisticMenu = new JMenu("Статистика");

		// Меню настроек
		JMenu optionsMenu = new JMenu("Настройки");

		// Меню О программе
		JMenu aboutMenu = new JMenu("О программе");

		// Формируем меню окна
		menuBar.add(mainMenu);
		menuBar.add(statisticMenu);
		menuBar.add(optionsMenu);
		menuBar.add(aboutMenu);

		this.setJMenuBar(menuBar);

		JTable receptionTable = new JTable(tableModel);
		JScrollPane receptionTableScrollPane = new JScrollPane(receptionTable);
		JPanel tablePanel = new JPanel(new GridLayout(1,0));
		tablePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		tablePanel.add(receptionTableScrollPane);

		mainPanel.add(BorderLayout.CENTER,tablePanel);

		this.add(mainPanel, BorderLayout.CENTER);	

		this.pack();	
	}

	public MainViewReceptionsTableModel getTableModel () {
		return tableModel;
	}

}