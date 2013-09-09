package org.kesler.simplereg.gui.services;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServicesView extends JFrame{
	private ServicesViewController controller;
	private JTree servicesTree;

	public ServicesView(ServicesViewController controller) {
		super("Услуги");
		this.controller = controller;
		servicesTreeModel = new ServicesTreeModel();
		createGUI();
	}

	private void createGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel treePanel = new JPanel(new BorderLayout());
		treePanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		DefaultTreeNode rootNode = new DefaultTreeNode("Услуги");
		servicesTree = new JTree(rootNode);
		JScrollPane servicesScrollPane = new JScrollPane(servicesTree);

		treePanel.add(BorderLayout.CENTER, servicesScrollPane);

		JPanel buttonPanel = new JPanel();

		JButton updateButton = new JButton("Обновить");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.readServices();
			}
		});

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



		buttonPanel.add(updateButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		mainPanel.add(BorderLayout.CENTER, treePanel);
		mainPanel.add(BorderLayout.SOUTH, buttonPanel);

		this.add(mainPanel, BorderLayout.CENTER);	

		this.setSize(500, 500);
		//this.pack();	

		this.setLocationRelativeTo(null);

	}

	/**
	* Возвращает модель дерева услуг, привязанную к виду 
	*/
	public DefaultTreeModel getTreeModel() {
		return servicesTree.getModel();
	}

}