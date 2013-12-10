package org.kesler.simplereg.gui.applicator;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.util.ResourcesUtil;

import org.kesler.simplereg.logic.FL;
import org.kesler.simplereg.logic.UL;
import org.kesler.simplereg.logic.applicator.ApplicatorUL;

import org.kesler.simplereg.gui.AbstractDialog;

public class ApplicatorULDialog extends AbstractDialog {

	private ApplicatorUL applicatorUL;

	private JLabel nameLabel;
	private JLabel represLabel;

	public ApplicatorULDialog (JFrame parentFrame) {
		super(parentFrame,"Заявитель - юридическое лицо", true);
		this.applicatorUL = new ApplicatorUL();
		createGUI();
		setLocationRelativeTo(parentFrame);
	}

	public ApplicatorULDialog (JDialog parentDialog) {
		super(parentDialog,"Заявитель - юридическое лицо", true);
		this.applicatorUL = new ApplicatorUL();
		createGUI();
		setLocationRelativeTo(parentDialog);
	}


	public ApplicatorULDialog(JFrame parentFrame, ApplicatorUL applicatorUL) {
		super(parentFrame, "Заявитель - юридическое лицо", true);
		this.applicatorUL = applicatorUL;
		createGUI(); 
		setLocationRelativeTo(parentFrame);
	}

	public ApplicatorULDialog(JDialog parentDialog, ApplicatorUL applicatorUL) {
		super(parentDialog, "Заявитель - юридическое лицо", true);
		this.applicatorUL = applicatorUL;
		createGUI(); 
		setLocationRelativeTo(parentDialog);
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = new JPanel(new MigLayout("fill"));

		nameLabel = new JLabel();
		nameLabel.setBorder(BorderFactory.createEtchedBorder());

		JButton selectULButton = new JButton("Выбрать");
		selectULButton.setIcon(ResourcesUtil.getIcon("chart_organisation_add.png"));
		selectULButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				selectApplicatorUL();
			}
		});

		represLabel = new JLabel();
		represLabel.setBorder(BorderFactory.createEtchedBorder());

		JButton selectRepresFLButton = new JButton("Выбрать");
		selectRepresFLButton.setIcon(ResourcesUtil.getIcon("group_add.png"));
		selectRepresFLButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				selectRepresFL();
			}
		});


		JButton clearRepresFLButton = new JButton("Очистить");
		clearRepresFLButton.setIcon(ResourcesUtil.getIcon("group_delete.png"));
		clearRepresFLButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				clearRepresFL();
			}
		});



		dataPanel.add(new JLabel("Юр. лицо:"),"span, wrap");
		dataPanel.add(nameLabel,"grow, push, w 500, h 50");
		dataPanel.add(selectULButton, "wrap");
	
		dataPanel.add(new JLabel("Представитель:"), "wrap");
		dataPanel.add(represLabel,"growx,pushx, w 500");
		dataPanel.add(selectRepresFLButton);
		dataPanel.add(clearRepresFLButton,"wrap");


		// Панель с кнопками
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		this.getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				boolean checkResult = checkUL();
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

		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.pack();

		updateLabels();
	}

	public ApplicatorUL getApplicatorUL() {
		return applicatorUL;
	}

	private void updateLabels() {
		nameLabel.setText("<html>" + applicatorUL.getName() + "</html>");
		represLabel.setText(applicatorUL.getRepresFIO());
	}	

	private void selectApplicatorUL() {
		UL ul = ULListDialogController.getInstance().openSelectDialog(currentDialog);
		applicatorUL.setUL(ul);
		updateLabels();
	}

	private void selectRepresFL() {
		FL fl = FLListDialogController.getInstance().openSelectDialog(currentDialog);//Модальный вызов
		applicatorUL.setRepres(fl);
		updateLabels();
	}

	private void clearRepresFL() {
		applicatorUL.setRepres(null);
		updateLabels();
	}

	private boolean checkUL() {
		if(applicatorUL.getUL() == null) {
			JOptionPane.showMessageDialog(this, "Юр лицо не выбрано", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (applicatorUL.getRepres() == null) {
			JOptionPane.showMessageDialog(this,"Представитель не выбран", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

}