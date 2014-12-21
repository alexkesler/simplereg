package org.kesler.simplereg.dao.impl;

import java.util.List;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import org.apache.log4j.Logger;

import org.kesler.simplereg.dao.GenericDAO;
import org.kesler.simplereg.dao.AbstractEntity;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.dao.DAOListener;

import org.kesler.simplereg.dao.support.DAOException;
import org.kesler.simplereg.util.HibernateUtil;

public class GenericDAOImpl<T extends AbstractEntity> implements GenericDAO <T> {
	protected Logger log;

	private List<DAOListener> listeners = new ArrayList<DAOListener>();

	private Class<T> type;

	public GenericDAOImpl(Class<T> type) {
		this.type = type;
		log = Logger.getLogger("GenericDAO<"+ type.getSimpleName() + ">");
	}

	@Override
	public void addDAOListener(DAOListener listener) {
		if (!listeners.contains(listener)) listeners.add(listener);
	}

    @Override
    public void removeDAOListener(DAOListener listener) {
        listeners.remove(listener);
    }

    public Long addItem(T item) throws DAOException{
		Long id = null;

		notifyListeners(DAOState.CONNECTING);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			log.debug("Begin to write item");
			notifyListeners(DAOState.WRITING);
			tx = session.beginTransaction();
			session.save(item);
			tx.commit();
			log.info("Adding item complete");
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			if (tx != null) tx.rollback();
			log.error("Error writing item", he);
			he.printStackTrace();
			notifyListeners(DAOState.ERROR);
			throw new DAOException("Error writing item", he);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();		
			}
		}

		id = item.getId();

		return id;
	}

	public void updateItem(T item) throws DAOException{

		notifyListeners(DAOState.CONNECTING);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			log.debug("Updating item");
			notifyListeners(DAOState.WRITING);
			tx = session.beginTransaction();
			session.update(item);
			tx.commit();
			log.info("Updating item complete");
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			if (tx != null) tx.rollback();
			log.error("Error updating item", he);
			notifyListeners(DAOState.ERROR);
			throw new DAOException("Error updating item", he);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();		
			}
		}		

	}

	public T getItemById(long id) throws DAOException{
		T item = null;

		notifyListeners(DAOState.CONNECTING);
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			log.debug("Reading item");
			notifyListeners(DAOState.READING);
			item = (T) session.get(type, id);
			log.info("Reading item complete");
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			log.error("Reading item error", he);
			notifyListeners(DAOState.ERROR);
			throw new DAOException("Reading item error", he);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();				
			}

		}
		return item;
	}

	public List<T> getAllItems() throws DAOException{
		List<T> list = new ArrayList<T>();
		notifyListeners(DAOState.CONNECTING);
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			log.debug("Reading items");
			notifyListeners(DAOState.READING);
			list = session.createCriteria(type).list();
			log.info("Readed " + list.size() + " items");
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			log.error("Error reading items", he);
			notifyListeners(DAOState.ERROR);
			throw new DAOException("Error reading items", he);
		} finally {
			if (session!=null && session.isOpen()) {
				session.close();				
			}
		}

		return list;
	}

	public void removeItem(T item) {

		notifyListeners(DAOState.CONNECTING);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			log.debug("Removing item");
			notifyListeners(DAOState.WRITING);
			tx = session.beginTransaction();
			session.delete(item);
			tx.commit();
			log.info("Item removed");
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			if (tx != null) tx.rollback();
			log.error("Error removing item", he);
			notifyListeners(DAOState.ERROR);
			throw new DAOException("Error removing item", he);
		} finally {
			if (session!=null && session.isOpen()) {
				session.close();				
			}			
		}

	}

	protected void notifyListeners(DAOState state) {
		for (DAOListener listener: listeners) {
			listener.daoStateChanged(state);
		}
	}

}