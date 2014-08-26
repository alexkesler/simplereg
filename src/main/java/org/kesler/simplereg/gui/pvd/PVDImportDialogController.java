package org.kesler.simplereg.gui.pvd;

import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.pvdimport.ReaderListener;
import org.kesler.simplereg.pvdimport.domain.*;
import org.kesler.simplereg.pvdimport.support.PackagesReader;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PVDImportDialogController implements ReaderListener{

    private static PVDImportDialogController instance = new PVDImportDialogController();
    private PackagesReader packagesReader;
    private PVDImportDialog dialog;

    private final List<Cause> causes;

    private PVDImportDialogController() {
        causes = new ArrayList<Cause>();
    }

    public PVDImportDialogController getInstance() { return instance; }

    List<Cause> getCauses() { return causes; }

    public Cause showSelectDialog(JDialog parentDialog, int lastNum) {
        causes.clear();
        dialog = new PVDImportDialog(parentDialog, this);
        readCausesInSeparateThread(lastNum);
        dialog.setVisible(true);
        if(dialog.getResult()==PVDImportDialog.OK) {
            return dialog.getSelectedCause();
        } else {
            return null;
        }
    }

    private void readCauses (int lastNum) {
        packagesReader = new PackagesReader(this, lastNum);
        packagesReader.read();
    }

    private void readCausesInSeparateThread(final int lastNum) {
        Thread readerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                readCauses(lastNum);
            }
        });
        readerThread.start();
    }

    @Override
    public void readComplete() {
        causes.clear();
        for (org.kesler.simplereg.pvdimport.domain.Package pack: packagesReader.getPackages()) {
            causes.addAll(pack.getCauses());
        }
        dialog.update();
    }
}
