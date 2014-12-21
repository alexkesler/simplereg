package org.kesler.simplereg.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.kesler.simplereg.dao.TemplateDAO;
import org.kesler.simplereg.logic.template.Template;
import org.kesler.simplereg.util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 17.12.14.
 */
public class TemplateDAOImpl implements TemplateDAO {
    private final Logger log = Logger.getLogger(this.getClass());
    @Override
    public void addTemplate(Template template) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            log.debug("Begin to add template");
            tx = session.beginTransaction();
            session.save(template);
            tx.commit();
            log.info("Adding template complete");
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            log.error("Error writing template", he);
            he.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

    }

    @Override
    public void updateTemplate(Template template) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            log.debug("Updating template");
            tx = session.beginTransaction();
            session.update(template);
            tx.commit();
            log.info("Updating template complete");
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            log.error("Error updating template", he);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

    }

    @Override
    public void removeTemplate(Template template) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            log.debug("Removing item");
            tx = session.beginTransaction();
            session.delete(template);
            tx.commit();
            log.info("Template removed");
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            log.error("Error removing item", he);
        } finally {
            if (session!=null && session.isOpen()) {
                session.close();
            }
        }

    }

    @Override
    public List<Template> getAllTemplates() {
        Session session = null;
        List<Template> templates = new ArrayList<Template>();
        log.info("Reading templates");
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Template.class);

            templates = criteria.list();

            log.info("Reading " + templates.size() + " templates complete");
        } catch (HibernateException he) {
            log.error("Error reading templates",he);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return  templates;
    }

    @Override
    public List<Template> getDefaultTemplates() {
        Session session = null;
        List<Template> templates = new ArrayList<Template>();
        log.info("Reading default templates");
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Template.class);

            criteria.add(Restrictions.eq("byDefault",true));

            templates = criteria.list();

            log.info("Reading " + templates.size() + " templates complete");
        } catch (HibernateException he) {
            log.error("Error reading templates",he);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return  templates;
    }

    @Override
    public Template getTemplateByUUID(String uuid) {
        Session session = null;
        List<Template> templates = new ArrayList<Template>();
        log.info("Reading template by UUID: "+uuid);
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Template.class);

            criteria.add(Restrictions.eq("uuid",uuid));

            templates = criteria.list();

            log.info("Reading " + templates.size() + " templates complete");
        } catch (HibernateException he) {
            log.error("Error reading templates",he);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return  templates.size()==1?templates.get(0):null;
    }
}
