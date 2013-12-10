package org.kesler.simplereg.gui.applicator;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.util.ResourcesUtil;
import org.kesler.simplereg.logic.UL;

public class ULDialog extends JDialog {

	public static int NONE = -1;
	public static int OK = 0;
	public static int CANCEL = 1;

	private JTextArea fullNameTextArea;
	private JTextArea shortNameTextArea;

	private JFrame frame;
	private JDialog dialog;

	private UL ul;

	private int result;

	public ULDialog(JFrame frame) {
		super(frame, "Создать", true);
		this.frame = frame;
		
		ul = new UL();
		result = NONE;
		
		createGUI();
	}

	public ULDialog(JFrame frame, UL ul) {
		super(frame, "Изменить", true);
		this.frame = frame;
		
		this.ul = ul;
		result = NONE;
		
		createGUI();
	}

	public ULDialog(JDialog dialog) {
		super(dialog, "Создать", true);
		this.dialog = dialog;
		
		ul = new UL();
		result = NONE;
		
		createGUI();
	}

	public ULDialog(JDialog dialog, UL ul) {
		super(dialog, "Изменить", true);
		this.dialog = dialog;
		
		this.ul = ul;
		result = NONE;

		createGUI();
	}


	public int getResult() {
		return result;
	}

	public UL getUL() {
		return ul;
	}	

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill, nogrid"));


		fullNameTextArea = new JTextArea();
		JScrollPane fullNameTextAreaScrollPane = new JScrollPane(fullNameTextArea);


		shortNameTextArea = new JTextArea();
		JScrollPane shortNameTextAreaScrollPane = new JScrollPane(shortNameTextArea);

		//Собираем панель данных
		dataPanel.add(new JLabel("Полное наименование юр. лица:"), "wrap");
		dataPanel.add(fullNameTextAreaScrollPane, "push, grow, wrap, w 500, h 100");
		dataPanel.add(new JLabel("Сокращенное наименование юр. лица"), "wrap");
		dataPanel.add(shortNameTextAreaScrollPane, "push, grow, wrap, w 500, h 50");

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton();
		okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		this.getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				boolean checkResult = saveULFromGUI();
				if(checkResult) {
					result = OK;
					setVisible(false);
				}
				
			}
		});

		JButton cancelButton = new JButton();
		cancelButton.setIcon(ResourcesUtil.getIcon("cancel.png"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				result = CANCEL;
				setVisible(false);
			}
		});
		// Собираем панель кнопок
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		// Собираем основную панель
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.pack();
		if (frame != null) {
			this.setLocationRelativeTo(frame);
		} else if (dialog != null) {
			this.setLocationRelativeTo(dialog);
		} else {
			this.setLocationRelativeTo(null);
		}
		
		loadGUIDataFromUL();

	}

	private void loadGUIDataFromUL() {
		fullNameTextArea.setText(ul.getFullName());
		shortNameTextArea.setText(ul.getShortName());
	}

	private boolean saveULFromGUI() {
		if (fullNameTextArea.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this,"Ошибка", "Полное наименование не может быть пустым",JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (shortNameTextArea.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this,"Ошибка", "Сокращенное наименование не может быть пустым",JOptionPane.ERROR_MESSAGE);
			return false;
		}


		ul.setFullName(fullNameTextArea.getText().trim());
		ul.setShortName(shortNameTextArea.getText().trim());

		return true;
	}


}