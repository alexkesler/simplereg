package org.kesler.simplereg.gui.reestr.filter;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

import java.awt.GridLayout;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.Operator;
import org.kesler.simplereg.logic.operator.OperatorsService;
import org.kesler.simplereg.logic.reception.filter.OperatorReceptionsFilter;

public class OperatorReceptionsFilterDialog extends ReceptionsFilterDialog {

	private List<Operator> filterOperators;

	private List<Operator> allOperators;
	List<JCheckBox> checkBoxes;

	public OperatorReceptionsFilterDialog(JFrame frame) {
		super(frame, "Фильтр по операторам");

	}

	public OperatorReceptionsFilterDialog(JFrame frame, OperatorReceptionsFilter filter) {
		super(frame, "Фильтр по операторам", filter);
	}

	@Override
	protected void createReceptionsFilter() {
	
		filterOperators = new ArrayList<Operator>();

		receptionsFilter = new OperatorReceptionsFilter(filterOperators);
	}

	@Override
	protected JPanel createDataPanel() {
		JPanel dataPanel = new JPanel(new MigLayout("fill"));

		JPanel operatorsPanel = new JPanel(new GridLayout(0,1));
		checkBoxes = new ArrayList<JCheckBox>();

		allOperators = OperatorsService.getInstance().getActiveOperators();

		for (Operator operator: allOperators) {
			JCheckBox checkBox = new JCheckBox(operator.getShortFIO());
			checkBoxes.add(checkBox);
			operatorsPanel.add(checkBox);
		}

		JScrollPane operatorsPanelScrollPane = new JScrollPane(operatorsPanel);

		dataPanel.add(operatorsPanelScrollPane, "grow");
			
		return dataPanel;
	}

	@Override 
	protected void loadGUIDataFromReceptionsFilter() {

		OperatorReceptionsFilter operatorReceptionsFilter = (OperatorReceptionsFilter) receptionsFilter;
		this.filterOperators = operatorReceptionsFilter.getOperators();


		for (int i = 0; i < allOperators.size(); i++) {
			Operator operator = allOperators.get(i);
			JCheckBox checkBox = checkBoxes.get(i);
			if(filterOperators.contains(operator)) checkBox.setSelected(true);
			else checkBox.setSelected(false);
		}

	}


	@Override
	protected boolean readReceptionsFilterFromGUIData() {

		filterOperators.clear();

		for (int i = 0; i < checkBoxes.size(); i++) {
			JCheckBox checkBox = checkBoxes.get(i);
			Operator operator = allOperators.get(i);
			if (checkBox.isSelected()) {
				filterOperators.add(operator);
			}
		}

		if (filterOperators.size() == 0) {
			JOptionPane.showMessageDialog(this, "Не выбрано ни одного оператора", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;

	}

}