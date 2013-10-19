package org.kesler.simplereg.gui.reestr.filter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.util.Date;
import java.util.Calendar;

import net.miginfocom.swing.MigLayout;
import com.toedter.calendar.JDateChooser;


public class OpenDateReceptionsFilterDialog extends ReceptionsFilterDialog {

	private JDateChooser fromDateChooser;
	private JDateChooser toDateChooser;

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

		fromDateChooser = new JDateChooser();

		toDateChooser = new JDateChooser();

		dataPanel.add(new JLabel("Начальная дата: "));
		dataPanel.add(fromDateChooser, "w 100, wrap");
		dataPanel.add(new JLabel("Конечная дата"));
		dataPanel.add(toDateChooser, "w 100, wrap");

		

		return dataPanel;
	}

	@Override 
	protected void loadGUIDataFromReceptionsFilter() {

		OpenDateReceptionsFilter openDateReceptionsFilter = (OpenDateReceptionsFilter) receptionsFilter;
		Date fromDate = openDateReceptionsFilter.getFromDate();
		Date toDate = openDateReceptionsFilter.getToDate();

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

		OpenDateReceptionsFilter openDateReceptionsFilter = (OpenDateReceptionsFilter) receptionsFilter;
		openDateReceptionsFilter.setFromDate(fromDate);
		openDateReceptionsFilter.setToDate(toDate);

		return true;

	}

}