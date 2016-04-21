package org.kesler.simplereg.dao.impl;

import org.kesler.simplereg.dao.ReceptionStatusChangeDAO;
import org.kesler.simplereg.logic.reception.ReceptionStatusChange;

/**
 * Created by alex on 20.04.16.
 */
public class ReceptionStatusChangeDAOImpl extends GenericDAOImpl<ReceptionStatusChange> implements ReceptionStatusChangeDAO {
    public ReceptionStatusChangeDAOImpl() {
        super(ReceptionStatusChange.class);
    }


}
