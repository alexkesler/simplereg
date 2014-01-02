package org.kesler.simplereg.gui.reestr.filter;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.gui.applicator.FLListDialogController;
import org.kesler.simplereg.logic.FL;
import org.kesler.simplereg.logic.reception.filter.FLReceptionsFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FLReceptionsFilterDialog extends ReceptionsFilterDialog {

    private FL fl;
    private JLabel flLabel;

    public FLReceptionsFilterDialog(JFrame frame) {
        super(frame, "Фильтр по физ. лицу");
        setSize(250,100);
    }

    public FLReceptionsFilterDialog(JFrame frame, FLReceptionsFilter filter) {
        super(frame, "Фильтр по физ. лицу", filter);
        setSize(250,100);
    }

    @Override
    protected void createReceptionsFilter() {

//        receptionsFilter = new FLReceptionsFilter(fl);
    }

    @Override
    protected JPanel createDataPanel() {
        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        flLabel = new JLabel();
        flLabel.setBorder(BorderFactory.createEtchedBorder());

        JButton selectFLButton = new JButton("Выбрать");
        selectFLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fl = FLListDialogController.getInstance().openSelectDialog(currentDialog);
                if (fl!=null) flLabel.setText(fl.getFIO());
            }
        });

        dataPanel.add(new JLabel("Физ лицо:"));
        dataPanel.add(flLabel);
        dataPanel.add(selectFLButton);

        return dataPanel;
    }

    @Override
    protected void loadGUIDataFromReceptionsFilter() {

        if (receptionsFilter != null) {
            FLReceptionsFilter flReceptionsFilter = (FLReceptionsFilter) receptionsFilter;
            fl = flReceptionsFilter.getFl();
            flLabel.setText(fl.getFIO());
        } else {
            flLabel.setText("Не определен");
        }


    }


    @Override
    protected boolean readReceptionsFilterFromGUIData() {

        if (fl == null) {
            JOptionPane.showMessageDialog(currentDialog, "Физ лицо не выбрано", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        receptionsFilter = new FLReceptionsFilter(fl);

        return true;

    }

}