package org.kesler.simplereg.gui.reestr.filter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.util.Date;
import java.util.Calendar;

import net.miginfocom.swing.MigLayout;
import com.toedter.calendar.JDateChooser;


public class ToIssueDateReceptionsFilterDialog extends ReceptionsFilterDialog {

	private JDateChooser fromDateChooser;
	private JDateChooser toDateChooser;

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

		fromDateChooser = new JDateChooser();

		toDateChooser = new JDateChooser();

		dataPanel.add(new JLabel("Начальная дата: "));
		dataPanel.add(fromDateChooser, "wrap");
		dataPanel.add(new JLabel("Конечная дата"));
		dataPanel.add(toDateChooser, "wrap");

		

		return dataPanel;
	}

	@Override 
	protected void loadGUIDataFromReceptionsFilter() {

		ToIssueDateReceptionsFilter toIssueDateReceptionsFilter = (ToIssueDateReceptionsFilter) receptionsFilter;
		Date fromDate = toIssueDateReceptionsFilter.getFromDate();
		Date toDate = toIssueDateReceptionsFilter.getToDate();

		fromDateChooser.setDate(fromDate);
		toDateChooser.setDate(toDate);
	}


	@Override
	protected boolean readReceptionsFilterFromGUIData() {

		Date fromDate = fromDateChooser.getDate();
		Date toDate = toDateChooser.getDate();

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