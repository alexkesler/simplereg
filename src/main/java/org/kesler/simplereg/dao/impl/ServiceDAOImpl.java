package org.kesler.simplereg.dao.impl;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import org.kesler.simplereg.util.HibernateUtil;

import org.kesler.simplereg.dao.ServiceDAO;
import org.kesler.simplereg.logic.Service;

public class ServiceDAOImpl implements ServiceDAO {

	public void addService(Service service) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(service);
			session.getTransaction().commit();			
		} catch (HibernateException he) {
			System.err.println("Error while saving service");
			he.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	} 

	public void updateService(Service service) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.update(service);
			session.getTransaction().commit();			
		} catch (HibernateException he) {
			System.err.println("Error while saving service");
			he.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	public Service getServiceById(Long id) {
		Session session = null;
		Service service = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			service = (Service) session.load(Service.class, id);
		} catch (HibernateException he) {
			System.err.println("Error while saving service");
			he.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
		return service;
	}

	public List<Service> getAllServices() {
		Session session = null;
		List<Service> services = new ArrayList<Service>();
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			services = session.createCriteria(Service.class).list();
		} catch (HibernateException he) {
			System.err.println("Error while reading services");
			he.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
		return  services;
	}

	public List<Service> getActiveServices() {
		Session session = null;
		List<Service> services = new ArrayList<Service>();
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			services = session.createCriteria(Service.class)
			.add(Restrictions.eq("enabled",new Boolean(true)))
			.list();
		} catch (HibernateException he) {
			System.err.println("Error while reading services");
			he.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
		return  services;
	}


	public void deleteService(Service service) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.delete(service);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			System.err.println("Error while removing service");
			he.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
	}

	public void saveServices(List<Service> services) {
		Session session = null;
		Transaction tx = null;


	}

}