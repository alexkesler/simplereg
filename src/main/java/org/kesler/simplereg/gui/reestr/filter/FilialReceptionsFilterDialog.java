package org.kesler.simplereg.gui.reestr.filter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.Service;

public class FilialReceptionsFilterDialog extends ReceptionsFilterDialog {

	private String filialCode;
	private JTextField filialCodeTextField;

	public FilialReceptionsFilterDialog(JFrame frame) {
		super(frame, "Фильтр по коду филиала");
	}

	public FilialReceptionsFilterDialog(JFrame frame, FilialReceptionsFilter filter) {
		super(frame, "Фильтр по коду филиала", filter);
	}

	@Override
	protected void createReceptionsFilter() {
	
		filialCode = "01";

		receptionsFilter = new FilialReceptionsFilter(filialCode);
	}

	@Override
	protected JPanel createDataPanel() {
		JPanel dataPanel = new JPanel(new MigLayout("fill"));

		filialCodeTextField = new JTextField(5);

		dataPanel.add(new JLabel("Код филиала"));
		dataPanel.add(filialCodeTextField);
			
		return dataPanel;
	}

	@Override 
	protected void loadGUIDataFromReceptionsFilter() {
		
		FilialReceptionsFilter filialReceptionsFilter = (FilialReceptionsFilter) receptionsFilter;
		filialCode = filialReceptionsFilter.getFilialCode();
		filialCodeTextField.setText(filialCode);

	}


	@Override
	protected boolean readReceptionsFilterFromGUIData() {

		filialCode = filialCodeTextField.getText();
		FilialReceptionsFilter filialReceptionsFilter = (FilialReceptionsFilter) receptionsFilter;
		filialReceptionsFilter.setFilialCode(filialCode);

		return true;

	}

}