package org.kesler.simplereg.util;

import org.apache.log4j.Logger;

import org.kesler.simplereg.dao.DAOFactory;


public class CounterUtil {
	private static final Logger log = Logger.getLogger("CounterUtil");

	public static int getNextCount () {
		int count = 1;

		log.info("Getting current count");
		Counter currentCount = DAOFactory.getInstance().getCounterDAO().getItemById(1L);
		if (currentCount == null) {
			log.warn("Current count is null - creating new");
			currentCount = new Counter();
			currentCount.setId(1L);
			currentCount.setValue(count);
			DAOFactory.getInstance().getCounterDAO().addItem(currentCount);
		} else {
			count = currentCount.getValue();
			log.info("Current count: " + count);
			count++;
			currentCount.setValue(count);
			log.info("Saving new count: " + count);
			DAOFactory.getInstance().getCounterDAO().updateItem(currentCount);
		}

		return count;
	}

}