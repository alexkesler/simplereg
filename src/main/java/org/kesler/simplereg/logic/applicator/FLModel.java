package org.kesler.simplereg.logic.applicator;

import java.util.List;
import java.util.ArrayList;

import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.DAOListener;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.logic.ModelState;

public class FLModel implements DAOListener{
	private List<FL> flList;
	private List<FL> filteredFLList;
	private String filterString;
	private static FLModel instance = null;

	private List<FLModelStateListener> listeners;

	public static synchronized FLModel getInstance() {
		if (instance == null) {
			instance = new FLModel();
		}
		return instance;
	}

	private FLModel() {

		flList = new ArrayList<FL>();
		filteredFLList = new ArrayList<FL>();
		filterString = "";
		listeners = new ArrayList<FLModelStateListener>();

	}

	public void addFLModelStateListener(FLModelStateListener listener) {
		listeners.add(listener);
	}

	public List<FL> getAllFLs() {

		return flList;

	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	public void filterFLs() {
		
		// если строка фильтра не пустая - пересоздаем фильтрованный список
		if (!filterString.isEmpty()) {
			filteredFLList = new ArrayList<FL>();
			for (FL fl: flList) {
				if (fl.getSurName().toLowerCase().indexOf(filterString.toLowerCase(),0) == 0) {
					filteredFLList.add(fl);
				}
			}
		} else {
			filteredFLList = flList;
		}
		notifyListeners(ModelState.FILTERED);
	}

	public void filterFLsInSeparateThread() {
		Thread filterer = new Thread(new Runnable() {
			public void run() {
				filterFLs();
			}
		});
		filterer.start();
	}

	public List<FL> getFilteredFLs() {

		return filteredFLList;
		
	}

	public int addFL(FL fl) {
		Long id = DAOFactory.getInstance().getFLDAO().addFL(fl);
		if (id != null) {
			flList.add(fl);
			return flList.size()-1;
		} else {
			return -1;
		}
		
	}

	public void updateFL(FL fl) {
		DAOFactory.getInstance().getFLDAO().updateFL(fl);
	}

	public void deleteFL(FL fl) {
		DAOFactory.getInstance().getFLDAO().deleteFL(fl);
		flList.remove(fl);
	}

	public void readLFs() {
		flList = DAOFactory.getInstance().getFLDAO().getAllFLs();
		notifyListeners(ModelState.UPDATED);
	}

	public void readFLsInSeparateThread() {
		Thread reader = new Thread(new Runnable() {
			public void run() {
				readLFs();
				filterFLs();
			}
		});

		reader.start();
	}

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
		for (FLModelStateListener listener : listeners) {
			listener.flModelStateChanged(state);			
		}
	}


}