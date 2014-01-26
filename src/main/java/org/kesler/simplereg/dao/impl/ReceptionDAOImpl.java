package org.kesler.simplereg.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

import org.hibernate.Session;
import org.hibernate.HibernateException;

import org.hibernate.criterion.Restrictions;
import org.kesler.simplereg.util.HibernateUtil;

import org.kesler.simplereg.dao.ReceptionDAO;
import org.kesler.simplereg.dao.DAOListener;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.logic.Reception;

public class ReceptionDAOImpl extends GenericDAOImpl<Reception> implements ReceptionDAO {

	// private List<DAOListener> listeners = new ArrayList<DAOListener>();

	// @Override
	// public void addDAOListener(DAOListener listener) {
	// 	listeners.add(listener);
	// }

	public ReceptionDAOImpl() {
		super(Reception.class);
	}


	public void addReception(Reception reception) {
		addItem(reception);
		// Session session = null;
		// try {
		// 	notifyListeners(DAOState.CONNECTING);
		// 	session = HibernateUtil.getSessionFactory().openSession();
		// 	notifyListeners(DAOState.WRITING);
		// 	session.beginTransaction();
		// 	session.save(reception);
		// 	session.getTransaction().commit();	
		// 	notifyListeners(DAOState.READY);		
		// } catch (HibernateException he) {
		// 	System.err.println("Error while saving reception");
		// 	he.printStackTrace();
		// 	notifyListeners(DAOState.ERROR);
		// } finally {
		// 	if (session != null && session.isOpen()) {
		// 		session.close();
		// 	}
		// }
	} 

	public void updateReception(Reception reception) {
		updateItem(reception);
		// Session session = null;
		// try {
		// 	notifyListeners(DAOState.CONNECTING);
		// 	session = HibernateUtil.getSessionFactory().openSession();
		// 	notifyListeners(DAOState.WRITING);
		// 	session.beginTransaction();
		// 	session.update(reception);
		// 	session.getTransaction().commit();
		// 	notifyListeners(DAOState.READY);			
		// } catch (HibernateException he) {
		// 	System.err.println("Error while saving reception");
		// 	he.printStackTrace();
		// 	notifyListeners(DAOState.ERROR);
		// } finally {
		// 	if (session != null && session.isOpen()) {
		// 		session.close();
		// 	}
		// }
	}

	public Reception getReceptionById(Long id) {
		return getItemById(id);
		// Session session = null;
		// Reception reception = null;
		// try {
		// 	notifyListeners(DAOState.CONNECTING);
		// 	session = HibernateUtil.getSessionFactory().openSession();
		// 	notifyListeners(DAOState.READING);
		// 	reception = (Reception) session.load(Reception.class, id);
		// 	notifyListeners(DAOState.READY);
		// } catch (HibernateException he) {
		// 	System.err.println("Error while reading reception");
		// 	he.printStackTrace();
		// 	notifyListeners(DAOState.ERROR);
		// } finally {
		// 	if (session != null && session.isOpen()) {
		// 		session.close();
		// 	}				
		// }
		// return reception;
	}

	public List<Reception> getAllReceptions() {
		return getAllItems();
		// Session session = null;
		// List<Reception> receptions = new ArrayList<Reception>();
		// try {
		// 	notifyListeners(DAOState.CONNECTING);
		// 	session = HibernateUtil.getSessionFactory().openSession();
		// 	notifyListeners(DAOState.READING);
		// 	receptions = session.createCriteria(Reception.class).list();
		// 	notifyListeners(DAOState.READY);
		// } catch (HibernateException he) {
		// 	System.err.println("Error while reading receptions");
		// 	he.printStackTrace();
		// 	notifyListeners(DAOState.ERROR);
		// } finally {
		// 	if (session != null && session.isOpen()) {
		// 		session.close();
		// 	}				
		// }
		// return  receptions;
	}

    public List<Reception> getReceptionsByOpenDate(Date beginDate, Date endDate) {
        Session session = null;
        List<Reception> receptions = new ArrayList<Reception>();
        log.info("Reading receptions by OpenDate");
        try {
            notifyListeners(DAOState.CONNECTING);
            session = HibernateUtil.getSessionFactory().openSession();
            notifyListeners(DAOState.READING);
            receptions = session.createCriteria(Reception.class)
                                                .add(Restrictions.between("openDate",beginDate,endDate))
                                                .list();
            log.info("Reading " + receptions.size() + " receptions complete");                                    
            System.out.println("----Receptions in DAO-----" + receptions.size());
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
    	removeItem(reception);
		// Session session = null;
		// try {
		// 	notifyListeners(DAOState.CONNECTING);
		// 	session = HibernateUtil.getSessionFactory().openSession();
		// 	notifyListeners(DAOState.WRITING);
		// 	session.beginTransaction();
		// 	session.delete(reception);
		// 	session.getTransaction().commit();
		// 	notifyListeners(DAOState.READY);
		// } catch (HibernateException he) {
		// 	System.err.println("Error while removing reception");
		// 	he.printStackTrace();
		// 	notifyListeners(DAOState.ERROR);
		// } finally {
		// 	if (session != null && session.isOpen()) {
		// 		session.close();
		// 	}				
		// }
	}

	// private void notifyListeners(DAOState state) {
	// 	for (DAOListener listener: listeners) {
	// 		listener.daoStateChanged(state);
	// 	}
	// }

}