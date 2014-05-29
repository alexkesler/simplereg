package org.kesler.simplereg.gui.reestr.filter;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Calendar;

import net.miginfocom.swing.MigLayout;
import com.alee.extended.date.WebDateField;

import org.kesler.simplereg.logic.reception.filter.OpenDateReceptionsFilter;
import org.kesler.simplereg.util.DateUtil;
import org.kesler.simplereg.util.ResourcesUtil;

public class OpenDateReceptionsFilterDialog extends ReceptionsFilterDialog {

	private WebDateField fromWebDateField;
	private WebDateField toWebDateField;

	public OpenDateReceptionsFilterDialog(JFrame frame) {
		super(frame, "Фильтр по дате открытия");
	}

	public OpenDateReceptionsFilterDialog(JFrame frame, OpenDateReceptionsFilter filter) {
		super(frame, "Фильтр по дате открытия", filter);
	}

	@Override
	protected void createReceptionsFilter() {
	
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		Calendar toCalendar = Calendar.getInstance();
		toCalendar.set(Calendar.HOUR_OF_DAY, 23);
		fromCalendar.set(Calendar.MINUTE, 59);
		fromCalendar.set(Calendar.SECOND, 59);

		Date fromDate = fromCalendar.getTime();
		Date toDate = toCalendar.getTime();

		receptionsFilter = new OpenDateReceptionsFilter(fromDate, toDate);
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

		OpenDateReceptionsFilter openDateReceptionsFilter = (OpenDateReceptionsFilter) receptionsFilter;
		Date fromDate = openDateReceptionsFilter.getFromDate();
		Date toDate = openDateReceptionsFilter.getToDate();
        // обнуление времени для выбранной даты

        fromDate = DateUtil.toBeginOfDay(fromDate);
        toDate = DateUtil.toEndOfDay(toDate);

		fromWebDateField.setDate(fromDate);
		toWebDateField.setDate(toDate);
	}


	@Override
	protected boolean readReceptionsFilterFromGUIData() {

		Date fromDate = fromWebDateField.getDate();
		Date toDate = toWebDateField.getDate();


		if(fromDate == null && toDate == null) {
			JOptionPane.showMessageDialog(this, "Необходимо задать хотя бы одну дату", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		OpenDateReceptionsFilter openDateReceptionsFilter = (OpenDateReceptionsFilter) receptionsFilter;
		openDateReceptionsFilter.setFromDate(fromDate);
		openDateReceptionsFilter.setToDate(toDate);

		return true;

	}

}