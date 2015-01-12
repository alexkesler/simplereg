package org.kesler.simplereg.logic.service;

import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.DAOListener;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.logic.ServiceState;
import org.kesler.simplereg.logic.Service;

/**
* Реализует методы доступа к услугам, сохраненным в базе данных, хранит список услуг, прочитанный из базы, записывает изменения в базу
*/
public class ServicesModel implements DAOListener{
    private Logger log;
	private static ServicesModel instance = null;
	private List<Service> services = null;
    private List<Service> activeServices = null;

	private List<ServicesModelListener> listeners; // список слушателей для оповещения о событиях с моделью

	private ServicesModel() {
        log = Logger.getLogger(getClass().getSimpleName());
		services = new ArrayList<Service>();
        activeServices = new ArrayList<Service>();
		DAOFactory.getInstance().getServiceDAO().addDAOListener(this);
		listeners = new ArrayList<ServicesModelListener>();
	}

	public static synchronized ServicesModel getInstance() {
		if (instance == null) {
			instance = new ServicesModel();
		}
		return instance;
	}


	public void addServicesModelListener(ServicesModelListener listener) {
		listeners.add(listener);
	}

    /**
    * Читает услуги из базы данных во внутренний список
    */
	public void readServices() {
        log.info("Reading services from DB");
		services = DAOFactory.getInstance().getServiceDAO().getAllItems();
        activeServices.clear();
        for (Service service:services) {
            if (service.getEnabled()!=null && service.getEnabled()) activeServices.add(service);
        }
        log.info("Read " + services.size() + " services");
		notifyListeners(ServiceState.UPDATED);
	}

    /**
    * Читает услуги из базы данных во внутренний список в отдельном потоке
    * @return ID потока
    */
	public void readServicesInSeparateProcess() {
		Thread readServicesThread = new Thread(new Runnable() {
			public void run() {
				readServices();
			}
		});

		readServicesThread.start();

	}

	/**
	* Прерывает процесс 
	* @param thread ID процесса, который необходимо прервать
	*/

	public void cancelProcess(Thread thread) {
		thread.interrupt();
	}

	/**
	* Возвращает список всех услуг, сохраненных в внутреннем списке, если внутренний список не определен - читает его из базы
	*/
	public List<Service> getAllServices() {
		if (services.size()==0) {
			readServices();
		}
		return services;
	}

	public List<Service> getActiveServices() {
        if (activeServices.size()==0) {
            readServices();
        }
		return activeServices;
	}

	/**
	* Добавляет услугу, сохраняет её в базу данных
	*/
	public void addService(Service service) {
        log.info("Adding service: " + service.getName());
		services.add(service);
		DAOFactory.getInstance().getServiceDAO().addItem(service);
        log.info("Adding service complete");
	}

	/**
	* Обновляет услугу в базе данных
	*/
	public void updateService(Service service) {
        log.info("Updating service: " + service.getName());
		DAOFactory.getInstance().getServiceDAO().updateItem(service);
        log.info("Updating service complete");
	}


	/**
	* Удаляет услугу из базы данных
	*/
	public void deleteService(Service service) {
        log.info("Removing service: " + service.getName());
        services.remove(service);
		DAOFactory.getInstance().getServiceDAO().removeItem(service);
        log.info("removing service complete");
	}

	/**
	* получает оповещения от DAO оповещает своих слушателей о состоянии чтения данных
	*/
	@Override
	public void daoStateChanged(DAOState state) {
		switch (state) {
			case CONNECTING:
				notifyListeners(ServiceState.CONNECTING);
			break;
			case READING:
				notifyListeners(ServiceState.READING);
			break;
			case WRITING:
				notifyListeners(ServiceState.WRITING);
			break;
			case READY:
				notifyListeners(ServiceState.READY);
			break;
			case ERROR:
				notifyListeners(ServiceState.ERROR);
			break;
		}
	}

	private void notifyListeners(ServiceState state) {
		for (ServicesModelListener listener: listeners) {
			listener.modelStateChanged(state);
		}
	}

}