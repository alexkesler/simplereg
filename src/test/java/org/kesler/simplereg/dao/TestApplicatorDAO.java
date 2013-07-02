package org.kesler.simplereg.dao;

import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.SQLException;

//import org.kesler.simplereg.dao.impl.ApplicatorDAOImpl;
import org.kesler.simplereg.logic.Applicator;

public class TestApplicatorDAO {

	@Test
	public void testAddRead() {
		Applicator a = new Applicator();

		a.setFIO("Иванов Иван Иванович");

		try {
			DAOFactory.getInstance().getApplicatorDAO().addApplicator(a);
		} catch (SQLException sqle) {
			System.out.println("DB Error: " + sqle.getMessage());						
		}
	}
}