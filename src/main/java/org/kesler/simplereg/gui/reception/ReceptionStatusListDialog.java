package org.kesler.simplereg.gui.reception;

import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.AbstractListModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.util.ResourcesUtil;
import org.kesler.simplereg.logic.reception.ReceptionStatus;

public class ReceptionStatusListDialog extends JDialog {

	private JFrame frame;
	private ReceptionStatusListDialogController controller;

	public ReceptionStatusListDialog(JFrame frame, ReceptionStatusListDialogController controller) {
		super(frame, true);
		this.frame = frame;
		this.controller = controller;

		createGUI();
	}

	private void createGUI() {

		// Основная панель
		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill,nogrid"));

		StatusesListModel statusesListModel = new StatusesListModel();
		JList statusesList = new JList(statusesListModel);
		JScrollPane statusesListScrollPane = new JScrollPane(statusesList);

		JButton addStatusButton = new JButton();
		addStatusButton.setIcon(ResourcesUtil.getIcon("add.png"));
		addStatusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.openAddStatusDialog();
			}
		});

		JButton editStatusButton = new JButton();
		editStatusButton.setIcon(ResourcesUtil.getIcon("pencil.png"));
		editStatusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.openEditStatusDialog();
			}
		});

		JButton removeStatusButton = new JButton();
		removeStatusButton.setIcon(ResourcesUtil.getIcon("delete.png"));




		dataPanel.add(statusesListScrollPane, "grow, wrap");
		dataPanel.add(addStatusButton,"span");
		dataPanel.add(editStatusButton);
		dataPanel.add(removeStatusButton, "wrap");

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton();
		okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

			}
		});


		JButton cancelButton = new JButton();
		cancelButton.setIcon(ResourcesUtil.getIcon("cancel.png"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

			}
		});

		// Собираем панель кнопок
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		// Собираем основную панель
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.pack();
		this.setLocationRelativeTo(frame);

	}

	class StatusesListModel extends AbstractListModel {

		@Override
		public int getSize() {
			return controller.getReceptionStatuses().size();
		}

		@Override
		public ReceptionStatus getElementAt(int index) {
			return controller.getReceptionStatuses().get(index);
		}
	}
}