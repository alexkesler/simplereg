package org.kesler.simplereg.logic;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.DAOListener;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.logic.service.ServicesModelState;
import org.kesler.simplereg.logic.service.ServicesModelListener;

/**
* Реализует методы доступа к услугам, сохраненным в базе данных, хранит список услуг, прочитанный из базы, записывает изменения в базу
*/
public class ServicesModel implements DAOListener{
	private static ServicesModel instance = null;
	private List<Service> services = null;

	private List<ServicesModelListener> listeners; // список слушателей для оповещения о событиях с моделью

	private ServicesModel() {
		services = new ArrayList<Service>();
		DAOFactory.getInstance().getServiceDAO().addDAOListener(this);
		threads = new ArrayList<Thread>();
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
		services = DAOFactory.getInstance().getServiceDAO().getAllServices();
		notifyListeners(ServicesModelState.UPDATED);	
	}

    /**
    * Читает услуги из базы данных во внутренний список в отдельном потоке
    * @return ID потока
    */
	public void readServicesInProcess() {
		Thread readServicesThread = new Thread(new Runnable() {
			public void run() {
				readServices();
			}
		});

		readServicesThread.start();

	}

	/**
	* Прерывает процесс 
	* @param threadId ID процесса, который необходимо прервать
	*/

	public void cancelProcess(Thread thread) {
		thread.interrupt();
	}

	/**
	* Возвращает список всех услуг, сохраненных в внутреннем списке, если внутренний список не определен - читает его из базы
	*/
	public List<Service> getAllServices() {
		if (services == null) {
			readServices();
		}
		return services;
	}

	public List<Service> getActiveServces() {
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

	/**
	* получает оповещения от DAO оповещает своих слушателей о состоянии чтения данных
	*/
	@Override
	public void daoStateChanged(DAOState state) {
		switch (state) {
			case CONNECTING:
				notifyListeners(ServicesModelState.CONNECTING);
			break;
			case READING:
				notifyListeners(ServicesModelState.READING);
			break;
			case READY:
			// ничего не делаем, статус объявится при получении списка 
			break;
			case ERROR:
				notifyListeners(ServicesModelState.ERROR);
			break;
		}
	}

	private void notifyListeners(ServicesModelState state) {
		for (ServicesModelListener listener: listeners) {
			listener.modelStateChanged(state);
		}
	}

}