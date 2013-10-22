package org.kesler.simplereg.gui.reestr.filter;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import org.kesler.simplereg.gui.reestr.filter.ReceptionsFilter;
import org.kesler.simplereg.gui.reestr.filter.OpenDateReceptionsFilter;


public abstract class ReceptionsFilterDialog extends JDialog {

	public static final int NONE = -1;
	public static final int OK = 0;
	public static final int CANCEL = 1;

	protected JFrame frame;
	
	protected ReceptionsFilter receptionsFilter = null;
	private int result;


	public ReceptionsFilterDialog(JFrame frame, String name) {
		super(frame, name, true);
		this.frame = frame;

		createReceptionsFilter();

		createGUI();
		loadGUIDataFromReceptionsFilter();
		result = NONE;
	}

	public ReceptionsFilterDialog(JFrame frame, String name, ReceptionsFilter filter) {
		super(frame, name, true);
		this.frame = frame;
		this.receptionsFilter = filter;

		createGUI();
		loadGUIDataFromReceptionsFilter();
		result = NONE;
	}


	public int getResult() {
		return result;
	}

	public ReceptionsFilter getReceptionsFilter() {
		return receptionsFilter;
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = createDataPanel();


		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
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
		this.setLocationRelativeTo(frame);
		
	}

	// это надо будет реализовать в специальных диалогах

	abstract protected void createReceptionsFilter();

	abstract protected JPanel createDataPanel(); 

	abstract protected void loadGUIDataFromReceptionsFilter();

	abstract protected boolean readReceptionsFilterFromGUIData();


}