package org.kesler.simplereg.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MainView extends JFrame {
	private MainViewController controller = null;
	private MainViewReceptionsTableModel tableModel = null;

	public MainView(MainViewController controller) {
		super("Регистрация заявителей в Росреестре");

		this.controller = controller;
		this.setSize(800,400);
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
				System.exit(0);
			}
		});
		// Формируем основное меню
		mainMenu.add(loginMenuItem);
		mainMenu.add(exitMenuItem);

		// Меню статистики
		JMenu statisticMenu = new JMenu("Статистика");

		JMenuItem statisticMenuItem = new JMenuItem("Статистика приемов");
		statisticMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

			}
		});

		statisticMenu.add(statisticMenuItem);

		// Меню настроек
		JMenu optionsMenu = new JMenu("Настройки");

		JMenuItem servicesMenuItem = new JMenuItem("Услуги");
		servicesMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
//				controller.openServicesView();
			}
		});

		JMenuItem operatorsMenuItem = new JMenuItem("Операторы");
		operatorsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

			}
		});

		optionsMenu.add(servicesMenuItem);
		optionsMenu.add(operatorsMenuItem);

		// Меню О программе
		JMenu aboutMenu = new JMenu("О программе");

		// Формируем меню окна
		menuBar.add(mainMenu);
		menuBar.add(statisticMenu);
		menuBar.add(optionsMenu);
		menuBar.add(aboutMenu);

		this.setJMenuBar(menuBar);

		JPanel buttonPanel = new JPanel();
		JButton newReceptionButton = new JButton("Новый заявитель");
		newReceptionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.openReceptionView();
			}		
		});

		JButton updateButton = new JButton("Обновить");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.readReceptions();
			}
		});

		buttonPanel.add(newReceptionButton);
		buttonPanel.add(updateButton);


		JTable receptionTable = new JTable(tableModel);
		JScrollPane receptionTableScrollPane = new JScrollPane(receptionTable);
		JPanel tablePanel = new JPanel(new GridLayout(1,0));
		tablePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		tablePanel.add(receptionTableScrollPane);

		mainPanel.add(BorderLayout.CENTER,tablePanel);
		mainPanel.add(BorderLayout.NORTH,buttonPanel);

		this.add(mainPanel, BorderLayout.CENTER);	

		this.pack();	

		this.setLocationRelativeTo(null);
	}

	public MainViewReceptionsTableModel getTableModel () {
		return tableModel;
	}

}