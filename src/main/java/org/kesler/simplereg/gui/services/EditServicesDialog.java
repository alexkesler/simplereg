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
import javax.swing.tree.TreePath;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.kesler.simplereg.util.ResourcesUtil;

import org.kesler.simplereg.logic.Service;

public class EditServicesDialog extends ServicesDialog{


	public EditServicesDialog(JFrame frame, ServicesDialogController controller) {
		super(frame, controller);
	}

	@Override
	protected void createGUI() {
		// Основная панель
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel treePanel = createEditTreePanel();

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton updateButton = new JButton("Обновить");
		updateButton.setIcon(ResourcesUtil.getIcon("arrow_refresh.png"));
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				TreePath selectionPath = servicesTree.getSelectionModel().getSelectionPath();
				controller.reloadTree();
				if (selectionPath != null) {
					servicesTree.getSelectionModel().setSelectionPath(selectionPath);
					servicesTree.makeVisible(selectionPath);
				}
			}
		});


		JButton okButton = new JButton("Ok");
		okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				setVisible(false);
			}
		});


		// Собираем панель кнопок
		buttonPanel.add(updateButton);
		buttonPanel.add(okButton);

		// Собираем основную панель
		mainPanel.add(BorderLayout.CENTER, treePanel);
		mainPanel.add(BorderLayout.SOUTH, buttonPanel);

		this.setContentPane(mainPanel);
		this.setSize(500, 500);
		this.setLocationRelativeTo(parentFrame);

	}


	// создает панель дерева с возможностью редактирования
	private JPanel createEditTreePanel() {
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

				selectedNode = node;
				selectedService = (Service)node.getUserObject();
				// System.out.println("Selected service: " + selectedService);
			}
		});

		JScrollPane servicesScrollPane = new JScrollPane(servicesTree);

		// панель кнопок управления деревом
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		final JButton addButton = new JButton();
		addButton.setIcon(ResourcesUtil.getIcon("add.png"));

		final JPopupMenu addServicePopupMenu = new JPopupMenu();
		// пункт меню добавления услуги на том же уровне
		JMenuItem addServiceMenuItem = new JMenuItem("Добавить услугу на том же уровне");
		addServiceMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (selectedNode.isRoot()) return; /// Добавить вывод сообщения о том, что нельзя добавить
				addSubNode((DefaultMutableTreeNode)selectedNode.getParent());
			}
		});
		// пункт меню добавления подуслуги
		JMenuItem addSubServiceMenuItem = new JMenuItem("Добавить подуслугу");
		addSubServiceMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				addSubNode(selectedNode);
			}
		});

		addServicePopupMenu.add(addServiceMenuItem);
		addServicePopupMenu.add(addSubServiceMenuItem);
		// кнопка для вызова вариантов добавления услуги
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				addServicePopupMenu.show(addButton, addButton.getWidth(), 0);
			}
		});


		// Кнопка редактирования услуги
		JButton editButton = new JButton();
		editButton.setIcon(ResourcesUtil.getIcon("pencil.png"));
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (selectedService != null) {
					controller.editService(selectedService);
				} else {
					JOptionPane.showMessageDialog(null, "Услуга не выбрана", "Ошибка", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});

		// Кнопка удаления услуги
		JButton removeButton = new JButton();
		removeButton.setIcon(ResourcesUtil.getIcon("delete.png"));
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (selectedService != null) {
					controller.removeService(selectedService);
				} else {
					JOptionPane.showMessageDialog(null, "Услуга не выбрана", "Ошибка", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});

		buttonPanel.add(addButton);
		buttonPanel.add(editButton);
		buttonPanel.add(removeButton);


		treePanel.add(servicesScrollPane, BorderLayout.CENTER);
		treePanel.add(buttonPanel, BorderLayout.SOUTH);

		return treePanel;		
	}

	/**
	* Добавляет подузел с соответствующей услугой
	*/
	private void addSubNode(DefaultMutableTreeNode parentNode) {

		Service parentService;
		if (!parentNode.isRoot()) {
			parentService = (Service) parentNode.getUserObject();
		} else {
			parentService = null;
		}

		Service newService = controller.addSubService(parentService);

		if (newService != null) {
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newService);

			servicesTreeModel.insertNodeInto(newNode ,parentNode, parentNode.getChildCount());
		}
		
	}

	private void editNode(DefaultMutableTreeNode node) {
		Service service = (Service)node.getUserObject();
		if (controller.editService(service)) {
			servicesTreeModel.nodeChanged(node);
		}
	}

	private void removeNode(DefaultMutableTreeNode node) {
		Service service = (Service)node.getUserObject();
		int result = JOptionPane.showConfirmDialog(this, "Удалить услугу: " + service + "?", "Удалить?", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			servicesTreeModel.removeNodeFromParent(node);			
			controller.removeService(service);
		}

		
	}



}