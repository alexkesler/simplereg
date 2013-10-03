package org.kesler.simplereg.dao.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.HibernateException;

import org.kesler.simplereg.util.HibernateUtil;

import org.kesler.simplereg.dao.OperatorDAO;
import org.kesler.simplereg.logic.operator.Operator;

public class OperatorDAOImpl implements OperatorDAO {



	/**
	* <p>Сохраняет список операторов в зависимости от их статуса {@link org.kesler.simplereg.logic.Operator}:
	*<ul> 
	*<li>Operator.SAVED_STATE - <b>сохранен в базе данных</b> - ничего не делаем</li>
	*<li>Operator.NEW_STATE - <b>новый</b> - создаем новую запись в базе данных</li>
	*<li>Operator.EDITED_STATE - <b>изменен</b> - сохраняем измемения в базу данных</li>
	*<li>Operator.DELETED_STATE - <b>удален</b> - помечен для удаления - удаляем запись из базы данных</li>
	*</ul> 
	*</p>
	* <p>
	*  при удачном сохранении изменений в базу данных, изменения фиксируются в исходном списке 
	* </p>
	* @see org.kesler.simplereg.logic.Operator
	* @see org.kesler.simplereg.dao.AbstractEntity
	*/
	public void saveOperators(List<Operator> operators) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			for (Operator operator: operators) {
				switch (operator.getState()) {
					case Operator.SAVED_STATE:
					break;
					case Operator.NEW_STATE:
						session.save(operator);
					break;
					case Operator.EDITED_STATE:
						session.update(operator);
					break;
					case Operator.DELETED_STATE:
						session.delete(operator);
					break;			
					
				}
			}
			session.getTransaction().commit();
			
			// всем операторам назначаем сохраненное состояние - удаленные удаляем
			for (Iterator<Operator> it = operators.iterator(); it.hasNext();) {
				Operator operator = it.next();
				switch (operator.getState()) {
					case Operator.SAVED_STATE:
					break;
					case Operator.NEW_STATE:
						operator.setState(Operator.SAVED_STATE);
					break;
					case Operator.EDITED_STATE:
						operator.setState(Operator.SAVED_STATE);
					break;
					case Operator.DELETED_STATE:
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
			session = HibernateUtil.getSessionFactory().openSession();
			operators = session.createCriteria(Operator.class).list();
		} catch (HibernateException he) {
			System.err.println("Error while reading operators");
			he.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
		return  operators;
	}


}