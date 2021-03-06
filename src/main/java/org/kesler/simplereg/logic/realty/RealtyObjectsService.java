package org.kesler.simplereg.logic.realty;

import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.kesler.simplereg.logic.RealtyObject;
import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.DAOListener;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.logic.ServiceState;

public class RealtyObjectsService implements DAOListener {
    private Logger log;

	private static RealtyObjectsService instance;

	private List<RealtyObjectsModelStateListener> listeners;

	private List<RealtyObject> realtyObjects;
	private List<RealtyObject> filteredRealtyObjects;
	private String filterString;

	private RealtyObjectsService() {
        log = Logger.getLogger(getClass().getSimpleName());
		listeners = new ArrayList<RealtyObjectsModelStateListener>();
        realtyObjects = new ArrayList<RealtyObject>();
		DAOFactory.getInstance().getRealtyObjectDAO().addDAOListener(this);
	}

	public static synchronized RealtyObjectsService getInstance() {
		if (instance == null) {
			instance = new RealtyObjectsService();
		}
		return instance;
	}

	public void addRealtyObjectsModelStateListener(RealtyObjectsModelStateListener listener) {
		listeners.add(listener);
	}


	public List<RealtyObject> getAllRealtyObjects() {
		if (realtyObjects == null) {
			readRealtyObjects();
		}

		return realtyObjects;

	}

	public List<RealtyObject> getFilteredRealtyObjects() {
		if (filteredRealtyObjects == null) {
			filterRealtyObjects("");
		}
		return filteredRealtyObjects;

	}

	public void readRealtyObjects() {

        log.info("Reading RealtyObjects");
		realtyObjects = DAOFactory.getInstance().getRealtyObjectDAO().getAllItems();
		notifyListeners(ServiceState.UPDATED);
        log.info("Read complete");

	}

	public void filterRealtyObjects(String filterString) {
		this.filterString = filterString;
		filteredRealtyObjects = getAllRealtyObjects();

		if (!filterString.isEmpty()) {
			filteredRealtyObjects = new ArrayList<RealtyObject>();
			for (RealtyObject realtyObject: realtyObjects) {
				if (realtyObject.toString().toLowerCase().indexOf(filterString.toLowerCase()) != -1) {
					filteredRealtyObjects.add(realtyObject);
				}
			}
		}

	}

	public int addRealtyObject(RealtyObject realtyObject) {

		int index = -1;
		
		DAOFactory.getInstance().getRealtyObjectDAO().addItem(realtyObject);
		// нам нужен индекс от локального списка
		realtyObjects.add(realtyObject);
		filterRealtyObjects("");
		index = realtyObjects.size()-1;

		return index;

	}


	public void updateRealtyObject(RealtyObject realtyObject) {
		DAOFactory.getInstance().getRealtyObjectDAO().updateItem(realtyObject);
	}


	public void removeRealtyObject(RealtyObject realtyObject) {
		DAOFactory.getInstance().getRealtyObjectDAO().removeItem(realtyObject);
		realtyObjects.remove(realtyObject);
	}

	/**
	* Реализует интерфейс {@link org.kesler.simplereg.dao.DAOListener}
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
				// Ничего не делаем	
			break;
			
			case ERROR:
				notifyListeners(ServiceState.ERROR);
			break;				
		}
	}

	private void notifyListeners(ServiceState state) {
		for (RealtyObjectsModelStateListener listener: listeners) {
			listener.realtyObjectsModelStateChanged(state);
		}
	}

}