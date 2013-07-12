package org.kesler.simplereg.dao;

import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.SQLException;

import org.kesler.simplereg.logic.Applicator;

public class TestApplicatorDAO {

	@Test
	public void testAddRead() {
		Applicator a = new Applicator();

		a.setFirstName("Иван");
		a.setParentName("Иванович");
		a.setSurName("Иванов");

		try {
			DAOFactory.getInstance().getApplicatorDAO().addApplicator(a);
		} catch (SQLException sqle) {
			System.out.println("DB Error: " + sqle.getMessage());						
		}

		// проверяем получение id при сохранении 
		Long id = a.getId();
		assertNotNull("Writen Applicator dont get id", id);

		// проверяем чтение 
		Applicator resultApplicator = null;
		try {
			resultApplicator = DAOFactory.getInstance().getApplicatorDAO().getApplicatorById(id);
		} catch (SQLException sqle) {
			System.out.println("DB Error: " + sqle.getMessage());						
		}

		assertNotNull("Readed Applicator is null", resultApplicator);

		// проверяем соответствие полей сохраненного и прочитанного объектов
		String resultFirstName = resultApplicator.getFirstName();
		assertEquals("Applicator firstName not the same", "Иван", resultFirstName);
		String resultParentName = resultApplicator.getParentName();
		assertEquals("Applicator parentName not the same", "Иванович", resultParentName);
		String resultSurName = resultApplicator.getSurName();
		assertEquals("Applicator surName not the same", "Иванов", resultSurName);
		
	}

}