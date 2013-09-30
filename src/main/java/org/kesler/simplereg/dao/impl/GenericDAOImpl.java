package org.kesler.simplereg.dao.impl;

import java.util.List;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;

import org.kesler.simplereg.dao.DAO;
import org.kesler.simplereg.dao.AbstractEntity;

import org.kesler.simplereg.util.HibernateUtil;

public class GenericDAOImpl<T extends AbstractEntity> implements DAO <T> {

	private Class<T> type;

	public DAOImpl(Class<T> type) {
		this.type = type;
	}

	public Long add(T item) {
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

	public void update(T item) {

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

	public T getById(int id) {
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

	public List<T> getAll() {
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

	public void remove(T item) {

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

}