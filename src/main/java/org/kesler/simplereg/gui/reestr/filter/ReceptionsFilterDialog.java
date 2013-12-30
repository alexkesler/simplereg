package org.kesler.simplereg.gui.reestr.filter;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import org.kesler.simplereg.logic.reception.filter.ReceptionsFilter;

import org.kesler.simplereg.gui.AbstractDialog;

public abstract class ReceptionsFilterDialog extends AbstractDialog {

	protected ReceptionsFilter receptionsFilter = null;

	public ReceptionsFilterDialog(JFrame parentFrame, String name) {
		super(parentFrame, name, true);

		createReceptionsFilter();

		createGUI();
		this.setLocationRelativeTo(parentFrame);

		loadGUIDataFromReceptionsFilter();
		result = NONE;
	}

	public ReceptionsFilterDialog(JFrame parentFrame, String name, ReceptionsFilter filter) {
		super(parentFrame, name, true);
		this.receptionsFilter = filter;

		createGUI();
		this.setLocationRelativeTo(parentFrame);

		loadGUIDataFromReceptionsFilter();
		result = NONE;
	}


	public ReceptionsFilter getReceptionsFilter() {
		return receptionsFilter;
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = createDataPanel();


		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		this.getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				
				if (readReceptionsFilterFromGUIData()) {
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


		//собираем основную панель
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.pack();
		
	}

	// это надо будет реализовать в специальных диалогах

	abstract protected void createReceptionsFilter();

	abstract protected JPanel createDataPanel(); 

	abstract protected void loadGUIDataFromReceptionsFilter();

	abstract protected boolean readReceptionsFilterFromGUIData();


}