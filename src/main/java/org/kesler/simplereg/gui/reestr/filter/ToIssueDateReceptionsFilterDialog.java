package org.kesler.simplereg.gui.reestr.filter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.util.Date;
import java.util.Calendar;

import net.miginfocom.swing.MigLayout;
import com.alee.extended.date.WebDateField;

import org.kesler.simplereg.logic.reception.filter.ToIssueDateReceptionsFilter;

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

		receptionsFilter = new ToIssueDateReceptionsFilter(fromDate, toDate);
	}

	@Override
	protected JPanel createDataPanel() {
		JPanel dataPanel = new JPanel(new MigLayout("fill"));

		fromWebDateField = new WebDateField();

		toWebDateField = new WebDateField();

		dataPanel.add(new JLabel("Начальная дата: "));
		dataPanel.add(fromWebDateField, "w 100, wrap");
		dataPanel.add(new JLabel("Конечная дата"));
		dataPanel.add(toWebDateField, "w 100, wrap");

		

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