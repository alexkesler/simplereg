package org.kesler.simplereg.gui.reestr.filter;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.gui.applicator.ULListDialogController;
import org.kesler.simplereg.logic.UL;
import org.kesler.simplereg.logic.reception.filter.ULReceptionsFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ULReceptionsFilterDialog extends ReceptionsFilterDialog {

    private UL ul;
    private JLabel ulLabel;

    public ULReceptionsFilterDialog(JFrame frame) {
        super(frame, "Фильтр по юр. лицу");
        setSize(150,100);
    }

    public ULReceptionsFilterDialog(JFrame frame, ULReceptionsFilter filter) {
        super(frame, "Фильтр по юр. лицу", filter);
        setSize(150,100);
    }

    @Override
    protected void createReceptionsFilter() {

//        receptionsFilter = new ULReceptionsFilter(ul);
    }

    @Override
    protected JPanel createDataPanel() {
        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        ulLabel = new JLabel();
        ulLabel.setBorder(BorderFactory.createEtchedBorder());

        JButton selectFLButton = new JButton("Выбрать");
        selectFLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ul = ULListDialogController.getInstance().openSelectDialog(currentDialog);
                if (ul!=null) ulLabel.setText(ul.getShortName());
            }
        });

        dataPanel.add(new JLabel("Физ лицо:"));
        dataPanel.add(ulLabel);
        dataPanel.add(selectFLButton);

        return dataPanel;
    }

    @Override
    protected void loadGUIDataFromReceptionsFilter() {

        if (receptionsFilter != null) {
            ULReceptionsFilter ulReceptionsFilter = (ULReceptionsFilter) receptionsFilter;
            ul = ulReceptionsFilter.getUl();
            ulLabel.setText(ul.getShortName());
        } else {
            ulLabel.setText("Не определено");
        }


    }


    @Override
    protected boolean readReceptionsFilterFromGUIData() {

        if (ul == null) {
            JOptionPane.showMessageDialog(currentDialog, "Юр лицо не выбрано", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        receptionsFilter = new ULReceptionsFilter(ul);

        return true;

    }

}