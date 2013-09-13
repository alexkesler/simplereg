package org.kesler.simplereg.gui.reception;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.Reception;

class ReceptionView extends JFrame{

	private ReceptionViewController controller;
	private JButton backButton;
	private JButton nextButton;
	private JButton readyButton;
	private JButton cancelButton;

	private JTabbedPane tabbedPane;

	private ServicePanel servicePanel;
	private ApplicatorsPanel applicatorsPanel;
	private PrintPanel printPanel;

	public ReceptionView(ReceptionViewController controller) {
		super("Прием заявителя");
		this.controller = controller;
		createGUI();
	}

	private void createGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel padPanel = new JPanel(new GridLayout(1,1));

		// создаем панель с вкладками
		tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.LEFT);
		//tabbedPane.setEnabled(false);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				//
			}
		});

		servicePanel = new ServicePanel();
		applicatorsPanel = new ApplicatorsPanel();
		printPanel = new PrintPanel();

		tabbedPane.add("Выбор услуги", servicePanel);
		tabbedPane.add("Заявители", applicatorsPanel);
		tabbedPane.add("Печать", printPanel);

		padPanel.add(tabbedPane);

		// Создаем панель кнопок, добавляем кнопки
		JPanel buttonPanel = new JPanel();

		backButton = new JButton("Назад");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.back();
			}
		});

		nextButton = new JButton("Далее");
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.next();
			}
		});

		readyButton = new JButton("Готово");
		readyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.ready();
			}
		});

		cancelButton = new JButton("Отмена");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.cancel();
			}
		});

		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(readyButton); 
		buttonPanel.add(cancelButton);


		mainPanel.add(padPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.add(mainPanel, BorderLayout.CENTER);

		this.setSize(700, 700);
		this.setLocationRelativeTo(null);
	}

	// Создаем панель выбоа услуги
	class ServicePanel extends JPanel {
		
		JLabel serviceNameLabel;

		ServicePanel() {
			super(new MigLayout());

			serviceNameLabel = new JLabel("Услуга не выбрана");
			JButton selectServiceButton = new JButton("Выбрать");
			selectServiceButton.addActionListener(new ServiceReceptionViewState(controller, this));

			this.add(serviceNameLabel,"growx 100");
			this.add(selectServiceButton, "right, wrap");

		}

		JLabel getServiceNameLabel() {
			return serviceNameLabel;
		}

	}

	class ApplicatorsPanel extends JPanel {

		ApplicatorsPanel() {
			super(new MigLayout());
		}

	}

	class PrintPanel extends JPanel {

		PrintPanel() {
			super(new MigLayout());
		}

	}

	JButton getBackButton() {
		return backButton;
	}

	JButton getNextButton() {
		return nextButton;
	}

	JButton getReadyButton() {
		return readyButton;
	}

	JTabbedPane getTabbedPane() {
		return tabbedPane;
	}


	JPanel getServicePanel() {
		return servicePanel;
	}


}