package org.kesler.simplereg.dao.impl;

import java.util.List;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;

import org.kesler.simplereg.dao.DAO;
import org.kesler.simplereg.dao.AbstractEntity;

import org.kesler.simplereg.util.HibernateUtil;

public class DAOImpl <T extends AbstractEntity> implements DAO <T> {

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

		return id;
	}

	public void update(T item) {

	}

	public T getById(int index) {
		T item = null;

		return item;
	}

	public List<T> getAll() {
		List<T> list = new ArrayList<T>();


		return list;
	}

	public void remove(T item) {

	}

}