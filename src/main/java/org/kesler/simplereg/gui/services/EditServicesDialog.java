package org.kesler.simplereg.gui.services;

import java.util.Enumeration;
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
import javax.swing.Action;
import javax.swing.AbstractAction;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.kesler.simplereg.gui.util.InfoDialog;
import org.kesler.simplereg.util.ResourcesUtil;

import org.kesler.simplereg.logic.Service;

public class EditServicesDialog extends ServicesDialog{

	private JDialog currentDialog;

	private Action addSubNodeAction;
	private Action addNodeAction;
	private Action editNodeAction;
	private Action removeNodeAction;

	public EditServicesDialog(JFrame frame, ServicesDialogController controller) {
		super(frame, controller);
		currentDialog = this;

	}

	@Override
	protected void createGUI() {
		// Основная панель
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel treePanel = createEditTreePanel();

		// Панель кнопок
		JPanel buttonPanel = new JPanel();



		JButton okButton = new JButton("Ok");
		okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		this.getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				setVisible(false);
			}
		});


		// Собираем панель кнопок
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
				selectedNode = (DefaultMutableTreeNode) servicesTree.getLastSelectedPathComponent();

				if (selectedNode == null) {
					addSubNodeAction.setEnabled(false);
					editNodeAction.setEnabled(false);
					removeNodeAction.setEnabled(false);
				} else {
					addSubNodeAction.setEnabled(true);
					if (selectedNode.isRoot()) {
						addNodeAction.setEnabled(false);
						editNodeAction.setEnabled(false);
						removeNodeAction.setEnabled(false);
					} else {
						addNodeAction.setEnabled(true);
						editNodeAction.setEnabled(true);
						removeNodeAction.setEnabled(true);

					}

					if (!selectedNode.isLeaf()) {
						removeNodeAction.setEnabled(false);						
					}					
				}


			}
		});

		JScrollPane servicesScrollPane = new JScrollPane(servicesTree);

		// панель кнопок управления деревом
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		final JButton addButton = new JButton();
		addButton.setIcon(ResourcesUtil.getIcon("add.png"));
		addButton.setToolTipText("Добавить услугу");

		final JPopupMenu addServicePopupMenu = new JPopupMenu();

		// пункт меню добавления услуги на том же уровне
		addNodeAction = new AddNodeAction();
		JMenuItem addServiceMenuItem = new JMenuItem(addNodeAction);


		// пункт меню добавления подуслуги
		addSubNodeAction = new AddSubNodeAction();
		JMenuItem addSubServiceMenuItem = new JMenuItem(addSubNodeAction);


		// Собираем всплывающее меню
		addServicePopupMenu.add(addServiceMenuItem);
		addServicePopupMenu.add(addSubServiceMenuItem);

		// кнопка для вызова вариантов добавления услуги
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				addServicePopupMenu.show(addButton, addButton.getWidth(), 0);
			}
		});


		// Кнопка редактирования услуги
		editNodeAction = new EditNodeAction();
		JButton editButton = new JButton(editNodeAction);


		// Кнопка удаления услуги
		removeNodeAction = new RemoveNodeAction();
		JButton removeButton = new JButton(removeNodeAction);

		// Кнопка обновления
		JButton updateButton = new JButton();
		updateButton.setIcon(ResourcesUtil.getIcon("arrow_refresh.png"));
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
					
				/////// Перезагружаем дерево
				controller.reloadTree();

			}
		});


		buttonPanel.add(addButton);
		buttonPanel.add(editButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(updateButton);


		treePanel.add(servicesScrollPane, BorderLayout.CENTER);
		treePanel.add(buttonPanel, BorderLayout.SOUTH);

		return treePanel;		
	}

	/**
	* Добавляет подузел с соответствующей услугой
	*/
	private void addSubNode(DefaultMutableTreeNode parentNode) {

		if (parentNode == null) return ;
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

			servicesTree.setSelectionPath(new TreePath(newNode.getPath()));

			new InfoDialog(currentDialog, "Сохранено", 1000, InfoDialog.GREEN).showInfo();	
		}
		
	}

	class AddSubNodeAction extends AbstractAction {

		AddSubNodeAction () {
			super("Добавить подуслугу");
		}

 		public void actionPerformed(ActionEvent ev) {
			if (selectedNode == null) {
				new InfoDialog(currentDialog, "Ничего не выбрано", 1000, InfoDialog.RED).showInfo();
				return;
			}	

			addSubNode(selectedNode);
		}
	}

	class AddNodeAction extends AbstractAction {

		AddNodeAction () {
			super("Добавить услугу на том же уровне");
		}

		public void actionPerformed(ActionEvent ev) {
			if (selectedNode == null) {
				new InfoDialog(currentDialog, "Ничего не выбрано", 1000, InfoDialog.RED).showInfo();
				return;
			}

			if (selectedNode.isRoot()) {
				new InfoDialog(currentDialog, "Не применимо для корневой услуги", 1000, InfoDialog.RED).showInfo();
				return;
			}	

			DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
			addSubNode(parentNode);
		}
	}

	class EditNodeAction extends AbstractAction {

		EditNodeAction() {
			super("", ResourcesUtil.getIcon("pencil.png"));
			putValue("SHORT_DESCRIPTION", "Редактировать услугу");
		}

		public void actionPerformed(ActionEvent ev) {
			if (selectedNode == null) {
				new InfoDialog(currentDialog, "Ничего не выбрано", 1000, InfoDialog.RED).showInfo();
				return;
			} 
			
			if (selectedNode.isRoot()) {
				new InfoDialog(currentDialog, "Не применимо для корневой услуги", 1000, InfoDialog.RED).showInfo();
				return;
			}

			Service selectedService = (Service) selectedNode.getUserObject();
			if (controller.editService(selectedService)) 
				servicesTreeModel.nodeChanged(selectedNode);
				new InfoDialog(currentDialog, "Сохранено", 1000, InfoDialog.GREEN).showInfo();	
		}
	}

	class RemoveNodeAction extends AbstractAction {

		RemoveNodeAction() {
			super("", ResourcesUtil.getIcon("delete.png"));
			putValue("SHORT_DESCRIPTION", "Удалить услугу");
		}

		public void actionPerformed(ActionEvent ev) {
			if (selectedNode == null) {
				new InfoDialog(currentDialog, "Ничего не выбрано", 1000, InfoDialog.RED);
				return;
			}

			if (selectedNode.isRoot()) {
				new InfoDialog(currentDialog, "Не применимо для корневой услуги", 1000, InfoDialog.RED);
				return;
			}

			Service selectedService = (Service) selectedNode.getUserObject();
			int result = JOptionPane.showConfirmDialog(currentDialog, "Удалить услугу: " + selectedService + "?", "Удалить?", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				servicesTreeModel.removeNodeFromParent(selectedNode);			
				controller.removeService(selectedService);
			}

		}
	}


}