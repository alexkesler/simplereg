package org.kesler.simplereg.dao;

import java.sql.SQLException;
import java.util.List;

import org.kesler.simplereg.logic.Applicator;

public interface ApplicatorDAO {
	public void addApplicator(Applicator applicator) throws SQLException;
	public void updateApplicator(Applicator applicator) throws SQLException;
	public Applicator getApplicatorById(Long id) throws SQLException;
	public List getAllApplicators() throws SQLException;
	public void deleteApplicator(Applicator applicator) throws SQLException;		
}

