package org.kesler.simplereg.gui.util;

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

public class ProcessDialog extends JDialog {

	public static final int NONE = 0;
	public static final int CANCEL = 1;
	public static final int ERROR = -1;

	private static ProcessDialog instance = null;

	private JFrame parentFrame;
	private JDialog parentDialog;
	private JLabel contentLabel;

	private int result;

	public ProcessDialog(JFrame parentFrame, String name, String content) {
		super(parentFrame, name, true);
		this.parentFrame = parentFrame;
		result = NONE;

		createGUI();
		setContent(content);
		setLocationRelativeTo(parentFrame);
	}

	public ProcessDialog(JDialog parentDialog, String name, String content) {
		super(parentDialog, name, true);
		this.parentDialog = parentDialog;
		result = NONE;

		createGUI();
		setContent(content);
		setLocationRelativeTo(parentDialog);
	}

	private ProcessDialog(JDialog parentDialog, String content) {
		super(parentDialog, false);
		result = NONE;

		createGUI();
		setContent(content);
		setUndecorated(true);		
		setLocationRelativeTo(parentDialog);
	}


	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
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
				result = CANCEL;
				setVisible(false);
			}
		});

		buttonPanel.add(cancelButton);


		mainPanel.add(infoPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.setSize(300,140);
		// this.setUndecorated(true);		


	}

	public static synchronized void showProcess(JDialog parentDialog, String content) {
		//hideProcess();
		if (instance != null && instance.getOwner().equals((Window)parentDialog)) {
			instance.setContent(content);
		} else {
			instance = new ProcessDialog(parentDialog, content);
			instance.setVisible(true);			
		}


	}

	public static synchronized void hideProcess() {
		if (instance != null) {
			instance.setVisible(false);
			instance.dispose();
			instance = null;
		}

	}




}