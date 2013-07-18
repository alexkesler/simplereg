package org.kesler.simplereg.gui.main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.AbstractAction;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Action;

import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import javax.swing.table.AbstractTableModel;

import org.kesler.simplereg.logic.Reception;


public class MainView extends JFrame {
	private MainViewController controller = null;
	private MainViewReceptionsTableModel tableModel = null;
	protected Action loginAction,
					newReceptionAction, 
					updateReceptionsAction,
					statisticAction,
					exitAction;

	public MainView(MainViewController controller) {
		super("Регистрация заявителей в Росреестре");

		this.controller = controller;
		this.setSize(800,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.tableModel = new MainViewReceptionsTableModel();
		
		loginAction = new LoginAction();
		newReceptionAction = new NewReceptionAction();
		updateReceptionsAction = new UpdateReceptionsAction();
		statisticAction = new StatisticAction();
		exitAction = new ExitAction();

		createGUI();
	}

	private void createGUI () {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JMenuBar menuBar = new JMenuBar();
		// Основное меню
		JMenu mainMenu = new JMenu("Логин");

		// Пункт меню 
		JMenuItem loginMenuItem = new JMenuItem(loginAction);
		// Пункт меню
		JMenuItem exitMenuItem = new JMenuItem(exitAction);

		// Формируем основное меню
		mainMenu.add(loginMenuItem);
		mainMenu.add(exitMenuItem);

		// Меню статистики
		JMenu statisticMenu = new JMenu("Статистика");

		JMenuItem statisticMenuItem = new JMenuItem(statisticAction);

		statisticMenu.add(statisticMenuItem);

		// Меню настроек
		JMenu optionsMenu = new JMenu("Настройки");

		JMenuItem servicesMenuItem = new JMenuItem("Услуги");
		servicesMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.openServicesView();
			}
		});

		JMenuItem operatorsMenuItem = new JMenuItem("Операторы");
		operatorsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
//				controller.openOperatorsView();
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
		JButton newReceptionButton = new JButton(newReceptionAction);

		JButton updateButton = new JButton(updateReceptionsAction);

		buttonPanel.add(newReceptionButton);
		buttonPanel.add(updateButton);

		// поправляем ширину столбцов, чтобы было покрасивей
		JTable receptionTable = new JTable(tableModel);
		receptionTable.getColumnModel().getColumn(0).setMinWidth(30);
		receptionTable.getColumnModel().getColumn(0).setMaxWidth(40);
		receptionTable.getColumnModel().getColumn(1).setMinWidth(100);
		receptionTable.getColumnModel().getColumn(1).setMaxWidth(500);

		JScrollPane receptionTableScrollPane = new JScrollPane(receptionTable);
		JPanel tablePanel = new JPanel(new GridLayout(1,0));
		tablePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		tablePanel.add(receptionTableScrollPane);

		mainPanel.add(BorderLayout.CENTER,tablePanel);
		mainPanel.add(BorderLayout.NORTH,buttonPanel);

		this.add(mainPanel, BorderLayout.CENTER);	

		//this.pack();	

		this.setLocationRelativeTo(null);
	}

	public MainViewReceptionsTableModel getTableModel () {
		return tableModel;
	}



	class MainViewReceptionsTableModel extends AbstractTableModel {
		private List<Reception> receptions;

		public MainViewReceptionsTableModel() {
			this.receptions = new ArrayList<Reception>();
		}

		public void setReceptions(List<Reception> receptions) {
			this.receptions = receptions;
			fireTableDataChanged();
		}

		public int getRowCount() {
			return receptions.size();
		}

		public int getColumnCount() {
			return 4;
		}

		public String getColumnName(int column) {
			String columnName = "Не опр";
			switch (column) {
					case 0: columnName = "№";
					break;
					case 1: columnName = "Создано";
					break;
					case 2: columnName = "ФИО заявителя";
					break;
					case 3: columnName = "Услуга";
					break;
				}
				return columnName;

		}

		public Object getValueAt(int row, int column) {
			Object value = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			Reception reception = receptions.get(row);
			if (reception != null) {
				switch (column) {
					case 0: value = reception.getId();
					break;
					case 1: value = dateFormat.format(reception.getOpenDate());
					break;
					case 2: value = reception.getApplicatorFIO();
					break;
					case 3: value = reception.getServiceName();
					break;
				}

			}

			return value;

		}
	}


	/// классы Action для вида

	public class LoginAction extends AbstractAction {
		public LoginAction() {
			putValue(Action.NAME, "Подключиться");
			putValue(Action.ACTION_COMMAND_KEY,"login");
		}

		public void actionPerformed(ActionEvent ev) {
			controller.openLoginView();
		}
	}

	public class NewReceptionAction extends AbstractAction {
		public NewReceptionAction() {
			putValue(Action.NAME,"Новый прием");
			putValue(Action.ACTION_COMMAND_KEY, "NewReception");
		}
		public void actionPerformed(ActionEvent ev) {
			controller.openReceptionView();			
		}
	}

	public class UpdateReceptionsAction extends AbstractAction {
		public UpdateReceptionsAction() {
			putValue(Action.NAME, "Обновить");
			putValue(Action.ACTION_COMMAND_KEY, "updateReceptions");
		}

		public void actionPerformed(ActionEvent ev) {
			controller.readReceptions();
		} 
	}

	public class StatisticAction extends AbstractAction {
		public StatisticAction() {
			putValue(Action.NAME, "Статистика приемов");
			putValue(Action.ACTION_COMMAND_KEY, "statistic");
		}

		public void actionPerformed(ActionEvent ev) {
			controller.openStatisticView();
		}
	}

	public class ExitAction extends AbstractAction {
		public ExitAction() {
			putValue(Action.NAME,"Выход");
			putValue(Action.ACTION_COMMAND_KEY, "exit");
		}
		public void actionPerformed(ActionEvent ev) {
			System.exit(0);
		}
	}



}