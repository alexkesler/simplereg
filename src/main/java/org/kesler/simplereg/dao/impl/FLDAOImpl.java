package org.kesler.simplereg.dao.impl;

import java.util.List;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;

import org.kesler.simplereg.util.HibernateUtil;

import org.kesler.simplereg.dao.FLDAO;
import org.kesler.simplereg.logic.FL;

public class FLDAOImpl implements FLDAO {

	/**
	* Сохраняет в БД новое физ лицо
	* @param fl физическое лицо 
	* @return код сохраненной записи, в случае неудачи возвращает null
	*/
	public Long addFL(FL fl) {
		Long id = null;

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(fl);
			tx.commit();
		} catch (HibernateException he) {
			if (tx != null) tx.rollback();
			he.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();		
			}

		}

		id = fl.getId();

		return id;
	}

	/**
	* Сохраняет в БД новое физ лицо
	* @param fl физическое лицо 
	*/
	public void updateFL(FL fl) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(fl);
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



	/**
	* Читает из базы данных {@link org.kesler.simplereg.logic.FL}
	* @param id код записи в базе данных
	* @return объект {@link org.kesler.simplereg.logic.FL} из базы данных
	*/
	public FL getFLById(Long id) {
		FL fl = null;

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			fl = (FL) session.load(FL.class, id);
		} catch (HibernateException he) {
			he.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();				
			}

		}

		return fl;
	}

	/**
	* Читает из базы данных список {@link org.kesler.simplereg.logic.FL}
	* @return список {@link org.kesler.simplereg.logic.FL} из базы данных, при неудаче возвращает пустой список
	*/
	public List<FL> getAllFLs() {
		List<FL> fls = new ArrayList<FL>();

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			fls = session.createCriteria(FL.class).list();
		} catch (HibernateException he) {
			he.printStackTrace();
		} finally {
			if (session!=null && session.isOpen()) {
				session.close();				
			}
		}


		return fls;
	}

	/**
	* Удаляет из базы данных запись, соответствующую объекту {@link org.kesler.simplereg.logic.FL}
	* @param fl физическое лицо 
	*/
	public void deleteFL(FL fl) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(fl);
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