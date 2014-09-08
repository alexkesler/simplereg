package org.kesler.simplereg.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.dao.ULDAO;
import org.kesler.simplereg.logic.FL;
import org.kesler.simplereg.logic.UL;
import org.kesler.simplereg.util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class ULDAOImpl extends GenericDAOImpl<UL> implements ULDAO{

    public ULDAOImpl() {
        super(UL.class);
    }

    @Override
    public List<UL> getULsByShortName(String shortName) {
        List<UL> uls = new ArrayList<UL>();

        notifyListeners(DAOState.CONNECTING);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            log.debug("Reading uls by shortName");
            notifyListeners(DAOState.READING);
            uls =  session.createCriteria(UL.class).
                    add(Restrictions.eq("shortName", shortName)).list();
            log.info("Reading uls complete");
            notifyListeners(DAOState.READY);
        } catch (HibernateException he) {
            log.error("Reading uls error", he);
            notifyListeners(DAOState.ERROR);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }

        }

        return uls;
    }

    @Override
    public List<UL> getULsByFullName(String fullName) {
        List<UL> uls = new ArrayList<UL>();

        notifyListeners(DAOState.CONNECTING);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            log.debug("Reading uls by shortName");
            notifyListeners(DAOState.READING);
            uls =  session.createCriteria(UL.class).
                    add(Restrictions.eq("fullName", fullName)).list();
            log.info("Reading uls complete");
            notifyListeners(DAOState.READY);
        } catch (HibernateException he) {
            log.error("Reading uls error", he);
            notifyListeners(DAOState.ERROR);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }

        }

        return uls;
    }
}
