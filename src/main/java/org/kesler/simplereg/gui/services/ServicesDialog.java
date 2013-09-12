package org.kesler.simplereg.gui.services;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.JOptionPane;
import javax.swing.tree.TreeModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
// import javax.swing.DropMode;
// import javax.swing.TransferHandler;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.kesler.simplereg.logic.Service;

public class ServicesDialog extends JDialog{
	public static final int SELECT = 0;
	public static final int EDIT = 1;

	private ServicesDialogController controller;
	private JTree servicesTree;
	private Service selectedService = null;


	public ServicesDialog(JFrame frame, ServicesDialogController controller, int mode) {
		super(frame, true);
		this.controller = controller;
		switch(mode) {
			case SELECT:
				this.setContentPane(createSelectContentPane());
				break;
			case EDIT:
				this.setContentPane(createEditContentPane());
				break;
			default :
				this.setContentPane(createSelectContentPane());
		}
		
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);

	}


	private JPanel createEditContentPane() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel treePanel = createTreePanel();

		JPanel buttonPanel = new JPanel();

		JButton updateButton = new JButton("Обновить");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.reloadTree();
			}
		});

		JButton saveButton = new JButton("Сохранить");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.saveTree();
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

		return mainPanel;

	}

	private JPanel createSelectContentPane() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel treePanel = createTreePanel();

		
		JPanel buttonPanel = new JPanel();

		JButton selectButton = new JButton("Выбрать");
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (selectedService == null) {
					JOptionPane.showMessageDialog(null,
                				"Услуга не выбрана",
                				"Ошибка",
                				JOptionPane.ERROR_MESSAGE);
				} else {
					setVisible(false);
				}
			}
		});

		JButton cancelButton = new JButton("Отменить");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				setVisible(false);
			}
		});


		buttonPanel.add(selectButton);
		buttonPanel.add(cancelButton);

		mainPanel.add(BorderLayout.CENTER, treePanel);
		mainPanel.add(BorderLayout.SOUTH, buttonPanel);

		return mainPanel;

	}


	private JPanel createTreePanel() {
		JPanel treePanel = new JPanel(new BorderLayout());
		treePanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Услуги");
		servicesTree = new JTree(rootNode);
		servicesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		servicesTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent ev) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) servicesTree.getLastSelectedPathComponent();
				if (node == null) return;
				if (node.isRoot()) return ;

				selectedService = (Service)node.getUserObject();
			}
		});

		JScrollPane servicesScrollPane = new JScrollPane(servicesTree);

		treePanel.add(BorderLayout.CENTER, servicesScrollPane);


		return treePanel;		
	}

	/**
	* Возвращает модель дерева услуг, привязанную к виду 
	*/
	public TreeModel getTreeModel() {
		return servicesTree.getModel();
	}

	/**
	* Возвращает выбранную услугу
	*/
	public Service getSelectedService() {
		return selectedService;
	}

}