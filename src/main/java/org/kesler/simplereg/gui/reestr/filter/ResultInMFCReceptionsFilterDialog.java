package org.kesler.simplereg.gui.reestr.filter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JCheckBox;

import net.miginfocom.swing.MigLayout;


import org.kesler.simplereg.logic.Service;

public class ResultInMFCReceptionsFilterDialog extends ReceptionsFilterDialog {

	private Boolean resultInMFC;
	private JCheckBox resultInMFCCheckBox;

	public ResultInMFCReceptionsFilterDialog(JFrame frame) {
		super(frame, "Фильтр по месту получения результата");
	}

	public ResultInMFCReceptionsFilterDialog(JFrame frame, ResultInMFCReceptionsFilter filter) {
		super(frame, "Фильтр по месту получения результата", filter);
	}

	@Override
	protected void createReceptionsFilter() {
	
		resultInMFC = new Boolean(true);

		receptionsFilter = new ResultInMFCReceptionsFilter(resultInMFC);
	}

	@Override
	protected JPanel createDataPanel() {
		JPanel dataPanel = new JPanel(new MigLayout("fill"));

		resultInMFCCheckBox = new JCheckBox("Получение результата в МФЦ");

		dataPanel.add(resultInMFCCheckBox);
			
		return dataPanel;
	}

	@Override 
	protected void loadGUIDataFromReceptionsFilter() {
		
		ResultInMFCReceptionsFilter resultInMFCReceptionsFilter = (ResultInMFCReceptionsFilter) receptionsFilter;
		resultInMFC = resultInMFCReceptionsFilter.getResultInMFC();
		resultInMFCCheckBox.setSelected(resultInMFC);

	}


	@Override
	protected boolean readReceptionsFilterFromGUIData() {

		resultInMFC = resultInMFCCheckBox.isSelected();
		ResultInMFCReceptionsFilter resultInMFCReceptionsFilter = (ResultInMFCReceptionsFilter) receptionsFilter;
		resultInMFCReceptionsFilter.setResultInMFC(resultInMFC);

		return true;

	}

}