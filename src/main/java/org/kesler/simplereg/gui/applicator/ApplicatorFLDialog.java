package org.kesler.simplereg.gui.applicator;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.util.ResourcesUtil;

import org.kesler.simplereg.logic.applicator.ApplicatorFL;
import org.kesler.simplereg.logic.applicator.FL;

import org.kesler.simplereg.gui.AbstractDialog;

public class ApplicatorFLDialog extends AbstractDialog {

	private ApplicatorFL applicatorFL;
	private JLabel applicatorFIOLabel;
 	private JLabel represFIOLabel;

	public ApplicatorFLDialog(JFrame parentFrame){
		super(parentFrame, true);
		applicatorFL = new ApplicatorFL();
		createGUI();
		setLocationRelativeTo(parentFrame);

	}

	public ApplicatorFLDialog(JDialog parentDialog){
		super(parentDialog, true);
		applicatorFL = new ApplicatorFL();
		createGUI();
		setLocationRelativeTo(parentDialog);
	}

	public ApplicatorFLDialog(JFrame parentFrame, ApplicatorFL applicatorFL) {
		super(parentFrame,"Заявитель - физическое лицо", true);
		setIconImage(ResourcesUtil.getIcon("user.png").getImage());
		this.applicatorFL = applicatorFL;
		createGUI();
		setLocationRelativeTo(parentFrame);
	}

	public ApplicatorFLDialog(JDialog parentDialog, ApplicatorFL applicatorFL) {
		super(parentDialog,"Заявитель - физическое лицо", true);
		setIconImage(ResourcesUtil.getIcon("user.png").getImage());
		this.applicatorFL = applicatorFL;
		createGUI();
		setLocationRelativeTo(parentDialog);
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout());

		applicatorFIOLabel = new JLabel();
		applicatorFIOLabel.setBorder(BorderFactory.createEtchedBorder());

		// Кнопка выбора заявителя 
		JButton selectApplicatorFLButton = new JButton("Выбрать");
		selectApplicatorFLButton.setIcon(ResourcesUtil.getIcon("user_add.png"));
		selectApplicatorFLButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				selectApplicatorFL();
			}
		});


		represFIOLabel = new JLabel();
		represFIOLabel.setBorder(BorderFactory.createEtchedBorder());

		// Кнопка выбора представителя заявителя
		JButton selectRepresFLButton = new JButton("Выбрать");
		selectRepresFLButton.setIcon(ResourcesUtil.getIcon("group_add.png"));
		selectRepresFLButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				selectRepresFL();
			}
		});

		// Кнопка очистки представителя заявителя
		JButton eraseRepresFLButton = new JButton("Очистить");
		eraseRepresFLButton.setIcon(ResourcesUtil.getIcon("group_delete.png"));
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
		okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		this.getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				boolean checkResult = checkFL();
				if (checkResult) {
					result = OK;
					setVisible(false);					
				}
			}
		});

		JButton cancelButton = new JButton("Отмена");
		cancelButton.setIcon(ResourcesUtil.getIcon("cancel.png"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				result = CANCEL;
				setVisible(false);
			}
		});

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		// Добавляем все на основную панель
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);

		updateLabels();

		this.pack();

	}

	private void updateLabels() {
		applicatorFIOLabel.setText(applicatorFL.getFIO());
		represFIOLabel.setText(applicatorFL.getRepresFIO());
	}

	public ApplicatorFL getApplicatorFL() {
		return applicatorFL;
	}

	private void selectApplicatorFL() {
		FL fl = FLListDialogController.getInstance().openSelectDialog(currentDialog);//Модальный вызов
		applicatorFL.setFL(fl);
		updateLabels();		
	}

	private void selectRepresFL() {
		FL fl = FLListDialogController.getInstance().openSelectDialog(currentDialog);//Модальный вызов
		applicatorFL.setRepres(fl);
		updateLabels();
	}

	private void eraseRepresFL() {
		applicatorFL.setRepres(null);
		updateLabels();
	}

	private boolean checkFL() {
		if (applicatorFL.getFL() == null) {
			JOptionPane.showMessageDialog(this,"Физ лицо не выбрано", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
}