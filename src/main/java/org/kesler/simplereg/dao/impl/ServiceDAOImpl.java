package org.kesler.simplereg.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.kesler.simplereg.util.HibernateUtil;
import org.kesler.simplereg.dao.ServiceDAO;
import org.kesler.simplereg.logic.Service;

public class ServiceDAOImpl implements ServiceDAO {

	public void addService(Service service) throws SQLException {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(service);
			session.getTransaction().commit();			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	} 

	public void updateService(Service service) throws SQLException {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.update(service);
			session.getTransaction().commit();			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	public Service getServiceById(Long id) throws SQLException {
		Session session = null;
		Service service = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			service = (Service) session.load(Service.class, id);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
		return service;
	}

	public List<Service> getAllServices() throws SQLException {
		Session session = null;
		List<Service> services = new ArrayList<Service>();
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			services = session.createCriteria(Service.class).list();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
		return  services;
	}

	public void deleteService(Service service) throws SQLException {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.delete(service);
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