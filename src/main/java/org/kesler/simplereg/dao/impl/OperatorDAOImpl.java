package org.kesler.simplereg.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;

import org.hibernate.Session;

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
	public void saveOperators(List<Operator> operators) throws SQLException {
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

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString(), "Ошибка I/O", JOptionPane.OK_OPTION);
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
	public List<Operator> getAllOperators() throws SQLException {
		Session session = null;
		List<Operator> operators = new ArrayList<Operator>();
		try {
			System.out.println("Open session to read operators...");
			session = HibernateUtil.getSessionFactory().openSession();
			operators = session.createCriteria(Operator.class).list();
		} catch (Exception e) {
			//JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}				
		}
		return  operators;
	}


}