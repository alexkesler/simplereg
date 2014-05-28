package org.kesler.simplereg.gui.reestr.filter;

import com.alee.extended.date.WebDateField;
import net.miginfocom.swing.MigLayout;
import org.kesler.simplereg.logic.reception.filter.StatusChangeDateReceptionsFilter;
import org.kesler.simplereg.logic.reception.filter.ToIssueDateReceptionsFilter;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alex on 28.05.14.
 */
public class StatusChangeDateReceptionsFilterDialog extends ReceptionsFilterDialog{
    private WebDateField fromWebDateField;
    private WebDateField toWebDateField;

    public StatusChangeDateReceptionsFilterDialog(JFrame frame) {
        super(frame, "Фильтр по дате смены состояния");
    }

    public StatusChangeDateReceptionsFilterDialog(JFrame frame, StatusChangeDateReceptionsFilter filter) {
        super(frame, "Фильтр по дате смены состояния", filter);
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

        receptionsFilter = new StatusChangeDateReceptionsFilter(fromDate, toDate);
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

        StatusChangeDateReceptionsFilter statusChangeDateReceptionsFilter = (StatusChangeDateReceptionsFilter) receptionsFilter;
        Date fromDate = statusChangeDateReceptionsFilter.getFromDate();
        Date toDate = statusChangeDateReceptionsFilter.getToDate();

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

        StatusChangeDateReceptionsFilter statusChangeDateReceptionsFilter = (StatusChangeDateReceptionsFilter) receptionsFilter;
        statusChangeDateReceptionsFilter.setFromDate(fromDate);
        statusChangeDateReceptionsFilter.setToDate(toDate);

        return true;

    }


}
