package org.kesler.simplereg.logic.reception;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import java.sql.SQLException;

import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.DAOListener;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.logic.reception.filter.ReceptionsFiltersModel;
import org.kesler.simplereg.util.OptionsUtil;
import org.kesler.simplereg.logic.reception.filter.ReceptionsFilter;
import org.kesler.simplereg.logic.ModelState;

public class ReceptionsModel implements DAOListener{
//	private static ReceptionsModel instance = null;

    private ReceptionsFiltersModel filtersModel;
	private List<Reception> allReceptions;
	private List<Reception> filteredReceptions;

	private List<ReceptionsModelStateListener> listeners;

	public ReceptionsModel() {
		allReceptions = new ArrayList<Reception>();
		filteredReceptions = new ArrayList<Reception>();
		listeners = new ArrayList<ReceptionsModelStateListener>();
		DAOFactory.getInstance().getReceptionDAO().addDAOListener(this);

        filtersModel = new ReceptionsFiltersModel();
	}

    public ReceptionsFiltersModel getFiltersModel() {
        return filtersModel;
    }

	public void addReceptionsModelStateListener(ReceptionsModelStateListener listener) {
		listeners.add(listener);
	}

//	public static synchronized ReceptionsModel getInstance() {
//		if (instance == null) {
//			instance = new ReceptionsModel();
//		}
//		return instance;
//	}


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
	public void applyFilters() {
        List<ReceptionsFilter> filters = filtersModel.getFilters();
		filteredReceptions = new ArrayList<Reception>();
		notifyListeners(ModelState.FILTERING);
		for (Reception reception: allReceptions) {
			boolean fit = true;		
			for (ReceptionsFilter filter: filters) {
				if (!filter.checkReception(reception)) {
                    fit = false;
                    break;
                }

			}
			if (fit) filteredReceptions.add(reception);
		}
		notifyListeners(ModelState.FILTERED);

	}

    /**
     * Применяет фильтры в отдельном потоке
     */
	public void applyFiltersInSeparateThread() {
		Thread filterThread = new Thread(new Runnable() {
			public void run() {
//				readReceptions();
				applyFilters();
			}
		});
		filterThread.start();
	}

    /**
     * Читает список приемов и применяет фильтры в отдельном потоке
     */
    public void readReceptionsAndApplyFiltersInSeparateThread() {
        Thread readerFiltererThread = new Thread(new Runnable() {
            public void run() {
				readReceptions();
                applyFilters();
            }
        });
        readerFiltererThread.start();
    }

    /**
     * Метод для последовательного применения фильтров
     * @param filters Фильтры, которые необходимо применить
     */
    public void applyFiltersSequently(List<ReceptionsFilter> filters) {
        List<Reception> newFilteredReceptions = new ArrayList<Reception>();
        notifyListeners(ModelState.FILTERING);
        for (Reception reception: filteredReceptions) {
            boolean fit = true;
            for (ReceptionsFilter filter: filters) {
                if (!filter.checkReception(reception)) fit = false;
            }
            if (fit) newFilteredReceptions.add(reception);
        }
        filteredReceptions = newFilteredReceptions;
        notifyListeners(ModelState.FILTERED);
    }

    /**
     * Применяет фильтры последовательно в отдельном потоке
     * @param filters Фильтры, которые необходимо применить
     */
    public void applyFiltersSequentlyInSeparateThread(final List<ReceptionsFilter> filters) {
        Thread filterThread = new Thread(new Runnable() {
            public void run() {
                applyFiltersSequently(filters);
            }
        });
        filterThread.start();
    }


    /**
     * Сохраняет новый прием
     * @param reception  Сохраняемый прием
     */
	public void addReception(Reception reception) {
		// Назначаем для приема начальный статус
        reception.setStatus(ReceptionStatusesModel.getInstance().getInitReceptionStatus());

		DAOFactory.getInstance().getReceptionDAO().addReception(reception);
//		allReceptions.add(reception);
	}

    /**
     * Сохраняет новый прием в отдельном потоке
      * @param reception сохраняемый прием
     */
    public void addReceptionInSeparateThread(final Reception reception) {
        Thread adderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                addReception(reception);
            }
        });
        adderThread.start();
    }

    /**
     * Сохраняет существующий прием
     * @param reception существующий прием, который необходимо сохранить
     */
	public void updateReception(Reception reception) {
		DAOFactory.getInstance().getReceptionDAO().updateReception(reception);
	}

    /**
     * Сохраняет существующий прием в отдельном потоке
     * @param reception  существующий прием, который необходимо сохранить
     */
    public void updateReceptionInSeparateThread(final Reception reception) {
        Thread updaterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                updateReception(reception);
            }
        });
        updaterThread.start();
    }

    /**
     * Удаляет прием
     * @param reception прием, который необходимо удалить
     */
	public void removeReception(Reception reception) {
		DAOFactory.getInstance().getReceptionDAO().removeReception(reception);
		allReceptions.remove(reception);
		filteredReceptions.remove(reception);
	}

    /**
     * Удаляет прием в отдельном потоке
     * @param reception прием, который необходимо удалить
     */
    public void removeReceptionInSeparateThread(final Reception reception) {
        Thread removerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                removeReception(reception);
            }
        });
        removerThread.start();
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

