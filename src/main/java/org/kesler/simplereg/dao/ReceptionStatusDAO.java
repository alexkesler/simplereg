package org.kesler.simplereg.dao;

import java.util.List;

import org.kesler.simplereg.logic.reception.ReceptionStatus;

public interface ReceptionStatusDAO {
	public List<ReceptionStatus> getAllStatuses();
	public Long addReceptionStatus(ReceptionStatus receptionStatus);
	public void updateReceptionStatus(ReceptionStatus receptionStatus);
	public void removeReceptionStatus(ReceptionStatus receptionStatus);
}