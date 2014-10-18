package org.kesler.simplereg.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.*;


import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.kesler.simplereg.util.HibernateUtil;

import org.kesler.simplereg.dao.ReceptionDAO;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.logic.Reception;

public class ReceptionDAOImpl extends GenericDAOImpl<Reception> implements ReceptionDAO {

	public ReceptionDAOImpl() {
		super(Reception.class);
        log = Logger.getLogger(this.getClass().getSimpleName());
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
                criteria.add(Restrictions.ge("openDate", beginDate));
            }
            if (endDate!=null) {
                criteria.add(Restrictions.le("openDate",endDate));
            }
            criteria.setFetchMode("realtyObject", FetchMode.JOIN);
            criteria.setFetchMode("operator", FetchMode.JOIN);
            criteria.setFetchMode("service", FetchMode.JOIN);

            receptions = criteria.list();

            log.info("Reading " + receptions.size() + " receptions complete");
            notifyListeners(DAOState.READY);
        } catch (HibernateException he) {
            log.error("Error reading receptions",he);
            notifyListeners(DAOState.ERROR);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return  receptions;
    }

    @Override
    public List<Reception> getReceptionsByRosreestrCode(String code) {
        Session session = null;
        List<Reception> receptions = new ArrayList<Reception>();
        if (code==null || code.isEmpty()) return receptions;
        log.info("Reading receptions by RosReestrCode: " + code);
        try {
            notifyListeners(DAOState.CONNECTING);
            session = HibernateUtil.getSessionFactory().openSession();
            notifyListeners(DAOState.READING);
            Criteria criteria = session.createCriteria(Reception.class);

            criteria.add(Restrictions.like("rosreestrCode", code, MatchMode.ANYWHERE).ignoreCase());

            criteria.setFetchMode("realtyObject", FetchMode.JOIN);
            criteria.setFetchMode("operator", FetchMode.JOIN);
            criteria.setFetchMode("service", FetchMode.JOIN);

            receptions = criteria.list();

            log.info("Reading " + receptions.size() + " receptions complete");
            notifyListeners(DAOState.READY);
        } catch (HibernateException he) {
            log.error("Error reading receptions",he);
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
            log.info("reading max PVD num");
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(hql);
            List results = query.list();
            if (results.size()>0) lastPVDNum = (Integer) results.get(0);
        } catch (HibernateException he) {
            log.error("Error while reading max pvdNum",he);
            notifyListeners(DAOState.ERROR);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        return lastPVDNum;
    }
}