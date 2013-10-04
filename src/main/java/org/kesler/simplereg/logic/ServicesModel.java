package org.kesler.simplereg.logic;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import org.kesler.simplereg.dao.DAOFactory;

/**
* Реализует методы доступа к услугам, сохраненным в базе данных, хранит список услуг, прочитанный из базы, записывает изменения в базу
*/
public class ServicesModel {
	private static ServicesModel instance = null;
	private List<Service> services = null;

	private ServicesModel() {
		services = new ArrayList<Service>();
	}

	public static synchronized ServicesModel getInstance() {
		if (instance == null) {
			instance = new ServicesModel();
		}
		return instance;
	}

    /**
    * Читает услуги из базы данных во внутренний список
    */
	public void readServices() {

		services = DAOFactory.getInstance().getServiceDAO().getAllServices();					

	}

	/**
	* Возвращает список всех услуг, сохраненных в внутреннем списке, если внутренний список не определен - читает его из базы
	*/
	public List<Service> getAllServices() {
//		if (services == null) {
			readServices();
//		}
		return services;
	}

	/**
	* Добавляет услугу, сохраняет её в базу данных
	*/
	public void addService(Service service) {
		services.add(service);
		DAOFactory.getInstance().getServiceDAO().addService(service);							
	}

	/**
	* Обновляет услугу в базе данных
	*/
	public void updateService(Service service) {
		DAOFactory.getInstance().getServiceDAO().updateService(service);
	}


	/**
	* Удаляет услугу из базы данных
	*/
	public void deleteService(Service service) {
		DAOFactory.getInstance().getServiceDAO().deleteService(service);
	}

	public void saveServices(List<Service> services) {
		
	}


}