package org.kesler.simplereg.gui.util;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.util.ResourcesUtil;

public class ProcessDialog extends JDialog {

	public static final int NONE = -1;
	public static final int CANCEL = 1;

	private JFrame parentFrame;
	private JLabel contentLabel;

	private int result;

	public ProcessDialog(JFrame parentFrame, String name, String content) {
		super(parentFrame, name, true);
		this.parentFrame = parentFrame;
		result = NONE;

		createGUI();
		setContent(content);
	}

	public int getResult() {
		return result;
	}

	public void setContent(String content) {
		contentLabel.setText(content);
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());


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
		this.setLocationRelativeTo(parentFrame);


	}



}