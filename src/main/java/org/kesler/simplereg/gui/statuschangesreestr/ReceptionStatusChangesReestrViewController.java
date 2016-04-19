package org.kesler.simplereg.gui.statuschangesreestr;

import org.kesler.simplereg.service.SimpleregService;
import org.kesler.simplereg.service.SimpleregServiceImpl;

import javax.swing.*;

/**
 * Created by alex on 20.04.16.
 */
public class ReceptionStatusChangesReestrViewController {

    private static ReceptionStatusChangesReestrViewController instance;

    private SimpleregService simpleregService = new SimpleregServiceImpl();

    private ReceptionStatusChangesReestrView view;

    public static ReceptionStatusChangesReestrViewController getInstance() {
        if (instance==null) instance = new ReceptionStatusChangesReestrViewController();
        return instance;
    }

    private ReceptionStatusChangesReestrViewController(){}


    public void showView(JFrame parentFrame) {
        if (view==null) view = new ReceptionStatusChangesReestrView(parentFrame, this);
        view.setVisible(true);
    }

}
