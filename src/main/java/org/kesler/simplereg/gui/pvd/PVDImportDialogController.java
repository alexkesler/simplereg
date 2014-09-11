package org.kesler.simplereg.gui.pvd;


import org.kesler.simplereg.gui.util.InfoDialog;
import org.kesler.simplereg.gui.util.ProcessDialog;
import org.kesler.simplereg.pvdimport.PVDReaderException;
import org.kesler.simplereg.pvdimport.ReaderListener;
import org.kesler.simplereg.pvdimport.domain.*;
import org.kesler.simplereg.pvdimport.support.CauseReader;
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

    private ProcessDialog processDialog;

    private final List<Cause> causes;
    private final List<Cause> allCauses;
    private String filterString;

    private PVDImportDialogController() {
        causes = new ArrayList<Cause>();
        allCauses = new ArrayList<Cause>();
    }

    public static synchronized PVDImportDialogController getInstance() { return instance; }

    List<Cause> getCauses() { return causes; }

    public Cause showSelectDialog(JFrame parentFrame, int lastNum) {
        causes.clear();
        dialog = new PVDImportDialog(parentFrame, this);
        filterString = "";
        processDialog = new ProcessDialog(dialog);
        processDialog.showProcess("Читаю данные из ПК ПВД");

        readCauses(lastNum);
        dialog.setVisible(true);


        if(dialog.getResult()==PVDImportDialog.OK) {
             return readCause(dialog.getSelectedCause());
        } else {
            return null;
        }
    }

    public Cause showSelectDialog(JFrame parentFrame) {
        causes.clear();
        dialog = new PVDImportDialog(parentFrame, this);
        filterString = "";
        processDialog = new ProcessDialog(dialog);
        processDialog.showProcess("Читаю данные из ПК ПВД");
        readCausesThisDay();
        dialog.setVisible(true);


        if(dialog.getResult()==PVDImportDialog.OK) {
            return readCause(dialog.getSelectedCause());
        } else {
            return null;
        }
    }

    private Cause readCause(Cause cause) {
        try {
            CauseReader.readCause(cause);
        } catch (PVDReaderException ex) {
            JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return cause;
    }

    private void readCauses (int lastNum) {
        packagesReader = new PackagesReader(this, lastNum);
        packagesReader.readCausesInSeparateThread();
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
        packagesReader.readCausesInSeparateThread();

    }

//    private void readCausesInSeparateThread(final int lastNum) {
//        Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread t, Throwable e) {
//                processDialog.hideProcess();
//                JOptionPane.showMessageDialog(dialog,"Ошибка: " + e.getMessage(),"Ошибка",JOptionPane.ERROR_MESSAGE);
//            }
//        };
//        Thread readerThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                readCauses(lastNum);
//            }
//        });
//        readerThread.setUncaughtExceptionHandler(handler);
//        readerThread.start();
//    }
//
//    private void readCausesThisDayInSeparateThread() {
//        Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread t, Throwable e) {
//                processDialog.hideProcess();
//                JOptionPane.showMessageDialog(dialog,"Ошибка: " + e.getMessage(),"Ошибка",JOptionPane.ERROR_MESSAGE);
//            }
//        };
//        Thread readerThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                readCausesThisDay();
//            }
//        });
//        readerThread.setUncaughtExceptionHandler(handler);
//        readerThread.start();
//    }

    void setFilterString(String filterString) {
        this.filterString = filterString.toLowerCase();
    }

    void filterCauses() {
        causes.clear();
        for (Cause cause: allCauses) {
            if (cause.getRegnum().toLowerCase().contains(filterString)) {
                causes.add(cause);
            }
        }
        dialog.update();
    }

    @Override
    public void readComplete() {
        allCauses.clear();
        for (org.kesler.simplereg.pvdimport.domain.Package pack: packagesReader.getPackages()) {
            allCauses.addAll(pack.getCauses());
        }
        filterCauses();
        dialog.update();
        processDialog.hideProcess();
    }
}
