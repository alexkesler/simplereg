package org.kesler.simplereg.gui.statuschangesreestr;

import org.apache.log4j.Logger;
import org.kesler.simplereg.logic.reception.ReceptionStatusChange;
import org.kesler.simplereg.service.SimpleRegService;
import org.kesler.simplereg.service.SimpleRegServiceImpl;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by alex on 20.04.16.
 */
public class ReceptionStatusChangesReestrViewController {
    private Logger log = Logger.getLogger(getClass().getSimpleName());

    private static ReceptionStatusChangesReestrViewController instance;

    private SimpleRegService simpleregService = new SimpleRegServiceImpl();

    private ReceptionStatusChangesReestrView view;

    public static ReceptionStatusChangesReestrViewController getInstance() {
        if (instance==null) instance = new ReceptionStatusChangesReestrViewController();
        return instance;
    }

    private ReceptionStatusChangesReestrViewController(){}


    public void showView(JFrame parentFrame) {
        if (view==null) view = new ReceptionStatusChangesReestrView(parentFrame, this);
        updateReceptionStatusChanges();
        view.setVisible(true);
    }


    public void updateReceptionStatusChanges() {
        log.info("Updating statuschanges...");
        ReceptionStatusChangesUpdater updater = new ReceptionStatusChangesUpdater();
        view.showProgress();
        updater.execute();
    }

    class ReceptionStatusChangesUpdater extends SwingWorker<List<ReceptionStatusChange>, Void> {
        @Override
        protected List<ReceptionStatusChange> doInBackground() throws Exception {
            return simpleregService.findAllStatusChanges();
        }

        @Override
        protected void done() {
            super.done();
            view.hideProgress();

            try {
                view.setReceptionStatusChanges(get());
                log.info("Updating statuschanges complete.");
            } catch (InterruptedException e) {
                log.error(e);
            } catch (ExecutionException e) {
                log.error(e);
            }
        }
    }

}
