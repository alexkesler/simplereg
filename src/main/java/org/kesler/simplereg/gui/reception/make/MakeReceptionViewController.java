package org.kesler.simplereg.gui.reception.make;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import javax.swing.*;

import org.kesler.simplereg.export.reception.RosReestrPoiReceptionPrinter;
import org.kesler.simplereg.gui.reception.select.SelectReceptionDialogController;
import org.kesler.simplereg.gui.util.ProcessDialog;
import org.kesler.simplereg.logic.*;
import org.kesler.simplereg.logic.applicator.ApplicatorFL;
import org.kesler.simplereg.logic.applicator.ApplicatorUL;
import org.kesler.simplereg.logic.reception.ReceptionStatusesModel;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.gui.services.ServicesDialogController;
import org.kesler.simplereg.gui.main.CurrentOperator;
import org.kesler.simplereg.gui.applicator.ApplicatorFLDialog;
import org.kesler.simplereg.gui.applicator.ApplicatorULDialog;
import org.kesler.simplereg.gui.realty.RealtyObjectListDialogController;
import org.kesler.simplereg.export.reception.ReceptionPrinter;

import org.apache.log4j.Logger;

import org.kesler.simplereg.gui.util.InfoDialog;
import org.kesler.simplereg.logic.reception.ReceptionsModelStateListener;
import org.kesler.simplereg.logic.service.ServicesModel;
import org.kesler.simplereg.util.CounterUtil;
import org.kesler.simplereg.util.OptionsUtil;

public class MakeReceptionViewController {
    private final Logger log;

    private static MakeReceptionViewController instance;
    private MakeReceptionView view;
    private MakeReceptionViewState viewState;

    JFrame parentFrame;
    JDialog parentDialog;
    boolean isNew = true;

    private Reception reception;


    private MakeReceptionViewController() {
        log = Logger.getLogger(this.getClass().getSimpleName());
        // initReception();
        // view = new MakeReceptionView(this);
        viewState = new NoneMakeReceptionViewState(this, view);
    }

    public static synchronized MakeReceptionViewController getInstance() {
        if (instance == null) {
            instance = new MakeReceptionViewController();
        }
        return instance;
    }

    public void openView(JFrame parentFrame) {
        log.info("Open view for new Reception");
        this.parentFrame = parentFrame;
        initReception();
        isNew = true;

        view = new MakeReceptionView(this, parentFrame);
        // Переключаем в начальное состояние
        viewState = new ServiceMakeReceptionViewState(this, view);
        view.showView();
        view = null;

    }

    public void openView(JDialog parentDialog) {
        this.parentDialog = parentDialog;
        initReception();
        isNew = true;

        view = new MakeReceptionView(this, parentDialog);
        // Переключаем в начальное состояние
        viewState = new ServiceMakeReceptionViewState(this, view);
        view.showView();
        view = null;

    }

    public void openView(JFrame parentFrame, Reception reception) {
        log.info("Open view for reception " + reception.getReceptionCode());
        this.parentFrame = parentFrame;
        this.reception = reception;
        isNew = false;

        // Создаем новый вид
        view = new MakeReceptionView(this, parentFrame);
        // Переключаем в начальное состояние
        viewState = new ServiceMakeReceptionViewState(this, view);

        view.showView();
        view = null;
    }

    public void openView(JDialog parentDialog, Reception reception) {
        log.info("Open view for reception " + reception.getReceptionCode());
        this.parentDialog = parentDialog;
        this.reception = reception;
        isNew = false;

        // Создаем новый вид
        view = new MakeReceptionView(this, parentFrame);
        // Переключаем в начальное состояние
        viewState = new ServiceMakeReceptionViewState(this, view);

        view.showView();
        view = null;
    }

    public void openView(JFrame parentFrame, Reception reception, boolean fromPVD) {
        log.info("Open view for reception from PVD" + reception.getReceptionCode());
        this.parentFrame = parentFrame;
        this.reception = reception;
        if (fromPVD) {
            isNew = true;
            initReceptionFromPVD();
        }

        // Создаем новый вид
        view = new MakeReceptionView(this, parentFrame);
        // Переключаем в начальное состояние
        viewState = new ServiceMakeReceptionViewState(this, view);

        view.showView();
        view = null;
    }


