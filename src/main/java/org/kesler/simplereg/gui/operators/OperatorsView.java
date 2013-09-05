package org.kesler.simplereg.gui.operators;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.Operator;

/**
*<p>Используеся для отображения и редактирования списка операторов</p>
*
*
*/
public class OperatorsView extends JFrame {

	private OperatorsViewController controller;
	private JTable operatorsTable;
	private OperatorsTableModel tableModel;
	private OperatorPanel operatorPanel;

	public OperatorsView(OperatorsViewController controller) {
		super("Операторы");
		this.controller = controller;
		tableModel = new OperatorsTableModel();
		operatorPanel = new OperatorPanel();
		createGUI();
	}

	private void createGUI() {
		this.setSize(500,500);

		JPanel mainPanel = new JPanel(new BorderLayout());

		GridBagConstraints c = new GridBagConstraints();
		JPanel tablePanel = new JPanel(new BorderLayout());

		// определяем ширину колонок
		operatorsTable = new JTable(tableModel);
		TableColumn column = null;
		for (int i=0; i<6; i++) {
			column = operatorsTable.getColumnModel().getColumn(i);
			if (i == 1) {
				column.setPreferredWidth(200);
			} else if (i == 5) {
				column.setMinWidth(0);
				column.setMaxWidth(0);
				column.setPreferredWidth(0);
			}
			else {
				column.setMaxWidth(50);
			}
		}

		// обозначаем удаленные/редактированные ячейки
		column = operatorsTable.getColumnModel().getColumn(1);
		column.setCellRenderer(new DefaultTableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table,
															Object value,
															boolean isSelected,
															boolean hasFocus,
															int row,
															int column) 
				{
				
				int state = ((Integer)table.getValueAt(row,5)).intValue();
				switch (state) {
					case Operator.EDITED_STATE: 
					value = "<html><i>" + value.toString() + "</i></html>";	
					break;
					case Operator.DELETED_STATE: 
					value = "<html><s>" + value.toString() + "</s></html>";	
					break;
					case Operator.NEW_STATE: 
					value = "<html><i><b>" + value.toString() + "</b></i></html>";	
					break;
				}	

				

				Component component = super.getTableCellRendererComponent(table,
																		value,
																		isSelected,
																		hasFocus,
																		row,
																		column);
				



				return component;
			}
		});

		operatorsTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ev) {
				if (ev.getClickCount() == 2) {
					int selected = operatorsTable.getSelectedRow();
					selected = operatorsTable.convertRowIndexToModel(selected);
					controller.editOperator(selected);
				}
			}
		});


		JScrollPane tableScrollPane = new JScrollPane(operatorsTable);
		//tableScrollPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		// Панель кнопок управления списком операторов
		JPanel tableButtonPanel = new JPanel();

		JButton addButton = new JButton("Добавить");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.newOperator();
			}
		});

		JButton editButton = new JButton("Редактировать");	
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				int selected = operatorsTable.getSelectedRow();
				if (selected == -1) {
					JOptionPane.showMessageDialog(null,"Ошибка","Ничего не выбрано",JOptionPane.ERROR_MESSAGE);
				} else {
					selected = operatorsTable.convertRowIndexToModel(selected);
					controller.editOperator(selected);
				}

			}
		});	

		JButton deleteButton = new JButton("Удалить");																									
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				int selected = operatorsTable.getSelectedRow();
				if (selected == -1) {
					JOptionPane.showMessageDialog(null, "Ошибка", "Ничего не выбрано", JOptionPane.ERROR_MESSAGE);
				} else {
					selected = operatorsTable.convertRowIndexToModel(selected);
					controller.deleteOperator(selected);
				}
			}
		});

		tableButtonPanel.add(addButton);
		tableButtonPanel.add(editButton);
		tableButtonPanel.add(deleteButton);

		// добавляем на панель таблицы
		tablePanel.add(BorderLayout.CENTER, tableScrollPane);
		tablePanel.add(BorderLayout.SOUTH, tableButtonPanel);
		tablePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		


		JPanel buttonPanel = new JPanel();

		JButton saveButton = new JButton("Сохранить");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.saveOperators();
			}
		});

		JButton closeButton = new JButton("Закрыть");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.closeView();
			}
		});

		buttonPanel.add(saveButton);
		buttonPanel.add(closeButton);


		mainPanel.add(BorderLayout.CENTER, tablePanel);
		mainPanel.add(BorderLayout.SOUTH, buttonPanel);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		this.add(mainPanel,BorderLayout.CENTER);
		this.setLocationRelativeTo(null);


	}

	/**
	* Возвращает модель таблицы для установки свойств и вызова методов
	*/
	public OperatorsTableModel getTableModel() {
		return tableModel;
	}

	/**
	* Возвращает панель редактирования свойств оператора для вызова её методов
	*/
	public OperatorPanel getOperatorPanel() {
		return operatorPanel;
	}

	/**
	* вызывает панель резактирования свойст оператора, заполняет её сведениями
	* @param operator оператор, сведения о котором будут редактироваться при открытии диалога
	*/
	public OperatorPanel getOperatorPanel(Operator operator) {
		operatorPanel.setOperator(operator);
		return operatorPanel;
	}

	/**
	* Панель для диалога добавления оператора
	*/
	public class OperatorPanel extends JPanel {
		private Operator operator;

		private JTextField fioTextField;

		private JTextField firstNameTextField;
		private JTextField parentNameTextField;
		private JTextField surNameTextField;

		private JTextField fioShortTextField;
		private JTextField passwordTextField;
		private JCheckBox controlerCheckBox;
		private JCheckBox adminCheckBox;
		private JCheckBox enabledCheckBox;


		public OperatorPanel () {
			super(new MigLayout("wrap 2", "[right][left]", ""));
			operator = new Operator();

			this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"Добавить/изменить"));

			JLabel fioLabel = new JLabel("ФИО");
			fioTextField = new JTextField(20);

			JLabel surNameLabel = new JLabel("Фамилия");
			surNameTextField = new JTextField(20);

			JLabel firstNameLabel = new JLabel("Имя");
			firstNameTextField = new JTextField(20);

			JLabel parentNameLabel = new JLabel("Отчество");
			parentNameTextField = new JTextField(20);


			JLabel passwordLabel = new JLabel("Пароль");
			passwordTextField = new JTextField(20);

			controlerCheckBox = new JCheckBox("Контролер",false);

			adminCheckBox = new JCheckBox("Админ",false);

			enabledCheckBox = new JCheckBox("Действ",true);

			// Добавляем элементы на панель 

			this.add(fioLabel);
			this.add(fioTextField);

			this.add(surNameLabel);
			this.add(surNameTextField);

			this.add(firstNameLabel);
			this.add(firstNameTextField);

			this.add(parentNameLabel);
			this.add(parentNameTextField);

			this.add(passwordLabel);
			this.add(passwordTextField);

			this.add(controlerCheckBox,"skip 1");

			this.add(adminCheckBox,"skip 1");

			this.add(enabledCheckBox,"skip 1");

		}

		/**
		*<p> Устанавливает приватное свойство - оператора</p>
		*<p> Заполняет поля сведениями об операторе</p>
		*/
		public void setOperator(Operator operator) {
			this.operator = operator;

			fioTextField.setText(operator.getFIO());
			surNameTextField.setText(operator.getSurName());
			firstNameTextField.setText(operator.getFirstName());
			parentNameTextField.setText(operator.getParentName());
			passwordTextField.setText(operator.getPassword());
			
			if (operator.getIsControler()==null) {
				controlerCheckBox.setSelected(false);
			} else {
				controlerCheckBox.setSelected(operator.getIsControler());
			}

			if (operator.getIsAdmin()==null) {
				adminCheckBox.setSelected(false);
			} else {
				adminCheckBox.setSelected(operator.getIsAdmin());
			}

			if (operator.getEnabled()==null) {
				enabledCheckBox.setSelected(true);
			} else {
				enabledCheckBox.setSelected(operator.getEnabled());
			}

		}


		/**
		* Заполняет поля оператора значениями управляющих элементов
		*/
		public void readOperator() {
			operator.setFIO(fioTextField.getText());
			operator.setSurName(surNameTextField.getText());
			operator.setFirstName(firstNameTextField.getText());
			operator.setParentName(parentNameTextField.getText());
			operator.setPassword(passwordTextField.getText());
			operator.setIsControler(controlerCheckBox.isSelected());
			operator.setIsAdmin(adminCheckBox.isSelected());
			operator.setEnabled(enabledCheckBox.isSelected());
		}
    }
		

	// Модель данных для таблицы
	public class OperatorsTableModel extends AbstractTableModel {

		private List<Operator> operators;

		public OperatorsTableModel () {
			operators = new ArrayList<Operator>();
		}

		public void setOperators(List<Operator> operators) {
			this.operators = operators;
			fireTableDataChanged();
		}

		@Override 
		public int getRowCount() {
			return operators.size();
		}


		@Override
		public int getColumnCount() {
			return 6;
		}

		@Override
		public String getColumnName(int column) {
			String columnName = "Не опр.";
			switch (column) {
				case 0: columnName="№";
				break;
				case 1: columnName="ФИО";
				break;
				case 2: columnName="Контр";
				break;
				case 3: columnName="Админ";
				break;
				case 4: columnName="Действ";
				break;
			}
			return columnName;
		}

		@Override
		public Object getValueAt(int row, int column) {
			Operator operator = operators.get(row);
			Object value = null;
			switch (column) {
				case 0: value=operator.getId();
				break;
				case 1: value=operator.getFIO();
				break;
				case 2: value=operator.getIsControler();
				break;
				case 3: value=operator.getIsAdmin();
				break;
				case 4: value=operator.getEnabled();
				break;
				case 5: value=operator.getState();
				break;
			}
			return value;

		}

		@Override
		public Class getColumnClass(int column) {
			
			return getValueAt(0, column).getClass();

		}

		@Override
		public boolean isCellEditable(int row, int column) {
			boolean editable = true;
			if (column == 0 || column == 1 || column == 5) {
				editable = false;
			}

			return editable;
		}

		@Override
		public void setValueAt(Object aValue, int row, int column) {
			Operator operator = operators.get(row);
			switch (column) {
				case 2: 
				operator.setIsControler((Boolean)aValue);
				operator.setState(Operator.EDITED_STATE);
				break;
				case 3: 
				operator.setIsAdmin((Boolean)aValue);
				operator.setState(Operator.EDITED_STATE);
				break;
				case 4: 
				operator.setEnabled((Boolean)aValue);
				operator.setState(Operator.EDITED_STATE);
				break;
			}
			fireTableDataChanged();
		} 

	}

}