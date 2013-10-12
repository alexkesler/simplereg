package org.kesler.simplereg.gui.reestr.filter;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

import java.awt.GridLayout;

import net.miginfocom.swing.MigLayout;
import com.toedter.calendar.JDateChooser;

import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.reception.ReceptionStatusesModel;

public class StatusReceptionsFilterDialog extends ReceptionsFilterDialog {

	private List<ReceptionStatus> filterStatuses;

	private List<ReceptionStatus> allStatuses;
	List<JCheckBox> checkBoxes;

	public StatusReceptionsFilterDialog(JFrame frame) {
		super(frame, "Фильтр по состоянию");
	}

	public StatusReceptionsFilterDialog(JFrame frame, StatusReceptionsFilter filter) {
		super(frame, "Фильтр по состоянию", filter);
		this.filterStatuses = filter.getStatuses();
	}

	@Override
	protected void createReceptionsFilter() {
	
		filterStatuses = new ArrayList<ReceptionStatus>();

		receptionsFilter = new StatusReceptionsFilter(filterStatuses);
	}

	@Override
	protected JPanel createDataPanel() {
		JPanel dataPanel = new JPanel(new MigLayout("fill"));

		JPanel statusesPanel = new JPanel(new GridLayout(0,1));
		checkBoxes = new ArrayList<JCheckBox>();

		allStatuses = ReceptionStatusesModel.getInstance().getReceptionStatuses();

		for (ReceptionStatus status: allStatuses) {
			JCheckBox checkBox = new JCheckBox(status.getName());
			checkBoxes.add(checkBox);
			statusesPanel.add(checkBox);
		}

		JScrollPane statusesPanelScrollPane = new JScrollPane(statusesPanel);

		dataPanel.add(statusesPanelScrollPane, "grow");
			
		return dataPanel;
	}

	@Override 
	protected void loadGUIDataFromReceptionsFilter() {

		for (int i = 0; i < allStatuses.size(); i++) {
			ReceptionStatus status = allStatuses.get(i);
			JCheckBox checkBox = checkBoxes.get(i);
			if(filterStatuses.contains(status)) checkBox.setSelected(true);
			else checkBox.setSelected(false);
		}

	}


	@Override
	protected boolean readReceptionsFilterFromGUIData() {

		filterStatuses.clear();

		for (int i = 0; i < checkBoxes.size(); i++) {
			JCheckBox checkBox = checkBoxes.get(i);
			ReceptionStatus status = allStatuses.get(i);
			if (checkBox.isSelected()) {
				filterStatuses.add(status);
			}
		}

		if (filterStatuses.size() == 0) {
			JOptionPane.showMessageDialog(this, "Не выбрано ни одного статуса", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;

	}

}