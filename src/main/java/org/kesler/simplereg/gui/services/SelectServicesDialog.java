package org.kesler.simplereg.gui.services;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JTree;
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

public class SelectServicesDialog extends ServicesDialog{

	private DefaultMutableTreeNode originalRootNode;
	private DefaultMutableTreeNode filteredRootNode;

	public SelectServicesDialog(JFrame frame, ServicesDialogController controller) {
		super(frame, controller);
		originalRootNode = (DefaultMutableTreeNode) servicesTreeModel.getRoot();
		filteredRootNode = new DefaultMutableTreeNode();
	}

	@Override
	protected void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		//панель фильтра
		JPanel filterPanel = new JPanel();

		JTextField filterTextField = new JTextField(15);


		filterPanel.add(filterTextField);


		// анель дерева услуг
		JPanel treePanel = createSelectTreePanel();

		JPanel buttonPanel = new JPanel();

		JButton selectButton = new JButton("Выбрать");
		selectButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (selectedService == null) {
					JOptionPane.showMessageDialog(null,
                				"Услуга не выбрана",
                				"Ошибка",
                				JOptionPane.ERROR_MESSAGE);
					return ;
				} 

				if (!selectedNode.isLeaf()) {
					JOptionPane.showMessageDialog(null,
                				"Нельзя выбрать обобщающую услугу",
                				"Ошибка",
                				JOptionPane.ERROR_MESSAGE);
					return ;
					
				}

				setVisible(false);

			}
		});

		JButton cancelButton = new JButton("Отменить");
		cancelButton.setIcon(ResourcesUtil.getIcon("cancel.png"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				setVisible(false);
			}
		});

		// собираем панель кнопок
		buttonPanel.add(selectButton);
		buttonPanel.add(cancelButton);

		mainPanel.add(BorderLayout.NORTH, filterPanel);
		mainPanel.add(BorderLayout.CENTER, treePanel);
		mainPanel.add(BorderLayout.SOUTH, buttonPanel);

		this.setContentPane(mainPanel);
		this.setSize(500, 500);
		this.setLocationRelativeTo(parentFrame);



	}


	// создает панель с возможностью выбора услуги без возможности редактирования
	private JPanel createSelectTreePanel() {
		JPanel treePanel = new JPanel(new BorderLayout());
		treePanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Услуги");
		servicesTree = new JTree(rootNode);
		servicesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		servicesTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent ev) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) servicesTree.getLastSelectedPathComponent();
				if (node == null) return;
				if (node.isRoot()) return;

				selectedNode = node;
				selectedService = (Service)node.getUserObject();
			}
		});

		JScrollPane servicesScrollPane = new JScrollPane(servicesTree);

		treePanel.add(BorderLayout.CENTER, servicesScrollPane);


		return treePanel;		
	}

}