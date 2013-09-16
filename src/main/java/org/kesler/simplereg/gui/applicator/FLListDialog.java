package org.kesler.simplereg.gui.applicator;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.AbstractListModel;
import java.awt.BorderLayout;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.applicator.FL;

public class FLListDialog extends JDialog{
	
	private JFrame frame;

	private FLListModel flListModel;

	public FLListDialog(JFrame frame) {
		super(frame, true);
		createGUI();
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fillx"));

		flListModel = new FLListModel(); 
		JList flList = new JList(flListModel);
		JScrollPane flListScrollPane = new JScrollPane(flList); 

		JButton addButton = new JButton("+");
		JButton editButton = new JButton("Ред");
		JButton deleteButton = new JButton("-");

		dataPanel.add(flListScrollPane, "span, wrap");

		dataPanel.add(addButton, "split");
		dataPanel.add(editButton);
		dataPanel.add(deleteButton,"wrap");

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		JButton cancelButton = new JButton("Отмена");

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		// Добавляем все на основную панель

		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);

		setSize(300,500);
		pack();
		setLocationRelativeTo(frame);

	}

	public void setFLList(List<FL> flList) {
		flListModel.setFLList(flList);
	}

	class FLListModel extends AbstractListModel {
		private List<FL> flList = new ArrayList<FL>();

		public int getSize() {
			return flList.size();
		}

		public FL getElementAt(int index) {
			FL fl = flList.get(index);
			return fl;
		}

		void setFLList(List<FL> flList) {
			this.flList = flList;
		}
	}
}