package org.kesler.simplereg.logic.reception;

import java.util.*;

import org.apache.log4j.Logger;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.DAOListener;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.logic.reception.filter.QuickReceptionsFiltersEnum;
import org.kesler.simplereg.logic.reception.filter.ReceptionsFiltersModel;
import org.kesler.simplereg.logic.reception.filter.ReceptionsFilter;
import org.kesler.simplereg.logic.ModelState;

public class ReceptionsModel implements DAOListener{
    protected final Logger log;


    private ReceptionsFiltersModel filtersModel;
	private List<Reception> allReceptions;
	private List<Reception> filteredReceptions;
    private List<Reception> lastReceptions;

	private List<ReceptionsModelStateListener> listeners;

	public ReceptionsModel() {
        log = Logger.getLogger(this.getClass().getSimpleName());
        allReceptions = new ArrayList<Reception>();
		filteredReceptions = new ArrayList<Reception>();
        lastReceptions = new ArrayList<Reception>();

		listeners = new ArrayList<ReceptionsModelStateListener>();
		DAOFactory.getInstance().getReceptionDAO().addDAOListener(this);

        filtersModel = new ReceptionsFiltersModel();
	}

    public ReceptionsFiltersModel getFiltersModel() {
        return filtersModel;
    }

	public void addReceptionsModelStateListener(ReceptionsModelStateListener listener) {
		if (!listeners.contains(listener)) listeners.add(listener);
	}


	public List<Reception> getAllReceptions() {
		return allReceptions;
	}

	public List<Reception> getFilteredReceptions() {
		return filteredReceptions;
	}

    public List<Reception> getLastReceptions() {
        return lastReceptions;
    }

    public List<Reception> getReceptionsByRosreesrtCode(String rosreestrCode) {
        return DAOFactory.getInstance().getReceptionDAO().getReceptionsByRosreestrCode(rosreestrCode);
    }


	// читаем данные из БД
	public void readReceptions() {
        log.info("Reading receptions from DB...");
        Date fromOpenDate = filtersModel.getFromOpenDate();
        Date toOpenDate = filtersModel.getToOpenDate();
        if (fromOpenDate != null || toOpenDate != null) {
            allReceptions = DAOFactory.getInstance().getReceptionDAO().getReceptionsByOpenDate(fromOpenDate,toOpenDate);
        } else {
            allReceptions = DAOFactory.getInstance().getReceptionDAO().getAllReceptions();
        }
        log.info("Read " + allReceptions.size() + " receptions");
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
        log.info("Applying filters: " + filters);
		notifyListeners(ModelState.FILTERING);
        filteredReceptions = filtersModel.filterReceptions(allReceptions);
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
        log.info("Applying filters sequently..");
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
     *
     */
    public void readLastReceptions() {
        log.info("Reading last receptions");
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.HOUR, -2);
        Date fromDate = calendar.getTime();
        Date toDate = new Date();
        lastReceptions = DAOFactory.getInstance().getReceptionDAO().getReceptionsByOpenDate(fromDate, toDate);
        log.info("Read "+ lastReceptions.size() + " receptions");
    }

    public void addRosreestrCodeFilter(String filterString) {
        log.info("Set RosreestrCode filter " );
        if(!filterString.isEmpty()) filtersModel.setQuickFilter(QuickReceptionsFiltersEnum.ROSREESTR_CODE,filterString);
        else filtersModel.resetQuickFilter(QuickReceptionsFiltersEnum.ROSREESTR_CODE);
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

    public Integer getLastPVDPackageNum() {
        return DAOFactory.getInstance().getReceptionDAO().getLastPVDPackageNum();
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
        log.info("State: "+state);
		for (ReceptionsModelStateListener listener: listeners) {
			listener.receptionsModelStateChanged(state);
		}
	}

    public void finish() {
        DAOFactory.getInstance().getReceptionDAO().removeDAOListener(this);
    }

}

