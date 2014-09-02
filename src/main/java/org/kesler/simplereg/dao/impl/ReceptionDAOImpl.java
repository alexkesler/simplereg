package org.kesler.simplereg.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.HibernateException;

import org.hibernate.criterion.Restrictions;
import org.kesler.simplereg.util.HibernateUtil;

import org.kesler.simplereg.dao.ReceptionDAO;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.logic.Reception;

public class ReceptionDAOImpl extends GenericDAOImpl<Reception> implements ReceptionDAO {

	public ReceptionDAOImpl() {
		super(Reception.class);
	}


	public void addReception(Reception reception) {
		addItem(reception);
	}

	public void updateReception(Reception reception) {
		updateItem(reception);
	}

	public Reception getReceptionById(Long id) {
		return getItemById(id);
	}

	public List<Reception> getAllReceptions() {
		return getAllItems();
	}

    public List<Reception> getReceptionsByOpenDate(Date beginDate, Date endDate) {
        Session session = null;
        List<Reception> receptions = new ArrayList<Reception>();
        log.info("Reading receptions by OpenDate");
        try {
            notifyListeners(DAOState.CONNECTING);
            session = HibernateUtil.getSessionFactory().openSession();
            notifyListeners(DAOState.READING);
            Criteria criteria = session.createCriteria(Reception.class);

            if (beginDate!=null) {
                criteria.add(Restrictions.ge("openDate",beginDate));
            }
            if (endDate!=null) {
                criteria.add(Restrictions.le("openDate",endDate));
            }
            receptions = criteria.list();

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
	}

    @Override
    public Integer getLastPVDPackageNum() {
        Integer lastPVDNum = null;

        Session session = null;

        String hql = "SELECT MAX(R.pvdPackageNum) from Reception R";
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(hql);
            List results = query.list();
            if (results.size()>0) lastPVDNum = (Integer) results.get(0);
        } catch (HibernateException he) {
            System.err.println("Error while reading max pvdNum");
            he.printStackTrace();
            notifyListeners(DAOState.ERROR);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        return lastPVDNum;
    }
}