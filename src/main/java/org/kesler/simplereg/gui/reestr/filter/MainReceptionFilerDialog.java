package org.kesler.simplereg.gui.reestr.filter;

import net.miginfocom.swing.MigLayout;
import org.kesler.simplereg.logic.reception.filter.MainReceptionsFilter;

import javax.swing.*;

/**
 * Диалог редактирования фильтра основного дела
 */
public class MainReceptionFilerDialog extends ReceptionsFilterDialog{
    private Boolean main;
    private JCheckBox mainCheckBox;

    public MainReceptionFilerDialog(JFrame frame) {
        super(frame, "Фильтр основных дел");
    }

    public MainReceptionFilerDialog(JFrame frame, MainReceptionsFilter filter) {
        super(frame, "Фильтр основных дел", filter);
    }

    @Override
    protected void createReceptionsFilter() {

        main = true;

        receptionsFilter = new MainReceptionsFilter(main);
    }

    @Override
    protected JPanel createDataPanel() {
        JPanel dataPanel = new JPanel(new MigLayout("fill"));

        mainCheckBox = new JCheckBox("Основные дела");

        dataPanel.add(mainCheckBox);

        return dataPanel;
    }

    @Override
    protected void loadGUIDataFromReceptionsFilter() {

        MainReceptionsFilter mainReceptionsFilter = (MainReceptionsFilter) receptionsFilter;
        main = mainReceptionsFilter.getMain();
        mainCheckBox.setSelected(main);

    }


    @Override
    protected boolean readReceptionsFilterFromGUIData() {

        main = mainCheckBox.isSelected();
        MainReceptionsFilter mainReceptionsFilter = (MainReceptionsFilter) receptionsFilter;
        mainReceptionsFilter.setMain(main);

        return true;

    }
}
