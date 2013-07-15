package org.kesler.simplereg.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServicesView extends JFrame{
	private ServicesViewController controller;

	public ServicesView(ServicesViewController controller) {
		super("Услуги");
		this.controller = controller;
		createGUI();
	}

	public void createGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel treePanel = new JPanel(new BorderLayout());
		treePanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		JTree servicesTree = new JTree();
		JScrollPane servicesScrollPane = new JScrollPane(servicesTree);

		treePanel.add(BorderLayout.CENTER, servicesScrollPane);

		JPanel buttonPanel = new JPanel();

		JButton saveButton = new JButton("Сохранить");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

			}
		});

		JButton cancelButton = new JButton("Отменить");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				setVisible(false);
			}
		});


		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		mainPanel.add(BorderLayout.CENTER, treePanel);
		mainPanel.add(BorderLayout.SOUTH, buttonPanel);

		this.add(mainPanel, BorderLayout.CENTER);	

		this.setSize(300, 300);
		//this.pack();	

		this.setLocationRelativeTo(null);

	}

}