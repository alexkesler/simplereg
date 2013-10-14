package org.kesler.simplereg.gui.reception;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;

import org.kesler.simplereg.logic.reception.Reception;

public class ReceptionView extends JDialog {

	private JFrame parentFrame;
	private Reception reception;

	public ReceptionView(JFrame parentFrame, Reception reception) {
		super(parentFrame, true);
		this.parentFrame = parentFrame;
		this.reception = reception;

		createGUI();
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = new JPanel();


		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");

		buttonPanel.add(okButton);

		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);


		this.setContentPane(mainPanel);
		this.setSize(500,500);
		this.setLocationRelativeTo(parentFrame);

	}

}