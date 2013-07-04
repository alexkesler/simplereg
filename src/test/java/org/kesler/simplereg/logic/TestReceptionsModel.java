package org.kesler.simplereg.logic;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Ignore;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class TestReceptionsModel {

	@Test
	public void testCreate() {
		ArrayList<Reception> receptions = new ArrayList<Reception>();
		ReceptionsModel receptionsModel = new ReceptionsModel(receptions);
		List<Reception> resultList = receptionsModel.getReceptions();
		assertEquals("Wrong List of receptions", receptions, resultList);
	}

	@Test
	public void testCreateEmpty() {
		ReceptionsModel receptionsModel = new ReceptionsModel();
		List<Reception> resultList = receptionsModel.getReceptions();
		assertNotNull("List is null", resultList);
	}

	@Test
	public void testAddReception() {
		// готовим данные для приема
		ReceptionsModel receptionsModel = new ReceptionsModel();
		Service service = new Service("Simple service");
		Applicator applicator = new Applicator("Петров Петр Петрович");
		Operator operator = new Operator("Операторов оператор");
		Date date = new Date();
		Reception reception = new Reception();
		reception.setService(service);
		reception.setApplicator(applicator);
		reception.setOperator(operator);
		reception.setOpenDate(date);
		// добавляем
		receptionsModel.addReception(reception);

		// получаем лист приемов и проверяем наличие нашего объекта
		List<Reception> resultList = receptionsModel.getReceptions();
		boolean resultBoolean = resultList.contains(reception);
		assertTrue("Reception don't added", resultBoolean);

	}

	@Ignore
	@Test
	public void testSaveRead() {
		ReceptionsModel receptionsModel = new ReceptionsModel();
	}

}