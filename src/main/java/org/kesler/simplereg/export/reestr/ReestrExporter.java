package org.kesler.simplereg.export.reestr;

import com.alee.laf.filechooser.WebFileChooser;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.kesler.simplereg.gui.util.ProcessDialog;
import org.kesler.simplereg.logic.Reception;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Корневой класс для классов формирования ведомостей
 */
public abstract class ReestrExporter {

    List <Reception> receptions;
    ProcessDialog processDialog;

    protected SXSSFWorkbook wb = new SXSSFWorkbook(100);

    public abstract ReestrExportEnum getEnum();

    public void export(List<Reception> receptions) {
        if(receptions.size()==0) {
            JOptionPane.showMessageDialog(null,"Список пуст","Ошибка",JOptionPane.WARNING_MESSAGE);
            return;
        }
        this.receptions = receptions;
        Thread processThread = new Thread(new Runnable() {
            public void run() {
                process();
            }
        });
        processDialog = new ProcessDialog();
        processDialog.showProcess("Обрабатываю данные");

        processThread.start();
    }

    private void process() {
        prepare();
        if(processDialog!=null) processDialog.hideProcess();
        save();
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

    }


}
