package org.kesler.simplereg.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.dao.FLDAO;
import org.kesler.simplereg.dao.support.DAOException;
import org.kesler.simplereg.logic.FL;
import org.kesler.simplereg.util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class FLDAOImpl extends GenericDAOImpl<FL> implements FLDAO{

    public FLDAOImpl() {
        super(FL.class);
    }

    @Override
    public List<FL> getFLByFIO(String firstName, String surName, String parentName) throws DAOException{
        List<FL> fls = new ArrayList<FL>();

        notifyListeners(DAOState.CONNECTING);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            log.debug("Reading fls by FIO");
            notifyListeners(DAOState.READING);
            fls =  session.createCriteria(FL.class).
                    add(Restrictions.eq("firstName",firstName)).
                    add(Restrictions.eq("surName", surName)).
                    add(Restrictions.eq("parentName", parentName)).list();
            log.info("Reading fls complete");
            notifyListeners(DAOState.READY);
        } catch (HibernateException he) {
            log.error("Reading fls error", he);
            notifyListeners(DAOState.ERROR);
            throw new DAOException(he);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }

        }
        return fls;
    }
}
