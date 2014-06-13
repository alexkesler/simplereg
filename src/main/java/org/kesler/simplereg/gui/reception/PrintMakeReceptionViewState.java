package org.kesler.simplereg.gui.reception;


class PrintMakeReceptionViewState extends MakeReceptionViewState {

    PrintMakeReceptionViewState(MakeReceptionViewController controller, MakeReceptionView view) {
        super(controller, view);
        init();
    }

    @Override
    void init() {
        view.getTabbedPane().setEnabledAt(view.SERVICE_STATE, false);
        view.getTabbedPane().setEnabledAt(view.APPLICATORS_STATE, false);
        view.getTabbedPane().setEnabledAt(view.DATA_STATE, false);
        view.getTabbedPane().setEnabledAt(view.PRINT_STATE, true);

        view.getTabbedPane().setSelectedIndex(view.PRINT_STATE);

        view.getBackButton().setEnabled(true);
        view.getNextButton().setEnabled(false);
        view.getReadyButton().setEnabled(true);
    }

    @Override
    void back() {
        controller.setState(new DataMakeReceptionViewState(controller, view));
    }

    @Override
    void next() {
        // кнопка неактивна
    }

    @Override
    void ready() {
        // Сохраняем прием заявителя
        if (controller.saveReception())
            controller.setState(new NoneMakeReceptionViewState(controller, view));
    }

    @Override
    void updatePanelData() {

    }


}