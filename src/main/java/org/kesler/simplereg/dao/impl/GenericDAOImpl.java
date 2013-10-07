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

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(item);
			tx.commit();
		} catch (HibernateException he) {
			if (tx != null) tx.rollback();
			he.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();		
			}
		}

		id = item.getId();

		return id;
	}

	public void updateItem(T item) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(item);
			tx.commit();
		} catch (HibernateException he) {
			if (tx != null) tx.rollback();
			he.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();		
			}
		}		

	}

	public T getItemById(int id) {
		T item = null;

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			item = (T) session.load(type, id);
		} catch (HibernateException he) {
			he.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();				
			}

		}
		return item;
	}

	public List<T> getAllItems() {
		List<T> list = new ArrayList<T>();

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			list = session.createCriteria(type).list();
		} catch (HibernateException he) {
			he.printStackTrace();
		} finally {
			if (session!=null && session.isOpen()) {
				session.close();				
			}
		}

		return list;
	}

	public void removeItem(T item) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(item);
			tx.commit();
		} catch (HibernateException he) {
			if (tx != null) tx.rollback();
			he.printStackTrace();
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