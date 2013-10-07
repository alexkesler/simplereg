package org.kesler.simplereg.dao.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.HibernateException;

import org.kesler.simplereg.util.HibernateUtil;

import org.kesler.simplereg.dao.OperatorDAO;
import org.kesler.simplereg.dao.EntityState;
import org.kesler.simplereg.dao.DAOListener;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.logic.operator.Operator;


public class OperatorDAOImpl implements OperatorDAO {


	private List<DAOListener> listeners = new ArrayList<DAOListener>(); 

	public void addDAOListener(DAOListener listener) {
		listeners.add(listener);
	}


	/**
	* <p>Сохраняет список операторов в зависимости от их статуса {@link org.kesler.simplereg.dao.EntityState}:
	*<ul> 
	*<li>EntityState.SAVED - <b>сохранен в базе данных</b> - ничего не делаем</li>
	*<li>EntityState.NEW- <b>новый</b> - создаем новую запись в базе данных</li>
	*<li>EntityState.CHANGED - <b>изменен</b> - сохраняем измемения в базу данных</li>
	*<li>EntityState.DELETED - <b>удален</b> - помечен для удаления - удаляем запись из базы данных</li>
	*</ul> 
	*</p>
	* <p>
	*  при удачном сохранении изменений в базу данных, изменения фиксируются в исходном списке 
	* </p>
	* @see org.kesler.simplereg.logic.Operator
	* @see org.kesler.simplereg.dao.AbstractEntity
	* @see org.kesler.simplereg.dao.EntityState
	*/
	public void saveOperators(List<Operator> operators) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			for (Operator operator: operators) {
				switch (operator.getState()) {
					case SAVED:
					break;
					case NEW:
						session.save(operator);
					break;
					case CHANGED:
						session.update(operator);
					break;
					case DELETED:
						session.delete(operator);
					break;			
					
				}
			}
			session.getTransaction().commit();
			
			// всем операторам назначаем сохраненное состояние - удаленные удаляем
			for (Iterator<Operator> it = operators.iterator(); it.hasNext();) {
				Operator operator = it.next();
				switch (operator.getState()) {
					case SAVED:
					break;
					case NEW:
						operator.setState(EntityState.SAVED);
					break;
					case CHANGED:
						operator.setState(EntityState.SAVED);
					break;
					case DELETED:
						it.remove();
					break;			
					
				}
			}	

		} catch (HibernateException he) {
			System.err.println("Error while saving operators");
			he.printStackTrace();

		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**
	* 
	*@return список всех операторов из базы данных, состояние всех операторов Operator.SAVED_STATE по умолчанию
	*/
	public List<Operator> getAllOperators() {
		Session session = null;
		List<Operator> operators = new ArrayList<Operator>();
		try {
			System.out.println("Open session to read operators...");
			notifyListeners(DAOState.CONNECTING);
			session = HibernateUtil.getSessionFactory().openSession();
			System.out.println("Reading operators...");
			notifyListeners(DAOState.READING);			
			operators = session.createCriteria(Operator.class).list();
			notifyListeners(DAOState.READY);
		} catch (HibernateException he) {
			System.err.println("Error while reading operators");
			he.printStackTrace();
			notifyListeners(DAOState.ERROR);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
		return  operators;
	}

	/**
	* 
	*/
	private void notifyListeners(DAOState state) {
		for (DAOListener listener: listeners) {
			listener.daoStateChanged(state);
		}
	}

}