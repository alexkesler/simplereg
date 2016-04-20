package org.kesler.simplereg.service;

import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.reception.ReceptionStatusChange;

import java.util.List;

/**
 * Created by alex on 19.04.16.
 */
public interface SimpleRegService {

    List<Reception> findAllReceptions();
    List<ReceptionStatusChange> findAllStatusChanges();

}
