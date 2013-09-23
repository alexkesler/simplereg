package org.kesler.simplereg.dao.impl;

import java.util.List;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;

import org.kesler.simplereg.util.HibernateUtil;

import org.kesler.simplereg.dao.ULDAO;
import org.kesler.simplereg.logic.applicator.UL;

public class ULDAOImpl implements ULDAO {

	public Long addUL(UL ul) {
		Long id = null;

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(ul);
			tx.commit();
		} catch (HibernateException he) {
			if (tx != null) tx.rollback();
			he.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		id = ul.getId();

		return id;
	}

	public void updateUL(UL ul) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(ul);
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


	public UL getULById(Long id) {
		UL ul = null;

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			ul = (UL) session.load(UL.class, id);
		} catch (HibernateException he) {
			he.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();				
			}
		}

		return ul;
	}

	public List<UL> getAllULs() {
		List<UL> list = new ArrayList<UL>();
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			list = session.createCriteria(UL.class).list();
		} catch (HibernateException he) {
			he.printStackTrace();
		} finally {
			if (session!=null && session.isOpen()) {
				session.close();				
			}
		}

		return list;
	}

	public void deleteUL(UL ul) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(ul);
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