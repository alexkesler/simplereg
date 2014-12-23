package org.kesler.simplereg.gui.template;

import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.logic.template.Template;
import org.kesler.simplereg.util.OptionsUtil;
import org.kesler.simplereg.util.ResourcesUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;


public class TemplateDialog extends AbstractDialog {
    private final Logger log = Logger.getLogger(this.getClass());

    private JTextField nameTextField;
    private JTextField templateTextField;
    private JCheckBox byDefaultCheckBox;
    private byte[] data;

    private Template template;

    private TemplateDialog(JDialog parentDialog) {
        super(parentDialog, "Шаблон", true);
        createGui();
        pack();
        setSize(400,getHeight());
        setLocationRelativeTo(parentDialog);
    }

    private void createGui() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel dataPanel = new JPanel(new MigLayout());

        nameTextField = new JTextField();
        templateTextField = new JTextField();
        templateTextField.setEnabled(false);
        JButton uploadTemplateButton = new JButton(ResourcesUtil.getIcon("data_up.png"));
        uploadTemplateButton.setToolTipText("Загрузить в базу данных");
        uploadTemplateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadTemplate();
            }
        });

        JButton downloadTemplateButton = new JButton(ResourcesUtil.getIcon("data_down.png"));
        downloadTemplateButton.setToolTipText("Сохранить из базы данных");
        downloadTemplateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downloadTemplate();
            }
        });

        final JButton viewTemplateButton = new JButton(ResourcesUtil.getIcon("magnifier.png"));
        viewTemplateButton.setToolTipText("Просмотр");
        viewTemplateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewTemplate();
            }
        });

        byDefaultCheckBox = new JCheckBox();

        dataPanel.add(new JLabel("Наименование: "));
        dataPanel.add(nameTextField, "pushx, growx, wrap");
        dataPanel.add(new JLabel("Шаблон"));
        dataPanel.add(templateTextField,"span, split 4, growx");
        dataPanel.add(uploadTemplateButton);
        dataPanel.add(downloadTemplateButton);
        dataPanel.add(viewTemplateButton);
        dataPanel.add(new JLabel("По умолчанию"));
        dataPanel.add(byDefaultCheckBox);

        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (storeTemplate()) {
                    result= OK;
                    setVisible(false);
                }
            }
        });

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = CANCEL;
                setVisible(false);
            }
        });

        buttonPanel.add(okButton);

        mainPanel.add(dataPanel,BorderLayout.CENTER);
        mainPanel.add(buttonPanel,BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }


    public static boolean showModal(JDialog parentDialog, Template template) {
        TemplateDialog dialog = new TemplateDialog(parentDialog);
        dialog.template = template;
        dialog.loadContent();
        dialog.setVisible(true);
        return  dialog.getResult()==OK;
    }

    private void loadContent() {
        nameTextField.setText(template.getName());
        data = template.getData();
        templateTextField.setText(data==null?"Не загружен":"Загружен");
        byDefaultCheckBox.setSelected(template.getByDefault() == null ? false : template.getByDefault());
    }

    private boolean storeTemplate() {

        if (nameTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(currentDialog,
                    "Наименование не может быть пустым",
                    "Внимание",
                    JOptionPane.WARNING_MESSAGE);
            return false;

        }
        template.setName(nameTextField.getText());

        if (data==null) {
            JOptionPane.showMessageDialog(currentDialog,
                    "Загрузите файл шаблона",
                    "Внимание",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        template.setData(data);

        template.setByDefault(byDefaultCheckBox.isSelected());
        return true;
    }

    private void uploadTemplate() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.resetChoosableFileFilters();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Файлы docx", "docx"));

        File file = null;
        if (fileChooser.showOpenDialog(currentDialog)==JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        }
        if (file!=null) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                int length = fileInputStream.available();
                data = new byte[length];
                fileInputStream.read(data);
            } catch (FileNotFoundException e) {
                log.error("Template file not found",e);
                JOptionPane.showMessageDialog(currentDialog,
                        "Файл шаблона не найден",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                log.error("Cant read template", e);
                JOptionPane.showMessageDialog(currentDialog,
                        "Ошибка при чтении файла шаблона: "+ e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error("Cant close InputStream", e);
                }
            }


            templateTextField.setText(data==null?"Не загружен":"Загружен");

        }
    }

    private void downloadTemplate() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.resetChoosableFileFilters();
        if (fileChooser.showSaveDialog(currentDialog)==JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            File file = new File(filePath+".docx");
            FileOutputStream fileOutputStream = null;
            try {
                file.createNewFile();
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(currentDialog,
                        "Файл шаблона не найден",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                log.error("Cant write template", e);
                JOptionPane.showMessageDialog(currentDialog,
                        "Ошибка при записи файла шаблона: "+ e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    log.error("Cant close OutputStream", e);
                }
            }
        }





    }

    private void viewTemplate() {
        if (template==null) return;


        OptionsUtil.createOutDir();

        String filePath = OptionsUtil.getCurrentDir()+"out"+OptionsUtil.getDirSeparator()+template.getName()+".docx";

        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            log.error("Can't create file: " + filePath,e);
            JOptionPane.showMessageDialog(this,
                    "Не удалось создать файл: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            log.error("Can't find file for open",e);
            JOptionPane.showMessageDialog(this,
                    "Не удалось открыть файл для записи: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }

        if (fileOutputStream != null) {
            try {
                fileOutputStream.write(template.getData());
            } catch (IOException e) {
                log.error("Cant write file", e);
                JOptionPane.showMessageDialog(this,
                        "Ошибка при записи файла: "+e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    log.error("Cant close OutputStream", e);
                }
            }
        }


        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();

            //Открытие файла:

            try {
                desktop.open(file);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }


}
