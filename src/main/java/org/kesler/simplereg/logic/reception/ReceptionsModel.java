package org.kesler.simplereg.logic.reception;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import java.sql.SQLException;

import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.DAOListener;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.util.OptionsUtil;
import org.kesler.simplereg.gui.reestr.filter.ReceptionsFilter;
import org.kesler.simplereg.logic.ModelState;

public class ReceptionsModel implements DAOListener{
	private static ReceptionsModel instance = null;
	private List<Reception> allReceptions;
	private List<Reception> filteredReceptions;

	private List<ReceptionsModelStateListener> listeners;

	private ReceptionsModel() {
		allReceptions = new ArrayList<Reception>();
		filteredReceptions = new ArrayList<Reception>();
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


	public List<Reception> getAllReceptions() {
		return allReceptions;
	}


	public List<Reception> getFilteredReceptions() {
		return filteredReceptions;
	}


	// читаем данные из БД
	public void readReceptions() {
		allReceptions = DAOFactory.getInstance().getReceptionDAO().getAllReceptions();
		notifyListeners(ModelState.UPDATED);
	}

	public void readReceptionsInSeparateThread() {
		Thread readerThread = new Thread(new Runnable() {
			public void run() {
				readReceptions();
			}
		});
		readerThread.start();
	}

	// применяем фильтры
	public void applyFilters(List<ReceptionsFilter> filters) {
		filteredReceptions = new ArrayList<Reception>();
		notifyListeners(ModelState.FILTERING);
		for (Reception reception: allReceptions) {
			boolean fit = true;		
			for (ReceptionsFilter filter: filters) {
				if (!filter.checkReception(reception)) fit = false;
			}
			if (fit) filteredReceptions.add(reception);
		}
		notifyListeners(ModelState.FILTERED);

	}

	public void applyFiltersInSeparateThread(final List<ReceptionsFilter> filters) {
		Thread filterThread = new Thread(new Runnable() {
			public void run() {
				readReceptions();
				applyFilters(filters);
			}
		});
		filterThread.start();
	}



	public void addReception(Reception reception) {
		reception.setStatus(ReceptionStatusesModel.getInstance().getInitReceptionStatus());

			//DAOFactory.getInstance().getApplicatorDAO().addApplicator(reception.getApplicator());
		DAOFactory.getInstance().getReceptionDAO().addReception(reception);
		allReceptions.add(reception);
	}

	public void updateReception(Reception reception) {
		DAOFactory.getInstance().getReceptionDAO().updateReception(reception);
	}

	public void removeReception(Reception reception) {
		DAOFactory.getInstance().getReceptionDAO().removeReception(reception);
		allReceptions.remove(reception);
		filteredReceptions.remove(reception);
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
				notifyListeners(ModelState.READY);
			break;
			
			case ERROR:
				notifyListeners(ModelState.ERROR);
			break;				
		}
	}

	private void notifyListeners(ModelState state) {
		for (ReceptionsModelStateListener listener: listeners) {
			listener.receptionsModelStateChanged(state);
		}
	} 

}

