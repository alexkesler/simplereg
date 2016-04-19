package org.kesler.simplereg.gui.statuschangesreestr;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by alex on 20.04.16.
 */
public class ReceptionStatusChangesReestrView extends JFrame {

    private ReceptionStatusChangesReestrViewController controller;

    ReceptionStatusChangesReestrView(JFrame parentFrame, ReceptionStatusChangesReestrViewController controller) {
        super("Реестр изменений состояний");
        this.controller = controller;
        createGUI();
        setLocationRelativeTo(parentFrame);
    }


    private void createGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());


        JPanel dataPanel = new JPanel(new MigLayout("fill"));


        JPanel buttonPanel = new JPanel();



        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }
}
