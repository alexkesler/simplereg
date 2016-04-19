package org.kesler.simplereg.service;

import org.kesler.simplereg.dao.ReceptionStatusChangeDAO;
import org.kesler.simplereg.dao.impl.ReceptionStatusChangeDAOImpl;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionStatusChange;

import java.util.List;

/**
 * Created by alex on 20.04.16.
 */
public class SimpleregServiceImpl implements SimpleregService {

    private ReceptionStatusChangeDAO receptionStatusChangeDAO = new ReceptionStatusChangeDAOImpl();

    @Override
    public List<Reception> findAllReceptions() {
        return null;
    }

    @Override
    public List<ReceptionStatusChange> findAllStatusChanges() {
        return receptionStatusChangeDAO.getAllItems();
    }
}
