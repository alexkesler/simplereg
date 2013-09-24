package org.kesler.simplereg.dao.impl;

import java.util.List;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;

import org.kesler.simplereg.util.HibernateUtil;


import org.kesler.simplereg.dao.ReceptionStatusDAO;
import org.kesler.simplereg.logic.reception.ReceptionStatus;

public class ReceptionStatusDAOImpl implements ReceptionStatusDAO {

	public Long addReceptionStatus(ReceptionStatus receptionStatus) {
		Long id = null;

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(receptionStatus);
			tx.commit();
		} catch (HibernateException he) {
			if (tx != null) tx.rollback();
			he.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();		
			}

		}

		id = receptionStatus.getId();

		return id;
	}


	public void updateReceptionStatus(ReceptionStatus receptionStatus) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(receptionStatus);
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


	public List<ReceptionStatus> getAllReceptionStatuses() {
		List<ReceptionStatus> receptionStatuses = new ArrayList<ReceptionStatus>();

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			receptionStatuses = session.createCriteria(ReceptionStatus.class).list();
		} catch (HibernateException he) {
			he.printStackTrace();
		} finally {
			if (session!=null && session.isOpen()) {
				session.close();				
			}
		}


		return receptionStatuses;
	}


	public void removeReceptionStatus(ReceptionStatus receptionStatus) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(receptionStatus);
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