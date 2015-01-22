package org.kesler.simplereg.gui.pvd;


import org.apache.log4j.Logger;
import org.kesler.simplereg.gui.util.ProcessDialog;
import org.kesler.simplereg.pvdimport.domain.Cause;
import org.kesler.simplereg.pvdimport.domain.Package;
import org.kesler.simplereg.pvdimport.support.PackagesReader;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PVDImportDialogController {
    private final Logger log = Logger.getLogger(this.getClass().getSimpleName());

    private static PVDImportDialogController instance = new PVDImportDialogController();
    private PackagesReader packagesReader;
    private PVDImportDialog dialog;

    private ProcessDialog processDialog;

    private final List<Cause> causes;
    private final List<Cause> allCauses;
    private String filterString;
    private PVDImportDialog.Period selectedPeriod;

    private PVDImportDialogController() {
        causes = new ArrayList<Cause>();
        allCauses = new ArrayList<Cause>();
        processDialog = new ProcessDialog(dialog);
    }

    public static synchronized PVDImportDialogController getInstance() { return instance; }

    List<Cause> getCauses() { return causes; }

    public Cause showSelectDialog(JFrame parentFrame, int lastNum) {
        causes.clear();
        dialog = new PVDImportDialog(parentFrame, this);
        filterString = "";
        selectedPeriod = PVDImportDialog.Period.CURRENT_DAY;

        readCauses(lastNum);
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
        filterString = "";
        selectedPeriod = PVDImportDialog.Period.CURRENT_DAY;
        readCausesByPeriod();
        dialog.setVisible(true);


        if(dialog.getResult()==PVDImportDialog.OK) {
            return dialog.getSelectedCause();
        } else {
            return null;
        }
    }


    private void readCauses (int lastNum) {
        packagesReader = new PackagesReader(lastNum);
        new PackagesReaderWorker(packagesReader).go();
    }

    public void readCausesByPeriod() {
        processDialog = new ProcessDialog(dialog);

        switch (selectedPeriod) {
            case CURRENT_DAY:
                readCausesThisDay();
                break;
            case LAST_3_DAYS:
                readCausesLastThreeDays();
                break;
            case LAST_WEEK:
                readCausesLastWeek();
                break;
            default:
                readCausesThisDay();
        }
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
        packagesReader = new PackagesReader(begDate, endDate);
        new PackagesReaderWorker(packagesReader).go();

    }

    private void readCausesLastThreeDays () {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH,-2);
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,1);
        Date begDate = calendar.getTime();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR,23);
        calendar.set(Calendar.MINUTE, 59);
        Date endDate = calendar.getTime();
        packagesReader = new PackagesReader(begDate, endDate);
        new PackagesReaderWorker(packagesReader).go();

    }

    private void readCausesLastWeek () {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,1);
        Date begDate = calendar.getTime();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR,23);
        calendar.set(Calendar.MINUTE, 59);
        Date endDate = calendar.getTime();
        packagesReader = new PackagesReader(begDate, endDate);
        new PackagesReaderWorker(packagesReader).go();

    }


    void setSelectedPeriod(PVDImportDialog.Period selectedPeriod) {
        this.selectedPeriod = selectedPeriod;
    }
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


    class PackagesReaderWorker extends SwingWorker<List<Package>, Void> {
        private PackagesReader reader;

        void go() {
            processDialog.showProcess("Читаю данные из ПК ПВД");
            dialog.disableControls();
            execute();

        }

        PackagesReaderWorker(PackagesReader reader) {
            this.reader = reader;
        }
        @Override
        protected List<Package> doInBackground() throws Exception {
            reader.readCauses();

            return reader.getPackages();
        }

        @Override
        protected void done() {
            try {
                processDialog.hideProcess();
                List<Package> packages = get();
                allCauses.clear();
                for (org.kesler.simplereg.pvdimport.domain.Package pack: packages) {
                    allCauses.addAll(pack.getCauses());
                }
                filterCauses();
                dialog.update();
                dialog.enableControls();

            } catch (InterruptedException e) {
                log.error("Interrupted",e);

            } catch (ExecutionException e) {
                log.error("Error reading causes", e);
                JOptionPane.showMessageDialog(dialog,
                        "Ошибка при чтении данных из ПК ПВД: "+e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

 }
