package org.kesler.simplereg.gui.reestr.filter;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;


import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.Service;

import org.kesler.simplereg.gui.services.ServicesDialogController;


import org.kesler.simplereg.util.ResourcesUtil;


public class ServiceReceptionsFilterDialog extends ReceptionsFilterDialog {

	private List<Service> services;
	private ServicesListModel servicesListModel;
	private int selectedServiceIndex = -1;

	public ServiceReceptionsFilterDialog(JFrame frame) {
		super(frame, "Фильтр по услугам");
	}

	public ServiceReceptionsFilterDialog(JFrame frame, ServiceReceptionsFilter filter) {
		super(frame, "Фильтр по услугам", filter);

	}

	@Override
	protected void createReceptionsFilter() {
	
		services = new ArrayList<Service>();

		receptionsFilter = new ServiceReceptionsFilter(services);
	}

	@Override
	protected JPanel createDataPanel() {
		JPanel dataPanel = new JPanel(new MigLayout("fill, nogrid"));

		servicesListModel = new ServicesListModel();
		final JList servicesList = new JList(servicesListModel);
		servicesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		servicesList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				if(lse.getValueIsAdjusting() == false) {
					selectedServiceIndex = servicesList.getSelectedIndex();	
				}				
			}
		});
		JScrollPane servicesListScrollPane = new JScrollPane(servicesList);

		JButton addServiceButton = new JButton();
		addServiceButton.setIcon(ResourcesUtil.getIcon("add.png"));
		addServiceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				
				Service service = ServicesDialogController.getInstance().openSelectDialog(currentDialog); 
				
				if(service != null) {

					services.add(service);
					servicesListModel.serviceAdded(services.size()-1);
				}
			}
		});


		JButton removeServiceButton = new JButton();
		removeServiceButton.setIcon(ResourcesUtil.getIcon("delete.png"));
		removeServiceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (selectedServiceIndex != -1) {
					services.remove(selectedServiceIndex);
					servicesListModel.serviceRemoved(selectedServiceIndex);
				} else {
					JOptionPane.showMessageDialog(currentDialog, "Ничего не выбрано", "Ошибка", JOptionPane.ERROR_MESSAGE);
				}
			}
		});


		dataPanel.add(servicesListScrollPane, "growx, wrap, w 400, h 150");
		dataPanel.add(addServiceButton, "span");
		dataPanel.add(removeServiceButton, "wrap");
			
		return dataPanel;
	}

	@Override 
	protected void loadGUIDataFromReceptionsFilter() {
		

	}


	@Override
	protected boolean readReceptionsFilterFromGUIData() {

		if (services.size() == 0) {
			JOptionPane.showMessageDialog(currentDialog, "Не выбрано ни одной услуги", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;
		}


		return true;

	}

	class ServicesListModel extends AbstractListModel {

		ServicesListModel() {
			ServiceReceptionsFilter serviceReceptionsFilter = (ServiceReceptionsFilter) receptionsFilter;
			services = serviceReceptionsFilter.getServices();
		}

		@Override
		public int getSize() {
			return services.size();
		}

		@Override
		public String getElementAt(int index) {
			return services.get(index).toString();
		}

		public void serviceAdded(int index) {
			fireIntervalAdded(this, index,index);
		}

		public void serviceRemoved(int index) {
			fireIntervalRemoved(this, index, index);
		}

	}

}