    private void initReception() {
        log.info("Init reception");
        // Создаем экземпляр приема заявтеля
        reception = new Reception();

        //Фиксируем время приема
        reception.setOpenDate(new Date());

        //Получаем текущего оператора
        Operator operator = CurrentOperator.getInstance().getOperator();
        reception.setOperator(operator);

        // Присваиваем номер филиала
        reception.setFilialCode(OptionsUtil.getOption("reg.filial"));

        // Генерим уникальный номер
        int currentCount = CounterUtil.getNextCount();
        reception.setReceptionCodeNum(currentCount);

        // Генерируем код дела
        reception.generateReceptionCode();

        // Устанавливаем начальные значения
        reception.setByRecord(false);
        reception.setResultInMFC(false);

    }

    private void initReceptionFromPVD() {
        log.info("Init reception from PVD");

        //Получаем текущего оператора
        Operator operator = CurrentOperator.getInstance().getOperator();
        reception.setOperator(operator);

        // Присваиваем номер филиала
        reception.setFilialCode(OptionsUtil.getOption("reg.filial"));

        // Генерим уникальный номер
        int currentCount = CounterUtil.getNextCount();
        reception.setReceptionCodeNum(currentCount);

        // Генерируем код дела
        reception.generateReceptionCode();

        // Устанавливаем начальные значения
        reception.setByRecord(false);
        reception.setResultInMFC(false);

    }


    void back() {
        viewState.back();
    }

    void next() {
        viewState.next();
    }

    void ready() {
        viewState.ready();
    }

    void cancel() {
        reception = null;
        viewState.cancel();
    }

    void setState(MakeReceptionViewState viewState) {
        this.viewState = viewState;
    }

    Reception getReception() {
        return reception;
    }

    List<Applicator> getApplicators() {
        return reception.getApplicators();
    }

    Service getService() {
        return reception.getService();
    }

    RealtyObject getRealtyObject() {
        return reception.getRealtyObject();
    }


    // Методы для установки свойств Reception из ServicesPanel
    void setReceptionCode(String receptionCode) {
        reception.setReceptionCode(receptionCode);
    }

    void setReceptionOpenDate(Date openDate) {reception.setOpenDate(openDate);}

    void regenerateReceptionCode() {
        reception.generateReceptionCode();
        viewState.updatePanelData();
    }

    void selectService() {
        Service service = ServicesDialogController.getInstance().openSelectLeafDialog(view);
        reception.setService(service);
        reception.generateReceptionCode(); // заново генерируем код дела - уже с кодом услуги
        viewState.updatePanelData();
        service.addPvdtypePurpose(reception.getPvdtypeId(),reception.getPvdPurpose());

        // ОБновляем службу в отдельном потоке
        ServiceUpdater serviceUpdater = new ServiceUpdater(service);
        serviceUpdater.execute();
    }

    void setReceptionByRecord(boolean byRecord) {
        reception.setByRecord(byRecord);
    }

    void selectParentReception() {
        Reception parentReception = SelectReceptionDialogController.getInstance().showDialog(view);
        if (parentReception != null) {
            reception.setParentReception(parentReception);
            // Устанавливаем поля как у основного дела
            if (reception.getApplicators().size() == 0)
                copyApplicatorsFromReception(parentReception);
            if (reception.getRealtyObject() == null)
                reception.setRealtyObject(parentReception.getRealtyObject());
            if (reception.getToIssueDate() == null)
                reception.setToIssueDate(parentReception.getToIssueDate());
            if (reception.isResultInMFC() == null)
                reception.setResultInMFC(parentReception.isResultInMFC());

        }
        viewState.updatePanelData();
    }

    void resetParentReception() {
        reception.setParentReception(null);
        viewState.updatePanelData();
    }

    ////// Блок добавления, редактирования, удаления заявителей

    void addApplicatorFL() {
        ApplicatorFLDialog flDialog = new ApplicatorFLDialog(view);
        // Модальный диалог - ожидание закрытия
        flDialog.setVisible(true);

        if (flDialog.getResult() == ApplicatorFLDialog.OK) {
            List<Applicator> applicators = reception.getApplicators();
            Applicator applicator = flDialog.getApplicatorFL();
            applicator.setReception(reception);
            applicators.add(applicator);
            view.getApplicatorsPanel().applicatorAdded(applicators.size() - 1);
        }

    }

