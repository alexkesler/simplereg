package org.kesler.simplereg.gui.reestr.filter;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


import net.miginfocom.swing.MigLayout;
import com.toedter.calendar.JDateChooser;

import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.reception.ReceptionStatusesModel;

public class StatusReceptionsFilterDialog extends ReceptionsFilterDialog {


	public StatusReceptionsFilterDialog(JFrame frame) {
		super(frame, "Фильтр по состонию");
	}

	public StatusReceptionsFilterDialog(JFrame frame, StatusReceptionsFilter filter) {
		super(frame, "Фильтр по состоянию", filter);
	}

	@Override
	protected void createReceptionsFilter() {
	
		List<ReceptionStatus> statuses = new ArrayList<ReceptionStatus>();

		receptionsFilter = new StatusReceptionsFilter(statuses);
	}

	@Override
	protected JPanel createDataPanel() {
		JPanel dataPanel = new JPanel(new MigLayout("fill"));
			
		return dataPanel;
	}

	@Override 
	protected void loadGUIDataFromReceptionsFilter() {

	}


	@Override
	protected boolean readReceptionsFilterFromGUIData() {

		return true;

	}

}