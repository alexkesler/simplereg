package org.kesler.simplereg.logic.reception;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import java.sql.SQLException;

import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.DAOListener;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.util.OptionsUtil;


public class ReceptionsModel implements DAOListener{
	private static ReceptionsModel instance = null;
	private List<Reception> receptions;

	private List<ReceptionsModelStateListener> listeners;

	private ReceptionsModel() {
		receptions = new ArrayList<Reception>();
		listeners = new ArrayList<ReceptionsModelStateListener>();
		DAOFactory.getInstance().getReceptionDAO().addDAOListener(this);
	}

	public void addReceptionsModelStateListener(ReceptionsModelStateListener listener) {
		listeners.add(listener);
	}

	public static synchronized ReceptionsModel getInstance() {
		if (instance == null) {
			instance = new ReceptionsModel();
		}
		return instance;
	}


	public List<Reception> getReceptions() {
		return receptions;
	}

	// читаем данные из БД
	public void readReceptionsFromDB() {
		receptions = DAOFactory.getInstance().getReceptionDAO().getAllReceptions();
		notifyListeners(ReceptionsModelState.UPDATED);
	}

	public void addReception(Reception reception) {
		reception.setStatus(ReceptionStatusesModel.getInstance().getInitReceptionStatus());

			//DAOFactory.getInstance().getApplicatorDAO().addApplicator(reception.getApplicator());
		DAOFactory.getInstance().getReceptionDAO().addReception(reception);
		receptions.add(reception);
	}

	/**
	* Реализует интерфейс {@link org.kesler.simplereg.dao.DAOListener}
	*/
	@Override
	public void daoStateChanged(DAOState state) {
		switch (state) {
			case CONNECTING:
				notifyListeners(ReceptionsModelState.CONNECTING);
			break;
			
			case READING:
				notifyListeners(ReceptionsModelState.READING);
			break;
			
			case WRITING:
				notifyListeners(ReceptionsModelState.WRITING);
			break;
			
			case READY:
				// Ничего не делаем	
			break;
			
			case ERROR:
				notifyListeners(ReceptionsModelState.ERROR);
			break;				
		}
	}

	private void notifyListeners(ReceptionsModelState state) {
		for (ReceptionsModelStateListener listener: listeners) {
			listener.receptionsModelStateChanged(state);
		}
	} 

}

