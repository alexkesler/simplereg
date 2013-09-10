package org.kesler.simplereg.gui.reception;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import org.kesler.simplereg.logic.Reception;

class ReceptionView extends JFrame{

	private ReceptionViewController controler;
	private JButton previousButton;
	private JButton nextButton;
	private JButton readyButton;

	public ReceptionView(ReceptionViewController controller) {
		super("Прием заявителя");
		this.controller = controller;
		createGUI();
	}

	private void createGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel padPanel = new JPanel();

		JPanel buttonPanel = new JPanel();

		previousButton = new JButton("Назад");

		nextButton = new JButton("Далее");

		readyButton = new JButton("Готово");

		buttonPanel.add(previousButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(readyButton); 

		this.add(mainPanel, BorderLayout.CENTER);

		this.setSize(700, 700);
		this.setLocationRelativeTo(null);
	}


}