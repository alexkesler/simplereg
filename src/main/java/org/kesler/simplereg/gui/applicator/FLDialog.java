package org.kesler.simplereg.gui.applicator;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
	
	public static final int NONE = -1;
	public static final int OK = 0;
	public static final int CANCEL = 1;

	private FL fl;
	private JFrame frame;
	private JDialog dialog;

	private int result = NONE;

	private JTextField surName;
	private JTextField firstName;
	private JTextField parentName;

	public FLDialog(JFrame frame) {
		super(frame, true);
		this.frame = frame;
		fl = new FL();
		createGUI();
	}

	public FLDialog(JFrame frame, FL fl) {
		super(frame, true);
		this.frame = frame;
		this.fl = fl;
		createGUI();
	}

	public FLDialog(JDialog dialog) {
		super(dialog, true);
		this.dialog = dialog;
		fl = new FL();
		createGUI();
	}

	public FLDialog(JDialog dialog, FL fl) {
		super(dialog, true);
		this.dialog = dialog;
		this.fl = fl;
		createGUI();
	}

	public FL getFL() {
		return fl;
	}

	private void loadFLToGUI() {
		surName.setText(fl.getSurName());
		firstName.setText(fl.getFirstName());
		parentName.setText(fl.getParentName());
	}

	private boolean saveFLFromGUI() {
		// Добавить проверку на пустые поля
		if (surName.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this,"Поле фамилии не может быть пустым", "Ошибка",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		fl.setSurName(surName.getText());
		fl.setFirstName(firstName.getText());
		fl.setParentName(parentName.getText());
		return true;
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
				if(saveFLFromGUI()) {
					result = OK;
					setVisible(false);
				}	
			}
		});

		JButton cancelButton = new JButton("Отмена");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				result = CANCEL;
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

	public int getResult() {
		return result;
	}

}