    void addApplicatorUL() {
        ApplicatorULDialog ulDialog = new ApplicatorULDialog(view);

        ulDialog.setVisible(true);
        if (ulDialog.getResult() == ApplicatorULDialog.OK) {
            List<Applicator> applicators = reception.getApplicators();
            Applicator applicator = ulDialog.getApplicatorUL();
            applicator.setReception(reception);
            applicators.add(applicator);
            view.getApplicatorsPanel().applicatorAdded(applicators.size() - 1);
        }

    }

    void editApplicator(int index) {
        if (index == -1) {
            JOptionPane.showMessageDialog(view, "Заявитель не выбран.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Applicator> applicators = reception.getApplicators();
        Applicator currentApplicator = applicators.get(index);
        // Проверяем тип заявителя и вызываем соответствующий диалогс
        if (currentApplicator instanceof ApplicatorFL) {
            ApplicatorFL currentApplicatorFL = (ApplicatorFL) currentApplicator;
            ApplicatorFLDialog flDialog = new ApplicatorFLDialog(view, currentApplicatorFL);
            flDialog.setVisible(true);
            if (flDialog.getResult() == ApplicatorFLDialog.OK) view.getApplicatorsPanel().applicatorUpdated(index);
        } else if (currentApplicator instanceof ApplicatorUL) {
            ApplicatorUL currentApplicatorUL = (ApplicatorUL) currentApplicator;
            ApplicatorULDialog ulDialog = new ApplicatorULDialog(view, currentApplicatorUL);
            ulDialog.setVisible(true);
            if (ulDialog.getResult() == ApplicatorULDialog.OK) view.getApplicatorsPanel().applicatorUpdated(index);
        }

    }

    void removeApplicator(int index) {
        if (index == -1) {
            JOptionPane.showMessageDialog(view, "Заявитель не выбран.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Applicator> applicators = reception.getApplicators();

        applicators.remove(index);
        view.getApplicatorsPanel().applicatorRemoved(index);

    }

    void readLastReceptions() {
        ReceptionsModel lastReceptionsModel = new ReceptionsModel();
        lastReceptionsModel.readLastReceptions();
        view.getApplicatorsPanel().setLastReceptions(lastReceptionsModel.getLastReceptions());
    }

    void copyApplicatorsFromReception(Reception receptionToCopy) {
        List<Applicator> applicatorsToCopy = receptionToCopy.getApplicators();
        List<Applicator> copiedApplicators = new ArrayList<Applicator>();
        for (Applicator applicatorToCopy : applicatorsToCopy) {
            Applicator copiedApplicator = applicatorToCopy.copyThis();
            copiedApplicator.setReception(reception);
            copiedApplicators.add(copiedApplicator);
        }
        reception.setApplicators(copiedApplicators);
        viewState.updatePanelData();
    }

    /// блок ввода дополнительных данных по приему

    void selectRealtyObject() {

        RealtyObject realtyObject = RealtyObjectListDialogController.getInstance().showSelectDialog(view);
        reception.setRealtyObject(realtyObject);
        viewState.updatePanelData();
    }

    void setToIssueDate(Date toIssueDate) {
        reception.setToIssueDate(toIssueDate);
    }

    void setRosreestrCode(String rosreestrCode) {
        reception.setRosreestrCode(rosreestrCode);
    }

    void setPagesNum(Integer pagesNum) {
        reception.setPagesNum(pagesNum);
    }

    void setResultInMFC(boolean resultInMFC) {
        reception.setResultInMFC(resultInMFC);
    }

    void printRequest() {
        ReceptionPrinter printer = new RosReestrPoiReceptionPrinter(reception);
        ProcessDialog processDialog = new ProcessDialog(view);
        ReceptionPrinterWorker receptionPrinterWorker = new ReceptionPrinterWorker(printer,processDialog);

        processDialog.showProcess("Заполняю запрос");
        receptionPrinterWorker.execute();

    }

    boolean saveReception() {

        ReceptionSaver receptionSaver = new ReceptionSaver(reception,isNew);
        receptionSaver.execute();

        return true;
    }


    // Классы для сохранения сущностей в отдельнопо потоке
    class ServiceUpdater extends SwingWorker<Void, Void> {
        private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
        private Service service;
        public ServiceUpdater(Service service) {
            this.service = service;
        }

        @Override
        protected Void doInBackground() throws Exception {
            ServicesModel.getInstance().updateService(service);
            return null;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (InterruptedException e) {
                log.error("Interrupted",e);
            } catch (ExecutionException e) {
                log.error("Error updating service",e);
                JOptionPane.showMessageDialog(view,"Ошибка при обновлении услуги: "+e.getMessage(),"Ошибка",JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    class ReceptionSaver extends SwingWorker<Void,String> implements ReceptionsModelStateListener{
        private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
        private Reception reception;
        private boolean isNew;
        private ReceptionsModel receptionsModel;
        private ProcessDialog processDialog;

        public ReceptionSaver(Reception reception, boolean isNew) {
            this.reception = reception;
            this.isNew = isNew;
            receptionsModel = ReceptionsModel.getInstance();
//            receptionsModel.addReceptionsModelStateListener(this);
            if (parentFrame != null) {
                processDialog = new ProcessDialog(parentFrame);
            } else if (parentDialog != null) {
                processDialog = new ProcessDialog(parentDialog);
            }
        }

        @Override
        protected Void doInBackground() throws Exception {
            if (isNew) {
                log.info("Adding reception...");
                publish("Сохраняю в базу данных...");

                // Назначаем для приема начальный статус
                Date openDate = reception.getOpenDate();
                reception.setStatus(ReceptionStatusesModel.getInstance().getInitReceptionStatus(), openDate);

                receptionsModel.addReception(reception);
            } else {
                log.info("Updating reception...");
                publish("Сохраняю в базу данных...");
                receptionsModel.updateReception(reception);
            }
            return null;
        }


        public void receptionsModelStateChanged(ServiceState state) {
            switch (state) {
                case CONNECTING:
                    publish("Соединяюсь..");
                    break;
                case WRITING:
                    publish("Сохраняю в базу данных..");
                    break;
                case READY:
                    publish("Готово");

            }
        }

        @Override
        protected void process(List<String> chunks) {
            processDialog.showProcess(chunks.get(chunks.size()-1));
        }

        @Override
        protected void done() {
            try {
                processDialog.hideProcess();
//                receptionsModel.finish();
//                receptionsModel.removeReceptionsModelStateListener(this);

                get();
                if (parentFrame != null) {
                    new InfoDialog(parentFrame, "Сохранено", 500, InfoDialog.GREEN).showInfo();
                } else if (parentDialog != null) {
                    new InfoDialog(parentDialog, "Сохранено", 500, InfoDialog.GREEN).showInfo();
                }

            } catch (InterruptedException e) {
                log.error("Interrupted",e);
            } catch (ExecutionException e) {
                log.error("Error saving reception",e);
                JOptionPane.showMessageDialog(view,"Ошибка при сохранении приема: "+e.getMessage(),"Ошибка",JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    class ReceptionPrinterWorker extends SwingWorker<Void,Void> {
        private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
        private ReceptionPrinter receptionPrinter;
        private ProcessDialog processDialog;

        ReceptionPrinterWorker(ReceptionPrinter receptionPrinter, ProcessDialog processDialog) {
            this.receptionPrinter = receptionPrinter;
            this.processDialog = processDialog;
        }


        @Override
        protected Void doInBackground() throws Exception {
            receptionPrinter.printReception();
            return null;
        }

        @Override
        protected void done() {
            super.done();
            try {
                log.info("Request filled");
                processDialog.hideProcess();
                get();
            } catch (ExecutionException ex){
                log.error("Request filling error: " + ex.getMessage(), ex);
                processDialog.hideProcess();
                JOptionPane.showMessageDialog(view,"Ошибка: "+ex.getMessage(),"Ошибка",JOptionPane.ERROR_MESSAGE);
            } catch (InterruptedException ex) {
                log.error("Request filling interrupted: " + ex.getMessage(), ex);
                processDialog.hideProcess();
                JOptionPane.showMessageDialog(view,"Ошибка: "+ex.getMessage(),"Ошибка",JOptionPane.ERROR_MESSAGE);
            }

        }
    }

}