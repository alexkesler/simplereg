package org.kesler.simplereg.dao;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

import org.kesler.simplereg.logic.Operator;

public class TestOperatorDAO {

	@Test
	public void testSaveOperators() {

		// Создаем оператора
		Operator operator = new Operator();
		// Определяем для него статус - новый оператор
		operator.setState(Operator.NEW_STATE);

		List<Operator> operators = new ArrayList<Operator>();
		operators.add(operator);

		// Сохраняем коллекцию операторов
		try {
			DAOFactory.getInstance().getOperatorDAO().saveOperators(operators);
		} catch (SQLException sqle) {
			System.out.println("DB Error: " + sqle.getMessage());									
		}

		// Проверяем получение id при сохранении
		Long id = operator.getId();
		assertNotNull("Writen Operator dont get id", id);

		// Проверяем измемение статуса при сохранении
		int resultState = operator.getState();
		assertEquals("Operator saved, but state not changed",resultState,Operator.SAVED_STATE);


		// Читаем список операторов 
		List<Operator> resultOperators = null;
		try {
			resultOperators = DAOFactory.getInstance().getOperatorDAO().getAllOperators();
		} catch (SQLException sqle) {
			System.out.println("DB Error: " + sqle.getMessage());												
		}
		
			
		// Необходимо добавить проверку на чтение списка операторов из БД

		assertTrue(resultOperators.contains(operator));


	}

}