package org.kesler.simplereg.dao;

import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.SQLException;

import org.kesler.simplereg.logic.Applicator;

public class TestApplicatorDAO {

	@Test
	public void testAddRead() {
		Applicator a = new Applicator();

		String initFIO = "Иванов Иван Иванович";
		a.setFIO(initFIO);

		try {
			DAOFactory.getInstance().getApplicatorDAO().addApplicator(a);
		} catch (SQLException sqle) {
			System.out.println("DB Error: " + sqle.getMessage());						
		}

		// проверяем получение id при сохранении 
		Long id = a.getId();
		assertNotNull("Writen Applicator dont get id", id);
		if (id == null) {
			return ; // Дальше бессмысленно - выходим
		}

		// проверяем чтение 
		Applicator resultApplicator = null;
		try {
			resultApplicator = DAOFactory.getInstance().getApplicatorDAO().getApplicatorById(id);
		} catch (SQLException sqle) {
			System.out.println("DB Error: " + sqle.getMessage());						
		}

		assertNotNull("Readed Applicator is null", resultApplicator);

		if (resultApplicator == null) {
			return ; // Дальше бессмысленно - выходим
		}

		// проверяем соответствие полей сохраненного и прочитанного объектов
		String resultApplicatorFIO = "";
		resultApplicatorFIO = resultApplicator.getFIO();
		assertEquals("Applicator fio not the same", initFIO, resultApplicatorFIO);

	}

}