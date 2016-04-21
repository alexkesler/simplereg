package org.kesler.simplereg.dao;

import java.util.List;
import java.util.Date;

import org.kesler.simplereg.logic.Reception;

public interface ReceptionDAO extends GenericDAO<Reception>{
	void addReception(Reception r);
	void updateReception(Reception r);
	Reception getReceptionById(Long id);
	List<Reception> getAllReceptions();
	List<Reception> getReceptionsByOpenDate(Date from, Date to);
	List<Reception> getReceptionsByRosreestrCode(String code);
	void removeReception(Reception r);
    Integer getLastPVDPackageNum();
}