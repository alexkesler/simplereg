package org.kesler.simplereg.gui.reception;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.kesler.simplereg.logic.Reception;

class ReceptionView extends JFrame{

	private ReceptionViewController controller;
	private JButton previousButton;
	private JButton nextButton;
	private JButton readyButton;
	private JButton cancelButton;

	public ReceptionView(ReceptionViewController controller) {
		super("Прием заявителя");
		this.controller = controller;
		createGUI();
	}

	private void createGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel padPanel = new JPanel(new GridLayout(1,1));

		// создаем панель с вкладками
		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.add("Выбор услуги", createServicePanel());
		tabbedPane.add("Заявители", createApplicatorsPanel());
		tabbedPane.add("Печать", createPrintPanel());

		padPanel.add(tabbedPane);

		// Создаем панель кнопок, добавляем кнопки
		JPanel buttonPanel = new JPanel();

		previousButton = new JButton("Назад");

		nextButton = new JButton("Далее");

		readyButton = new JButton("Готово");

		cancelButton = new JButton("Отмена");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.cancel();
			}
		});

		buttonPanel.add(previousButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(readyButton); 
		buttonPanel.add(cancelButton);


		mainPanel.add(padPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.add(mainPanel, BorderLayout.CENTER);

		this.setSize(700, 700);
		this.setLocationRelativeTo(null);
	}

	private JPanel createServicePanel() {
		JPanel servicePanel = new JPanel();

		return servicePanel;
	}

	private JPanel createApplicatorsPanel() {
		JPanel applicatorsPanel = new JPanel();

		return applicatorsPanel;
	}

	private JPanel createPrintPanel() {
		JPanel printPanel = new JPanel();

		return printPanel;
	}


}