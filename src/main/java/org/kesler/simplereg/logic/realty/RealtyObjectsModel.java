package org.kesler.simplereg.logic.realty;

import java.util.List;
import java.util.ArrayList;

import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.DAOListener;
import org.kesler.simplereg.dao.DAOState;

public class RealtyObjectsModel implements DAOListener {

	private static RealtyObjectsModel instance;

	private List<RealtyObjectsModelStateListener> listeners;

	private List<RealtyObject> realtyObjects;

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
			readRealtyObjectsFromDB();
		}

		return realtyObjects;

	}

	public void readRealtyObjectsFromDB() {
		
		realtyObjects = DAOFactory.getInstance().getRealtyObjectDAO().getAllItems();
		notifyListeners(RealtyObjectsModelState.UPDATED);

	}

	/**
	* Реализует интерфейс {@link org.kesler.simplereg.dao.DAOListener}
	*/
	@Override
	public void daoStateChanged(DAOState state) {
		switch (state) {
			case CONNECTING:
				notifyListeners(RealtyObjectsModelState.CONNECTING);
			break;
			
			case READING:
				notifyListeners(RealtyObjectsModelState.READING);
			break;
			
			case WRITING:
				notifyListeners(RealtyObjectsModelState.WRITING);
			break;
			
			case READY:
				// Ничего не делаем	
			break;
			
			case ERROR:
				notifyListeners(RealtyObjectsModelState.ERROR);
			break;				
		}
	}

	private void notifyListeners(RealtyObjectsModelState state) {
		for (RealtyObjectsModelStateListener listener: listeners) {
			listener.realtyObjectsModelStateChanged(state);
		}
	}

}