package org.kesler.simplereg.gui.reestr.filter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;

import java.awt.GridLayout;

import com.alee.extended.date.WebDateField;
import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.reception.ReceptionStatusesModel;

import org.kesler.simplereg.logic.reception.filter.StatusReceptionsFilter;
import org.kesler.simplereg.util.DateUtil;
import org.kesler.simplereg.util.ResourcesUtil;

public class StatusReceptionsFilterDialog extends ReceptionsFilterDialog {

	private List<ReceptionStatus> filterStatuses;

	private List<ReceptionStatus> allStatuses;
	private List<JCheckBox> checkBoxes;

    private WebDateField fromWebDateField;
    private WebDateField toWebDateField;

    public StatusReceptionsFilterDialog(JFrame frame) {
		super(frame, "Фильтр по состоянию");
	}

	public StatusReceptionsFilterDialog(JFrame frame, StatusReceptionsFilter filter) {
		super(frame, "Фильтр по состоянию", filter);
		
	}

	@Override
	protected void createReceptionsFilter() {
	
		filterStatuses = new ArrayList<ReceptionStatus>();

        Date fromDate = null;
        Date toDate = null;

		receptionsFilter = new StatusReceptionsFilter(filterStatuses, fromDate, toDate);
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

        fromWebDateField = new WebDateField();
        JButton clearFromDateButton = new JButton(ResourcesUtil.getIcon("cancel.png"));
        clearFromDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fromWebDateField.setDate(null);
            }
        });


        toWebDateField = new WebDateField();
        JButton clearToDateButton = new JButton(ResourcesUtil.getIcon("cancel.png"));
        clearToDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toWebDateField.setDate(null);
            }
        });



        dataPanel.add(statusesPanelScrollPane, "span, grow");
        dataPanel.add(new JLabel("Начальная дата: "));
        dataPanel.add(fromWebDateField, "w 100");
        dataPanel.add(clearFromDateButton,"wrap");
        dataPanel.add(new JLabel("Конечная дата"));
        dataPanel.add(toWebDateField, "w 100");
        dataPanel.add(clearToDateButton, "wrap");


        return dataPanel;
	}

	@Override 
	protected void loadGUIDataFromReceptionsFilter() {
		StatusReceptionsFilter statusReceptionsFilter = (StatusReceptionsFilter) receptionsFilter;
		this.filterStatuses = statusReceptionsFilter.getStatuses();
        Date fromDate = statusReceptionsFilter.getFromDate();
        Date toDate = statusReceptionsFilter.getToDate();


        for (int i = 0; i < allStatuses.size(); i++) {
			ReceptionStatus status = allStatuses.get(i);
			JCheckBox checkBox = checkBoxes.get(i);
			if(filterStatuses.contains(status)) checkBox.setSelected(true);
			else checkBox.setSelected(false);
		}

        fromWebDateField.setDate(fromDate);
        toWebDateField.setDate(toDate);

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
        StatusReceptionsFilter statusReceptionsFilter = (StatusReceptionsFilter) receptionsFilter;

        Date fromDate = fromWebDateField.getDate();
        Date toDate = toWebDateField.getDate();

        fromDate = DateUtil.toBeginOfDay(fromDate);
        toDate = DateUtil.toEndOfDay(toDate);

        statusReceptionsFilter.setFromDate(fromDate);
        statusReceptionsFilter.setToDate(toDate);


        return true;

	}

}