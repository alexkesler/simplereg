package org.kesler.simplereg.gui.pvd;


import org.kesler.simplereg.pvdimport.ReaderListener;
import org.kesler.simplereg.pvdimport.domain.*;
import org.kesler.simplereg.pvdimport.support.PackagesReader;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PVDImportDialogController implements ReaderListener{

    private static PVDImportDialogController instance = new PVDImportDialogController();
    private PackagesReader packagesReader;
    private PVDImportDialog dialog;

    private final List<Cause> causes;

    private PVDImportDialogController() {
        causes = new ArrayList<Cause>();
    }

    public static synchronized PVDImportDialogController getInstance() { return instance; }

    List<Cause> getCauses() { return causes; }

    public Cause showSelectDialog(JFrame parentFrame, int lastNum) {
        causes.clear();
        dialog = new PVDImportDialog(parentFrame, this);
        readCausesInSeparateThread(lastNum);
        dialog.setVisible(true);
        if(dialog.getResult()==PVDImportDialog.OK) {
            return dialog.getSelectedCause();
        } else {
            return null;
        }
    }

    public Cause showSelectDialog(JFrame parentFrame) {
        causes.clear();
        dialog = new PVDImportDialog(parentFrame, this);
        readCausesThisDayInSeparateThread();
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


    private void readCausesThisDay () {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,1);
        Date begDate = calendar.getTime();
        calendar.set(Calendar.HOUR,23);
        calendar.set(Calendar.MINUTE, 59);
        Date endDate = calendar.getTime();
        packagesReader = new PackagesReader(this, begDate, endDate);
        packagesReader.readFullInSeparateThread();
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

    private void readCausesThisDayInSeparateThread() {
        Thread readerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                readCausesThisDay();
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
