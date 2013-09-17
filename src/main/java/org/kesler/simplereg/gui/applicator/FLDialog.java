package org.kesler.simplereg.gui.applicator;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.applicator.FL;

public class FLDialog extends JDialog {
	
	private FL fl;
	private JFrame frame;
	private JDialog dialog;

	private JTextField surName;
	private JTextField firstName;
	private JTextField parentName;

	public FLDialog(JFrame frame) {
		super(frame, true);
		this.frame = frame;
		createGUI();
	}

	public FLDialog(JFrame frame, FL fl) {
		super(frame, true);
		this.frame = frame;
		this.fl = fl;
		createGUI();
		loadFLToGUI();
	}

	public FLDialog(JDialog dialog) {
		super(dialog, true);
		this.dialog = dialog;
		createGUI();
	}

	public FLDialog(JDialog dialog, FL fl) {
		super(dialog, true);
		this.dialog = dialog;
		this.fl = fl;
		createGUI();
		loadFLToGUI();
	}

	public FL getFL() {
		saveFLFromGUI();
		return fl;
	}

	private void loadFLToGUI() {
		surName.setText(fl.getSurName());
		firstName.setText(fl.getFirstName());
		parentName.setText(fl.getParentName());
	}

	private void saveFLFromGUI() {
		fl = new FL();
		// Добавить проверку на пустые поля
		fl.setSurName(surName.getText());
		fl.setFirstName(firstName.getText());
		fl.setParentName(parentName.getText());
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = new JPanel(new MigLayout());

		surName = new JTextField(20);
		firstName = new JTextField(20);
		parentName = new JTextField(20);

		dataPanel.add(new JLabel("Фамилия"));
		dataPanel.add(surName, "wrap");
		dataPanel.add(new JLabel("Имя"));
		dataPanel.add(firstName,"wrap");
		dataPanel.add(new JLabel("Отчество"));
		dataPanel.add(parentName, "wrap");
		// Добавить еще поля

		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				saveFLFromGUI();
				setVisible(false);
			}
		});

		JButton cancelButton = new JButton("Отмена");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				fl = null;
				setVisible(false);
			}
		});

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);


		this.setContentPane(mainPanel);
		this.pack();
		if (frame != null) {
			this.setLocationRelativeTo(frame); // вызывать после pack()
		} else if (dialog != null) {
			this.setLocationRelativeTo(dialog);
		} else {
			this.setLocationRelativeTo(null);
		}
	} 

}