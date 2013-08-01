package org.kesler.simplereg.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import org.hibernate.Session;

import org.kesler.simplereg.util.HibernateUtil;

import org.kesler.simplereg.dao.OperatorDAO;
import org.kesler.simplereg.logic.Operator;

public class OperatorDAOImpl implements OperatorDAO {

	public void addOperator(Operator operator) throws SQLException {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(operator);
			session.getTransaction().commit();			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	} 

	public void updateOperator(Operator operator) throws SQLException {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.update(operator);
			session.getTransaction().commit();			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	public void saveOperators(List<Operator> operators) throws SQLException {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			for (Operator operator: operators) {
				session.saveOrUpdate(operator);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	public Operator getOperatorById(Long id) throws SQLException {
		Session session = null;
		Operator operator = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			operator = (Operator) session.load(Operator.class, id);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
		return operator;
	}

	public List<Operator> getAllOperators() throws SQLException {
		Session session = null;
		List<Operator> operators = new ArrayList<Operator>();
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			operators = session.createCriteria(Operator.class).list();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
		return  operators;
	}

	public void deleteOperator(Operator operator) throws SQLException {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.delete(operator);
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