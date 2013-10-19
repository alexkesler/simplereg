package org.kesler.simplereg.gui.reestr.filter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JCheckBox;



import net.miginfocom.swing.MigLayout;
import com.toedter.calendar.JDateChooser;

import org.kesler.simplereg.logic.Service;

public class ByRecordReceptionsFilterDialog extends ReceptionsFilterDialog {

	private Boolean byRecord;
	private JCheckBox byRecordCheckBox;

	public ByRecordReceptionsFilterDialog(JFrame frame) {
		super(frame, "Фильтр по предв записи");
	}

	public ByRecordReceptionsFilterDialog(JFrame frame, ByRecordReceptionsFilter filter) {
		super(frame, "Фильтр по предв записи", filter);
	}

	@Override
	protected void createReceptionsFilter() {
	
		byRecord = new Boolean(true);

		receptionsFilter = new ByRecordReceptionsFilter(byRecord);
	}

	@Override
	protected JPanel createDataPanel() {
		JPanel dataPanel = new JPanel(new MigLayout("fill"));

		byRecordCheckBox = new JCheckBox("По записи");

		dataPanel.add(byRecordCheckBox);
			
		return dataPanel;
	}

	@Override 
	protected void loadGUIDataFromReceptionsFilter() {
		
		ByRecordReceptionsFilter byRecordReceptionsFilter = (ByRecordReceptionsFilter) receptionsFilter;
		byRecord = byRecordReceptionsFilter.getByRecord();
		byRecordCheckBox.setSelected(byRecord);

	}


	@Override
	protected boolean readReceptionsFilterFromGUIData() {

		byRecord = byRecordCheckBox.isSelected();
		ByRecordReceptionsFilter byRecordReceptionsFilter = (ByRecordReceptionsFilter) receptionsFilter;
		byRecordReceptionsFilter.setByRecord(byRecord);

		return true;

	}

}