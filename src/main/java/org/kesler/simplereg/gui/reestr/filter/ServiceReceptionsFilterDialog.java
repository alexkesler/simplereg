package org.kesler.simplereg.gui.reestr.filter;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


import net.miginfocom.swing.MigLayout;
import com.toedter.calendar.JDateChooser;

import org.kesler.simplereg.logic.Service;

public class ServiceReceptionsFilterDialog extends ReceptionsFilterDialog {

	private List<Service> services;

	public ServiceReceptionsFilterDialog(JFrame frame) {
		super(frame, "Фильтр по услугам");
	}

	public ServiceReceptionsFilterDialog(JFrame frame, ServiceReceptionsFilter filter) {
		super(frame, "Фильтр по услугам", filter);
		this.services = filter.getServices();
	}

	@Override
	protected void createReceptionsFilter() {
	
		services = new ArrayList<Service>();

		receptionsFilter = new ServiceReceptionsFilter(services);
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