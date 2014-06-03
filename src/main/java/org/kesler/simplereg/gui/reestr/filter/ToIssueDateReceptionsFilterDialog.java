package org.kesler.simplereg.gui.reestr.filter;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Calendar;

import net.miginfocom.swing.MigLayout;
import com.alee.extended.date.WebDateField;

import org.kesler.simplereg.logic.reception.filter.ToIssueDateReceptionsFilter;
import org.kesler.simplereg.util.DateUtil;
import org.kesler.simplereg.util.ResourcesUtil;

public class ToIssueDateReceptionsFilterDialog extends ReceptionsFilterDialog {

	private WebDateField fromWebDateField;
	private WebDateField toWebDateField;

	public ToIssueDateReceptionsFilterDialog(JFrame frame) {
		super(frame, "Фильтр по дате на выдачу");
	}

	public ToIssueDateReceptionsFilterDialog(JFrame frame, ToIssueDateReceptionsFilter filter) {
		super(frame, "Фильтр по дате на выдачу", filter);
	}

	@Override
	protected void createReceptionsFilter() {


		Date fromDate = DateUtil.toBeginOfDay(new Date());
		Date toDate = DateUtil.toEndOfDay(new Date());

		receptionsFilter = new ToIssueDateReceptionsFilter(fromDate, toDate);
	}

	@Override
	protected JPanel createDataPanel() {
		JPanel dataPanel = new JPanel(new MigLayout("fill"));

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

		ToIssueDateReceptionsFilter toIssueDateReceptionsFilter = (ToIssueDateReceptionsFilter) receptionsFilter;
		Date fromDate = toIssueDateReceptionsFilter.getFromDate();
		Date toDate = toIssueDateReceptionsFilter.getToDate();

		fromWebDateField.setDate(fromDate);
		toWebDateField.setDate(toDate);
	}


	@Override
	protected boolean readReceptionsFilterFromGUIData() {

		Date fromDate = fromWebDateField.getDate();
		Date toDate = toWebDateField.getDate();

        fromDate = DateUtil.toBeginOfDay(fromDate);
        toDate = DateUtil.toEndOfDay(toDate);

        if(fromDate == null && toDate == null) {
			JOptionPane.showMessageDialog(this, "Необходимо задать хотя бы одну дату", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		ToIssueDateReceptionsFilter toIssueDateReceptionsFilter = (ToIssueDateReceptionsFilter) receptionsFilter;
		toIssueDateReceptionsFilter.setFromDate(fromDate);
		toIssueDateReceptionsFilter.setToDate(toDate);

		return true;

	}

}