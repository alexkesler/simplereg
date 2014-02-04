package org.kesler.simplereg.gui.fias;

import com.alee.laf.filechooser.WebFileChooser;
import net.miginfocom.swing.MigLayout;
import org.kesler.simplereg.fias.jaxb.JAXBFIASListener;
import org.kesler.simplereg.fias.jaxb.JAXBFIASLoader;
import org.kesler.simplereg.gui.AbstractDialog;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Алексей on 04.02.14.
 */
public class FIASDialog extends AbstractDialog implements JAXBFIASListener {

    private JLabel statusLabel;


    public FIASDialog(JFrame parentFrame, boolean modal) {
        super(parentFrame, modal);
        createGUI();
        setSize(250,150);
        setLocationRelativeTo(parentFrame);
    }

    private void createGUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Панель данных
        JPanel dataPanel = new JPanel(new MigLayout("fillx, nogrid"));


        JButton loadFIASButton = new JButton("Загрузить ФИАС");
        loadFIASButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFIAS();
            }
        });

        statusLabel = new JLabel(" ");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());

        dataPanel.add(loadFIASButton, "wrap");
        dataPanel.add(statusLabel, "growx");

        // Панель кнопок
        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton("ОК");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        buttonPanel.add(okButton);


        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel,BorderLayout.SOUTH);


        this.setContentPane(mainPanel);

    }

    private void loadFIAS() {
        WebFileChooser fileChooser = new WebFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Файлы XML","xml");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(filter);

        int result = fileChooser.showOpenDialog(currentDialog);
        if(result == WebFileChooser.APPROVE_OPTION) {
            File file =  fileChooser.getSelectedFile();
            JAXBFIASLoader loader = new JAXBFIASLoader(this);
            loader.loadFIAS(file);
        }
   }

    public void jaxbMessage(String message) {
        statusLabel.setText(message);
    }

}
