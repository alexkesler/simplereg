package org.kesler.simplereg.gui.reception;

import java.util.Date;
import javax.swing.JOptionPane;

import org.kesler.simplereg.logic.RealtyObject;

class DataMakeReceptionViewState extends MakeReceptionViewState {

	DataMakeReceptionViewState(MakeReceptionViewController controller, MakeReceptionView view) {
		super(controller, view);
		init();
	}

	@Override
	void init() {
		
		updateTabbedPaneState();
		updateCommonButtons();
		updatePanelData();

	}

	@Override
	void back() {
		// Переходим в состояние ввода данных о заявителях
		controller.setState(new ApplicatorsMakeReceptionViewState(controller, view));
	} 

	@Override
	void next() {

		// Проверяем и сохраняем дату выдачи
		Date toIssueDate = controller.getReception().getToIssueDate();
		if (toIssueDate == null) {
			JOptionPane.showMessageDialog(view,
    									"Не определена дата выдачи",
    									"Ошибка",
    									JOptionPane.ERROR_MESSAGE);	
    		return ;									
		} 

		// Проверяем и сохраняем объект недвижимости
		if (controller.getRealtyObject() != null) {
			// controller.storeRealtyObject();
		} else {
			JOptionPane.showMessageDialog(view,
    									"Не определен объект недвижимости",
    									"Ошибка",
    									JOptionPane.ERROR_MESSAGE);	
    		return ;									
			
		}

		// Проверяем и сохраняем код Росреестра
		String rosreestrCode = controller.getReception().getRosreestrCode();
		if (rosreestrCode==null || rosreestrCode.isEmpty()) {
			JOptionPane.showMessageDialog(view,
    									"Не определен код дела Росреестра",
    									"Ошибка",
    									JOptionPane.ERROR_MESSAGE);	
			return ;
			
		} 


		// Переходим в состояние печати запроса
		controller.setState(new PrintMakeReceptionViewState(controller, view));
	}

	@Override
	void ready() {
		// Ничего не делаем - кнопка невидима
	}

	private void updateTabbedPaneState() {

		view.getTabbedPane().setEnabledAt(view.SERVICE_STATE,false);
		view.getTabbedPane().setEnabledAt(view.APPLICATORS_STATE,false);
		view.getTabbedPane().setEnabledAt(view.DATA_STATE,true);
		view.getTabbedPane().setEnabledAt(view.PRINT_STATE,false);

		view.getTabbedPane().setSelectedIndex(view.DATA_STATE);

	}

	private void updateCommonButtons() {

		view.getBackButton().setEnabled(true);
		view.getNextButton().setEnabled(true);
		view.getReadyButton().setEnabled(false);

	}


	@Override
	void updatePanelData() {
		// обновляем дату на выдачу
		Date toIssueDate = controller.getReception().getToIssueDate();
		view.getDataPanel().setToIssueDate(toIssueDate);
		
		// обновляем наименование объекта недвижимости
		RealtyObject realtyObject = controller.getRealtyObject();
		String realtyObjectName = "";
		if (realtyObject != null) {
			realtyObjectName = realtyObject.toString();
		} else {
			realtyObjectName = "Не определено";
		}

		view.getDataPanel().setRealtyObjectName(realtyObjectName);

		// Обновляем код дела Росреестра
		String rosreestrCode = controller.getReception().getRosreestrCode();
		view.getDataPanel().setRosreestrCode(rosreestrCode);

		// Обновляем признак получения результата в МФЦ
		Boolean resultInMFC = controller.getReception().isResultInMFC();
		if (resultInMFC == null) resultInMFC = false;
		view.getDataPanel().setResultInMFC(resultInMFC);

	}


}