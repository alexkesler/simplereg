package org.kesler.simplereg.logic;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Ignore;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.kesler.simplereg.logic.operator.Operator;
import org.kesler.simplereg.logic.applicator.Applicator;

public class TestReceptionsModel {

	// @Test
	// public void testCreate() {
	// 	ArrayList<Reception> receptions = new ArrayList<Reception>();
	// 	ReceptionsModel receptionsModel = new ReceptionsModel();
	// 	List<Reception> resultList = receptionsModel.getReceptions();
	// 	assertEquals("Wrong List of receptions", receptions, resultList);
	// }

	@Test
	public void testCreateEmpty() {
		ReceptionsModel receptionsModel = ReceptionsModel.getInstance();
		List<Reception> resultList = receptionsModel.getReceptions();
		assertNotNull("List is null", resultList);
	}

	@Ignore
	@Test
	public void testAddReception() {
		// готовим данные для приема
		ReceptionsModel receptionsModel = ReceptionsModel.getInstance();
		Service service = new Service();
		List<Applicator> applicators = new ArrayList<Applicator>();
		Operator operator = new Operator(); 
		Date date = new Date();
		Reception reception = new Reception();
		reception.setService(service);
		reception.setApplicators(applicators);
		reception.setOperator(operator);
		reception.setOpenDate(date);
		// добавляем
		receptionsModel.addReception(reception);

		// получаем лист приемов и проверяем наличие нашего объекта
		List<Reception> resultList = receptionsModel.getReceptions();
		boolean resultBoolean = resultList.contains(reception);
		assertTrue("Reception don't added", resultBoolean);

	}

	@Test
	public void testSaveRead() {
		ReceptionsModel receptionsModel = ReceptionsModel.getInstance();
	}

}