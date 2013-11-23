package org.kesler.simplereg.logic.realty;

import java.util.List;
import java.util.ArrayList;

import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.DAOListener;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.logic.ModelState;

public class RealtyObjectsModel implements DAOListener {

	private static RealtyObjectsModel instance;

	private List<RealtyObjectsModelStateListener> listeners;

	private List<RealtyObject> realtyObjects;
	private List<RealtyObject> filteredRealtyObjects;
	private String filterString;

	private RealtyObjectsModel() {
		listeners = new ArrayList<RealtyObjectsModelStateListener>();
		DAOFactory.getInstance().getRealtyObjectDAO().addDAOListener(this);
	}

	public static synchronized RealtyObjectsModel getInstance() {
		if (instance == null) {
			instance = new RealtyObjectsModel();
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
		
		realtyObjects = DAOFactory.getInstance().getRealtyObjectDAO().getAllItems();
		notifyListeners(ModelState.UPDATED);

	}

	public void readRealtyObjectsInSeparateThread() {
		Thread readerThread = new Thread(new Runnable() {
			public void run() {
				readRealtyObjects();
			}
		});
		readerThread.start();
	}


	public void readAndFilterRealtyObjectsInSeparateThread(final String filterString) {
		Thread readerThread = new Thread(new Runnable() {
			public void run() {
				readRealtyObjects();
				filterRealtyObjects(filterString);
			}
		});
		readerThread.start();
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
				notifyListeners(ModelState.CONNECTING);
			break;
			
			case READING:
				notifyListeners(ModelState.READING);
			break;
			
			case WRITING:
				notifyListeners(ModelState.WRITING);
			break;
			
			case READY:
				// Ничего не делаем	
			break;
			
			case ERROR:
				notifyListeners(ModelState.ERROR);
			break;				
		}
	}

	private void notifyListeners(ModelState state) {
		for (RealtyObjectsModelStateListener listener: listeners) {
			listener.realtyObjectsModelStateChanged(state);
		}
	}

}