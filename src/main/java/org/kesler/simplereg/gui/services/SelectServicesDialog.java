package org.kesler.simplereg.gui.services;

import java.util.Enumeration;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.JButton;
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

	// private String filterString;

	public SelectServicesDialog(JFrame parentFrame, ServicesDialogController controller) {
		super(parentFrame, controller);
		originalRootNode = (DefaultMutableTreeNode) servicesTreeModel.getRoot();
		filteredRootNode = new DefaultMutableTreeNode("Услуги");
	}

	public SelectServicesDialog(JDialog parentDialog, ServicesDialogController controller) {
		super(parentDialog, controller);
		originalRootNode = (DefaultMutableTreeNode) servicesTreeModel.getRoot();
		filteredRootNode = new DefaultMutableTreeNode("Услуги");
	}


	@Override
	protected void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		//панель фильтра
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		final JTextField filterTextField = new JTextField(15);
		filterTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent event) {
				String filterString = filterTextField.getText().trim();
				filterServicesTree(filterString);
			}

			public void removeUpdate(DocumentEvent event) {
				String filterString = filterTextField.getText().trim();
				filterServicesTree(filterString);
			}

			public void changedUpdate(DocumentEvent event) {}
		});


		filterPanel.add(filterTextField);


		// анель дерева услуг
		JPanel treePanel = createSelectTreePanel();

		JPanel buttonPanel = new JPanel();

		JButton selectButton = new JButton("Выбрать");
		this.getRootPane().setDefaultButton(selectButton);		
		selectButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				selectService();
			}
		});

		JButton cancelButton = new JButton("Отменить");
		cancelButton.setIcon(ResourcesUtil.getIcon("cancel.png"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				result = CANCEL;
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

	private void selectService() {
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

		result = OK;
		setVisible(false);

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
				if (node == null || node.isRoot()) {
					selectedNode = null;
					selectedService = null;
					return;
				}	
				selectedNode = node;
				selectedService = (Service)node.getUserObject();
			}
		});

		servicesTree.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override 
			public void mouseClicked(java.awt.event.MouseEvent ev) {
				if (ev.getClickCount() == 2) selectService();
			}
		});

		JScrollPane servicesScrollPane = new JScrollPane(servicesTree);

		treePanel.add(BorderLayout.CENTER, servicesScrollPane);


		return treePanel;		
	}

	// создает дерево от нового корня, отфильтрованное по строке поиска
	private void filterServicesTree(String filterString) {
		
		// если строка поиска пустая - используем оригинальное дерево
		if (filterString.isEmpty()) {
			servicesTreeModel.setRoot(originalRootNode);
		} else {

			// создаем дерево на основе существующего
			filteredRootNode = copyNode(originalRootNode);
			
			// фильтруем дерево
			boolean badLeaves = true;
			while(badLeaves) {
				badLeaves = removeBadLives(filteredRootNode, filterString);
			}
			
			servicesTreeModel.setRoot(filteredRootNode);	

            for (int i = 0; i < servicesTree.getRowCount(); i++) {
                servicesTree.expandRow(i);
            }
            // выбираем первый лист
            DefaultMutableTreeNode firstLeaf = filteredRootNode.getFirstLeaf();
            if (!firstLeaf.isRoot()) {
            	servicesTree.setSelectionPath(new TreePath(firstLeaf.getPath()));
            } else {
            	selectedNode = null;
            	selectedService = null;
            }
            

		}

	}

	private DefaultMutableTreeNode copyNode(DefaultMutableTreeNode origNode) {
	 	
	 	DefaultMutableTreeNode newNode = new DefaultMutableTreeNode();
	 	newNode.setUserObject(origNode.getUserObject());

	 	Enumeration<DefaultMutableTreeNode> children = origNode.children();

	 	while(children.hasMoreElements()) {
	 		DefaultMutableTreeNode child = children.nextElement();
	 		newNode.add(copyNode(child));
	 	}

	 	return newNode;
	}

	private boolean removeBadLives(DefaultMutableTreeNode root, String filterString) {

		boolean badLeaves = false;

		DefaultMutableTreeNode leaf = root.getFirstLeaf();

		if(leaf.isRoot()) return false; // root - единственный узел

		int leafCount = root.getLeafCount(); //this get method changes if in for loop so have to define outside of it
		
		for (int i = 0; i < leafCount; i++) {

			DefaultMutableTreeNode nextLeaf = leaf.getNextLeaf();

			//if it does not start with the text then snip it off its parent
			if (leaf.getUserObject().toString().toLowerCase().indexOf(filterString.toLowerCase(),0) == -1) {
			    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) leaf.getParent();

			    if (parent != null)
			        parent.remove(leaf);

			    badLeaves = true;
			}
			leaf = nextLeaf;
		}

		return badLeaves;

	}

}