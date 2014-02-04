package org.kesler.simplereg.dao;

import org.kesler.simplereg.fias.FIASRecord;

import java.util.List;

public interface FIASRecordDAO extends DAOObservable{
	
    public void addRecords(List<FIASRecord> records);

	public List<FIASRecord> getAllRecords();

    public void removeAllRecords();

}