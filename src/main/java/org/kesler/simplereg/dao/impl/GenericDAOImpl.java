package org.kesler.simplereg.dao.impl;

import java.util.List;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;

import org.kesler.simplereg.dao.GenericDAO;
import org.kesler.simplereg.dao.AbstractEntity;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.dao.DAOListener;

import org.kesler.simplereg.util.HibernateUtil;

public class GenericDAOImpl<T extends AbstractEntity> implements GenericDAO <T> {

	private List<DAOListener> listeners = new ArrayList<DAOListener>();

	private Class<T> type;

	public GenericDAOImpl(Class<T> type) {
		this.type = type;
	}

	@Override
	public void addDAOListener(DAOListener listener) {
		listeners.add(listener);
	}

	public Long addItem(T item) {
		Long id = null;

		notifyListeners(DAOState.CONNECTING);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			notifyListeners(DAOState.WRITING);
			tx = session.beginTransaction();
			session.save(item);
			tx.commit();
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			if (tx != null) tx.rollback();
			he.printStackTrace();
			notifyListeners(DAOState.ERROR);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();		
			}
		}

		id = item.getId();

		return id;
	}

	public void updateItem(T item) {

		notifyListeners(DAOState.CONNECTING);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			notifyListeners(DAOState.WRITING);
			tx = session.beginTransaction();
			session.update(item);
			tx.commit();
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			if (tx != null) tx.rollback();
			he.printStackTrace();
			notifyListeners(DAOState.ERROR);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();		
			}
		}		

	}

	public T getItemById(long id) {
		T item = null;

		notifyListeners(DAOState.CONNECTING);
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			notifyListeners(DAOState.READING);
			item = (T) session.load(type, id);
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			he.printStackTrace();
			notifyListeners(DAOState.ERROR);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();				
			}

		}
		return item;
	}

	public List<T> getAllItems() {
		List<T> list = new ArrayList<T>();
		notifyListeners(DAOState.CONNECTING);
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			notifyListeners(DAOState.READING);
			list = session.createCriteria(type).list();
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			he.printStackTrace();
			notifyListeners(DAOState.ERROR);
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
			notifyListeners(DAOState.WRITING);
			tx = session.beginTransaction();
			session.delete(item);
			tx.commit();
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			if (tx != null) tx.rollback();
			he.printStackTrace();
			notifyListeners(DAOState.ERROR);
		} finally {
			if (session!=null && session.isOpen()) {
				session.close();				
			}			
		}

	}

	private void notifyListeners(DAOState state) {
		for (DAOListener listener: listeners) {
			listener.daoStateChanged(state);
		}
	}

}