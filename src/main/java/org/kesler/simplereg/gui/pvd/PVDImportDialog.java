package org.kesler.simplereg.gui.pvd;


import org.kesler.simplereg.gui.AbstractDialog;

import javax.swing.*;

public class PVDImportDialog extends AbstractDialog{
    PVDImportDialog(JDialog parentDialog) {
        super(parentDialog, true);
        createGUI();
        pack();
        setLocationRelativeTo(parentDialog);
    }

    private void createGUI() {
        JPanel mainPanel = new JPanel();

        setContentPane(mainPanel);
    }

}
