package org.kesler.simplereg.gui.applicator;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.applicator.ApplicatorFL;
import org.kesler.simplereg.logic.applicator.FL;

public class ApplicatorFLDialog extends JDialog {

	private JFrame frame;
	private ApplicatorFL applicatorFL;
	private JLabel applicatorFIOLabel;
 	private JLabel represFIOLabel;

	public ApplicatorFLDialog(JFrame frame){
		super(frame, true);
		applicatorFL = new ApplicatorFL();
		createGUI();

	}

	public ApplicatorFLDialog(JFrame frame, ApplicatorFL applicatorFL) {
		super(frame, true);
		this.applicatorFL = applicatorFL;
		createGUI();
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout());

		applicatorFIOLabel = new JLabel("Не опеределено");

		// Кнопка выбора заявителя 
		JButton selectApplicatorFLButton = new JButton("Выбрать");
		selectApplicatorFLButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				selectApplicatorFL();
			}
		});


		represFIOLabel = new JLabel("");

		// Кнопка выбора представителя заявителя
		JButton selectRepresFLButton = new JButton("Выбрать");
		selectRepresFLButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				selectRepresFL();
			}
		});

		// Кнопка очистки представителя заявителя
		JButton eraseRepresFLButton = new JButton("Очистить");
		eraseRepresFLButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				eraseRepresFL();
			}
		});

		dataPanel.add(new JLabel("Заявитель: "));
		dataPanel.add(applicatorFIOLabel, "w 300!");
		dataPanel.add(selectApplicatorFLButton, "wrap");
		dataPanel.add(new JLabel("Представитель: "));
		dataPanel.add(represFIOLabel, "w 300!");
		dataPanel.add(selectRepresFLButton);
		dataPanel.add(eraseRepresFLButton);

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				setVisible(false);
			}
		});

		JButton cancelButton = new JButton("Отмена");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				applicatorFL = null;
				setVisible(false);
			}
		});

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		// Добавляем все на основную панель
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.pack();
		this.setLocationRelativeTo(frame);

	}

	public ApplicatorFL getApplicatorFL() {
		return applicatorFL;
	}

	private void selectApplicatorFL() {
		FLDialog flDialog = new FLDialog(frame);
		flDialog.setVisible(true); //Модальный вызов
		FL fl = flDialog.getFL();
		if (fl != null) {
			applicatorFL.setFL(fl);
			applicatorFIOLabel.setText(fl.getShortFIO());
		}
		
	}

	private void selectRepresFL() {
		FLDialog flDialog = new FLDialog(frame);
		flDialog.setVisible(true); //Модальный вызов
		FL fl = flDialog.getFL();
		if (fl != null) {
			applicatorFL.setRepres(fl);
			represFIOLabel.setText(fl.getShortFIO());
		}

	}

	private void eraseRepresFL() {
		applicatorFL.setRepres(null);
		represFIOLabel.setText("");

	}
}