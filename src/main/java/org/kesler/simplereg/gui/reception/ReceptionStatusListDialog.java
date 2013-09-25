package org.kesler.simplereg.gui.reception;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.BorderLayout;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.util.ResourcesUtil;

public class ReceptionStatusListDialog extends JDialog {

	private JFrame frame;

	public ReceptionStatusListDialog(JFrame frame) {
		super(frame, true);
		this.frame = frame;

		createGUI();
	}

	private void createGUI() {

		// Основная панель
		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout());

		

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton();
		JButton cancelButton = new JButton();

		// Собираем панель кнопок
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		// Собираем основную панель
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.pack();
		this.setLocationRelativeTo(frame);

	}
}