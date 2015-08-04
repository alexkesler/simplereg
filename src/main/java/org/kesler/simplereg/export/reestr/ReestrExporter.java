package org.kesler.simplereg.export.reestr;

import com.alee.laf.filechooser.WebFileChooser;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.kesler.simplereg.gui.util.ProcessDialog;
import org.kesler.simplereg.logic.Reception;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Корневой класс для классов формирования ведомостей
 */
public abstract class ReestrExporter {

    protected List <Reception> receptions;
    protected ProcessDialog processDialog;

    protected SXSSFWorkbook wb = new SXSSFWorkbook(100);

    protected Type type = null;


    public Type getType() {
        return type;
    }


    public void export(List<Reception> receptions) {
        if(receptions.size()==0) {
            JOptionPane.showMessageDialog(null,"Список пуст","Ошибка",JOptionPane.WARNING_MESSAGE);
            return;
        }
        this.receptions = receptions;

        Thread processThread = new Thread(new ProcessWorker());
        processDialog = new ProcessDialog();
        processDialog.showProcess("Обрабатываю данные");

        processThread.start();
    }

    protected abstract void prepare();



    private void save() {
        WebFileChooser fileChooser = new WebFileChooser();
        //fileChooser.setMultiSelectionEnabled(false);
        FileFilter fileFilter = new FileNameExtensionFilter("Excel file", "xlsx");
        fileChooser.setFileFilter(fileFilter);

        File file = null;

        file = fileChooser.showSaveDialog();

        if (file == null) {
            return;
        }

        String filePath = file.getPath();
        if(filePath.indexOf(".xlsx") == -1) {
            filePath += ".xlsx";
            file = new File(filePath);
        }

        if (file.exists()) {

        }

        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            wb.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Открытие файла:

        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(file);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }




    }




    class ProcessWorker extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() throws Exception {
            prepare();
            return null;
        }

        @Override
        protected void done() {
            super.done();
            if(processDialog!=null) processDialog.hideProcess();
            try {
                get();
                save();
            } catch (InterruptedException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ошибка: " + e, "Ошибка", JOptionPane.ERROR_MESSAGE);
            } catch (ExecutionException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ошибка: " + e, "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public enum Type {
        SELECTED_COLUMNS("По выбранным столбцам"),
        FOR_ARCHIVE("Ведомость передачи в архив"),
        FOR_RETURN("Ведомость возврата"),
        FOR_TRANSMIT("Ведоомсть приема-передачи");

        private String desc;

        Type(String desc) {
            this.desc = desc;
        }


        @Override
        public String toString() {
            return desc;
        }
    }


}
