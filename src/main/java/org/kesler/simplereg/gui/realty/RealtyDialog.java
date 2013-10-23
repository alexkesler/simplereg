package org.kesler.simplereg.gui.realty;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;

import net.miginfocom.swing.MigLayout;

public class RealtyDialog extends JDialog {

	private JDialog parentDialog

	public RealtyDialog(JDialog parentDialog) {
		super(parentDialog, "Объект недвижимости", true);
		this.parentDialog = parentDialog;

		createGUI();
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = new JPanel(new MigLayout("fill"));

		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");

		buttonPanel.add(okButton);

		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

	}

}