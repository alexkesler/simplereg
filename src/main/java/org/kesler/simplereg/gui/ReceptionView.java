package org.kesler.simplereg.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.*;

import org.kesler.simplereg.logic.Reception; 


public class ReceptionView extends JFrame {
	private Reception reception = null;
	private MainViewController controller = null;

	public ReceptionView(MainViewController controller) {
		super("Новая регистрация");
		this.controller = controller;
		createGUI();
	}

	public ReceptionView(MainViewController controller, Reception reception) {
		super("Редактирование регистрации");
		this.controller = controller;
		this.reception = reception;
		createGUI();
	}

	private void createGUI() {
		this.setSize(300,300);
		JPanel mainPanel = new JPanel(new BorderLayout());

		

		JPanel buttonPanel = new JPanel();
		JButton saveButton = new JButton("Сохранить");

		JButton cancelButton = new JButton("Отмена");

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		mainPanel.add(BorderLayout.SOUTH, buttonPanel);

		this.add(mainPanel, BorderLayout.CENTER);
		this.pack();
	}

}