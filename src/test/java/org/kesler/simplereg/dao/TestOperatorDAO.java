package org.kesler.simplereg.dao;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

import org.kesler.simplereg.logic.operator.Operator;

public class TestOperatorDAO {

	@Test
	public void testSaveOperators() {

		// Создаем оператора
		Operator operator = new Operator();
		// Определяем для него статус - новый оператор
		operator.setState(EntityState.NEW);

		List<Operator> operators = new ArrayList<Operator>();
		operators.add(operator);

		// Сохраняем коллекцию операторов
		DAOFactory.getInstance().getOperatorDAO().saveOperators(operators);

		// Проверяем получение id при сохранении
		Long id = operator.getId();
		assertNotNull("Writen Operator dont get id", id);

		// Проверяем измемение статуса при сохранении
		EntityState resultState = operator.getState();
		assertEquals("Operator saved, but state not changed",resultState,EntityState.SAVED);


		// Читаем список операторов 
		List<Operator> resultOperators = null;
		resultOperators = DAOFactory.getInstance().getOperatorDAO().getAllOperators();		
			
		// Необходимо добавить проверку на чтение списка операторов из БД

		assertTrue(resultOperators.contains(operator));


	}

}