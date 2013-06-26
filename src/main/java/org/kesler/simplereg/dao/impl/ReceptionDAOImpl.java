package org.kesler.simplereg.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import org.hibernate.Session;

import org.kesler.simplereg.util.HibernateUtil;

import org.kesler.simplereg.dao.ReceptionDAO;
import org.kesler.simplereg.logic.Reception;

public class ReceptionDAOImpl implements ReceptionDAO {

	public void addReception(Reception reception) throws SQLException {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(reception);
			session.getTransaction().commit();			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	} 

	public void updateReception(Reception reception) throws SQLException {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.update(reception);
			session.getTransaction().commit();			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	public Reception getReceptionById(Long id) throws SQLException {
		Session session = null;
		Reception reception = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			reception = (Reception) session.load(Reception.class, id);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
		return reception;
	}

	public List<Reception> getAllReceptions() throws SQLException {
		Session session = null;
		List<Reception> receptions = new ArrayList<Reception>();
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			receptions = session.createCriteria(Reception.class).list();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
		return  receptions;
	}

	public void deleteReception(Reception reception) throws SQLException {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.delete(reception);
			session.getTransaction().commit();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
	}

}