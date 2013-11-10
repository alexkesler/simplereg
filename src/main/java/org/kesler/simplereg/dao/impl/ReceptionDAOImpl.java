package org.kesler.simplereg.dao.impl;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import org.hibernate.Session;
import org.hibernate.HibernateException;

import org.kesler.simplereg.util.HibernateUtil;

import org.kesler.simplereg.dao.ReceptionDAO;
import org.kesler.simplereg.dao.DAOListener;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.logic.reception.Reception;

public class ReceptionDAOImpl implements ReceptionDAO {

	private List<DAOListener> listeners = new ArrayList<DAOListener>();

	@Override
	public void addDAOListener(DAOListener listener) {
		listeners.add(listener);
	}


	public void addReception(Reception reception) {
		Session session = null;
		try {
			notifyListeners(DAOState.CONNECTING);
			session = HibernateUtil.getSessionFactory().openSession();
			notifyListeners(DAOState.WRITING);
			session.beginTransaction();
			session.save(reception);
			session.getTransaction().commit();	
			notifyListeners(DAOState.READY);		
		} catch (HibernateException he) {
			System.err.println("Error while saving reception");
			he.printStackTrace();
			notifyListeners(DAOState.ERROR);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	} 

	public void updateReception(Reception reception) {
		Session session = null;
		try {
			notifyListeners(DAOState.CONNECTING);
			session = HibernateUtil.getSessionFactory().openSession();
			notifyListeners(DAOState.WRITING);
			session.beginTransaction();
			session.update(reception);
			session.getTransaction().commit();
			notifyListeners(DAOState.READY);			
		} catch (HibernateException he) {
			System.err.println("Error while saving reception");
			he.printStackTrace();
			notifyListeners(DAOState.ERROR);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	public Reception getReceptionById(Long id) {
		Session session = null;
		Reception reception = null;
		try {
			notifyListeners(DAOState.CONNECTING);
			session = HibernateUtil.getSessionFactory().openSession();
			notifyListeners(DAOState.READING);
			reception = (Reception) session.load(Reception.class, id);
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			System.err.println("Error while reading reception");
			he.printStackTrace();
			notifyListeners(DAOState.ERROR);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
		return reception;
	}

	public List<Reception> getAllReceptions() {
		Session session = null;
		List<Reception> receptions = new ArrayList<Reception>();
		try {
			notifyListeners(DAOState.CONNECTING);
			session = HibernateUtil.getSessionFactory().openSession();
			notifyListeners(DAOState.READING);
			receptions = session.createCriteria(Reception.class).list();
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			System.err.println("Error while reading receptions");
			he.printStackTrace();
			notifyListeners(DAOState.ERROR);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
		return  receptions;
	}

	public void removeReception(Reception reception) {
		Session session = null;
		try {
			notifyListeners(DAOState.CONNECTING);
			session = HibernateUtil.getSessionFactory().openSession();
			notifyListeners(DAOState.WRITING);
			session.beginTransaction();
			session.delete(reception);
			session.getTransaction().commit();
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			System.err.println("Error while removing reception");
			he.printStackTrace();
			notifyListeners(DAOState.ERROR);
		} finally {
			if (session != null && session.isOpen()) {
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