package org.kesler.simplereg.gui.util;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.Window;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.util.ResourcesUtil;

import org.kesler.simplereg.gui.util.processdialog.ProcessDialogListener;

public class ProcessDialog extends JDialog {


	private List<ProcessDialogListener> listeners;

	private JFrame parentFrame;
	private JDialog parentDialog;
	private JLabel contentLabel;


	public ProcessDialog(JFrame parentFrame) {
		super(parentFrame, false);
		this.parentFrame = parentFrame;
		listeners = new ArrayList<ProcessDialogListener>();

		createGUI();
		// setContent(content);
		setLocationRelativeTo(parentFrame);
	}


	public ProcessDialog(JDialog parentDialog) {
		super(parentDialog, false);
		this.parentDialog = parentDialog;
		listeners = new ArrayList<ProcessDialogListener>();

		createGUI();
		// setContent(content);
		setLocationRelativeTo(parentDialog);
	}

    public ProcessDialog() {
        super();
        listeners = new ArrayList<ProcessDialogListener>();

        createGUI();
        // setContent(content);
        setLocationRelativeTo(null);
    }

    public void addProcessDialogListener(ProcessDialogListener listener) {
		listeners.add(listener);
	}


	public void setContent(String content) {
		contentLabel.setText(content);
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEtchedBorder());


		JPanel infoPanel = new JPanel(new MigLayout("fill"));

		contentLabel  = new JLabel();
		//contentLabel.setPreferredWidth(100);
			
		JLabel processLabel = new JLabel();
		processLabel.setIcon(ResourcesUtil.getIcon("loading.gif"));	

		infoPanel.add(contentLabel, "center ,wrap");
		infoPanel.add(processLabel,"center");


		JPanel buttonPanel = new JPanel();

		JButton cancelButton = new JButton("Отмена");
		cancelButton.setIcon(ResourcesUtil.getIcon("cancel.png"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				notifyListenersCancel();
				// if (processThread != null) processThread.interrupt();
				hideProcess();

			}
		});

		buttonPanel.add(cancelButton);


		mainPanel.add(infoPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.setSize(300,140);
		// this.setUndecorated(true);		


	}

	private void notifyListenersCancel() {
		for (ProcessDialogListener listener: listeners) {
			listener.cancelProcess();
		}
	}

	public void showProcess(String content) {
		setContent(content);
		setVisible(true);
	}


	public void hideProcess() {
		
		setVisible(false); // Скрываем диалог
		listeners.clear(); 			// Очищаем слушателей
		dispose();			// Освобождаем ресурсы

	}




}