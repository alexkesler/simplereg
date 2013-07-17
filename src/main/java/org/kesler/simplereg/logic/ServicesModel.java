package org.kesler.simplereg.logic;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import org.kesler.simplereg.dao.DAOFactory;


public class ServicesModel {
	private static ServicesModel instance = null;
	private List<Service> services = null;

	private ServicesModel() {
		readServices();
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
			JOptionPane.showMessageDialog(null,sqle.getMessage(),"Ошибка чтения услуг из базы данных", JOptionPane.OK_OPTION);
		}		
	}

	public List<Service> getAllServices() {
		if (services == null) {
			readServices();
		}
		return services;
	}

	public void addService(Service service) {
		services.add(service);
		try {
			DAOFactory.getInstance().getServiceDAO().addService(service);					
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null,sqle.getMessage(),"Ошибка сохранения услуги в базу данных", JOptionPane.OK_OPTION);
			services.remove(service);
		}		

	}

}