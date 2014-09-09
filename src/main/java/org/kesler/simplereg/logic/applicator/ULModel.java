package org.kesler.simplereg.logic.applicator;

import java.util.List;
import java.util.ArrayList;

import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.DAOListener;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.logic.ModelState;

import org.kesler.simplereg.logic.UL;

public class ULModel implements DAOListener {
	private List<UL> ulList;
	private List<UL> filteredULList;
	private String filterString;

	private static ULModel instance = null;

	private List<ULModelStateListener> listeners;

	public static synchronized ULModel getInstance() {
		if (instance == null) {
			instance = new ULModel();
		}
		return instance;
	}

	private ULModel() {
		ulList = new ArrayList<UL>();
		filteredULList = new ArrayList<UL>();
		filterString = "";
		listeners = new ArrayList<ULModelStateListener>();
	}

	public void addULModelStateListener(ULModelStateListener listener) {
		listeners.add(listener);
	}

	public List<UL> getAllULs() {
		return ulList;
	}

    public List<UL> getULsByShortName(String shortName) {
        return DAOFactory.getInstance().getULDAO().getULsByShortName(shortName);
    }

    public List<UL> getULsByFullName(String fullName) {
        return DAOFactory.getInstance().getULDAO().getULsByFullName(fullName);
    }

	public void readULs() {
		ulList = DAOFactory.getInstance().getULDAO().getAllItems();
		notifyListeners(ModelState.UPDATED);
	}

	public void readULsInSeparateThread() {
		Thread reader = new Thread(new Runnable() {
			public void run() {
				readULs();
				filterULs();
			}
		});
		reader.start();
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	public void filterULs() {

		if (!filterString.isEmpty()) {
			filteredULList = new ArrayList<UL>();
			for (UL ul: ulList) {
				if (ul.getShortName().toLowerCase().indexOf(filterString.toLowerCase(),0) != -1) {
					filteredULList.add(ul);
				}
			}
		} else {
			filteredULList = ulList;
		}

		notifyListeners(ModelState.FILTERED);
	}

	public void filterULsInSeparateThread() {
		Thread filterer = new Thread(new Runnable() {
			public void run() {
				filterULs();
			}
		});
		filterer.start();
	}

	public List<UL> getFilteredULs() {
		return filteredULList;
	}

	public int addUL(UL ul) {

		Long id = DAOFactory.getInstance().getULDAO().addItem(ul);
		if (id != null) {
			ulList.add(ul);
			return ulList.size()-1;			
		} else {
			return -1;
		}

	}

	public void updateUL(UL ul) {
		DAOFactory.getInstance().getULDAO().updateItem(ul);
	}

	public void deleteUL(UL ul) {
		DAOFactory.getInstance().getULDAO().removeItem(ul);
		ulList.remove(ul);
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
		for (ULModelStateListener listener : listeners) {
			listener.ulModelStateChanged(state);			
		}
	}


}