package org.kesler.simplereg.dao;

import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.SQLException;

import org.kesler.simplereg.logic.Operator;

public class TestOperatorDAO {

	@Test
	public void testAddRead() {
		Operator o = new Operator();

		String initFIO = "Операторов Оператор";
		o.setFIO(initFIO);

		// сохраняем
		try {
			DAOFactory.getInstance().getOperatorDAO().addOperator(o);
		} catch (SQLException sqle) {
			System.out.println("DB Error: " + sqle.getMessage());									
		}

		// Проверяем получение id при сохранении
		Long id = o.getId();
		assertNotNull("Writen Operator dont get id", id);
		if (id == null) {
			return ; // далее бессмысленно		
		}	

		// проверяем чтение 
		Operator resultOperator = null;
		try {
			resultOperator = DAOFactory.getInstance().getOperatorDAO().getOperatorById(id);
		} catch (SQLException sqle) {
			System.out.println("DB Error: " + sqle.getMessage());												
		}
		
		assertNotNull("Readed operator is null", resultOperator);
		if (resultOperator == null) {
			return ; // далее бессмысленно
		}

		// проверяем соответствие одного из полей

		String resultFIO = "";
		resultFIO = resultOperator.getFIO();
		assertEquals("Readed fio not equals writen", initFIO, resultFIO);

	}

}