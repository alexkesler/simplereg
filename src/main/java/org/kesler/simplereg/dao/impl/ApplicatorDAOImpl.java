package org.kesler.simplereg.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import org.hibernate.Session;

import org.kesler.simplereg.util.HibernateUtil;
import org.kesler.simplereg.dao.ApplicatorDAO;
import org.kesler.simplereg.logic.Applicator;

public class ApplicatorDAOImpl implements ApplicatorDAO {

	public void addApplicator(Applicator applicator) throws SQLException {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(applicator);
			session.getTransaction().commit();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	public void updateApplicator(Applicator applicator) throws SQLException {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.update(applicator);
			session.getTransaction().commit();			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	public Applicator getApplicatorById(Long id) throws SQLException {
		Session session = null;
		Applicator applicator = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			applicator = (Applicator) session.load(Applicator.class, id);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
		return applicator;
	}

	public List<Applicator> getAllApplicators() throws SQLException {
		Session session = null;
		List<Applicator> applicators = new ArrayList<Applicator>();
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			applicators = session.createCriteria(Applicator.class).list();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
		return  applicators;
	}

	public void deleteApplicator(Applicator applicator) throws SQLException {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.delete(applicator);
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