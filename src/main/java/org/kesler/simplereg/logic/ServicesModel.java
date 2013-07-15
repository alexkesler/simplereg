package org.kesler.simplereg.logic;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import org.kesler.simplereg.dao.DAOFactory;


public class ServicesModel {
	private static ServicesModel instance;
	private List<Service> services;

	private ServicesModel() {
		services = new ArrayList<Service>();
	}

	public static synchronized ServicesModel getInstance() {
		if (instance == null) {
			instance = new ServicesModel();
		}
		return instance;
	}

	public void readServices() {

		try {
			services = DAOFactory.getInstance().getServiceDAO().getAllServices();					
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null,sqle.getMessage(),"Ошибка чтения из базы данных", JOptionPane.OK_OPTION);
		}		
	}

	public List<Service> getAllServices() {
		return services;
	}

	public void addService(Service service) {
		services.add(service);
	}

}