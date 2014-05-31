package org.kesler.simplereg.gui.main;

import net.miginfocom.swing.MigLayout;
import org.kesler.simplereg.Version;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by alex on 31.05.14.
 */
public class AboutDialog extends JDialog{

    public AboutDialog(JFrame parentFrame) {
        super(parentFrame,"О программе", true);
        createGUI();
        setLocationRelativeTo(parentFrame);
    }

    private void createGUI() {
        JPanel mainPanel = new JPanel(new MigLayout());

        JLabel versionLabel = new JLabel(Version.getVersion());
        JLabel releaseLabel = new JLabel(Version.getReleaseDate());

        JButton okButton = new JButton("Ок");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        mainPanel.add(new JLabel("Версия: "));
        mainPanel.add(versionLabel, "wrap");
        mainPanel.add(new JLabel("Дата сборки: "));
        mainPanel.add(releaseLabel,"wrap");
        mainPanel.add(okButton,"span, center");

        setContentPane(mainPanel);
        pack();
    }

    public void showDialog() {
        setVisible(true);
    }
}
