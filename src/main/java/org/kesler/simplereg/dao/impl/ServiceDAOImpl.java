package org.kesler.simplereg.dao.impl;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import org.kesler.simplereg.util.HibernateUtil;

import org.kesler.simplereg.dao.ServiceDAO;
import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.dao.DAOListener;

public class ServiceDAOImpl extends GenericDAOImpl<Service> implements ServiceDAO {

	private List<DAOListener> listeners = new ArrayList<DAOListener>();

	public ServiceDAOImpl() {
		super(Service.class);
	}

	public List<Service> getActiveServices() {
		Session session = null;
		List<Service> services = new ArrayList<Service>();
		try {
			log.info("Reading active services");
			notifyListeners(DAOState.CONNECTING);
			session = HibernateUtil.getSessionFactory().openSession();
			notifyListeners(DAOState.READING);
			services = session.createCriteria(Service.class)
							  .add(Restrictions.eq("enabled",new Boolean(true)))
							  .list();
			log.info("Complete reading active services");				  
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			System.err.println("Error while reading services");
			log.error("Error reading active services", he);
			notifyListeners(DAOState.ERROR);			
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
		return  services;
	}



}