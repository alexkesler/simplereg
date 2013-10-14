package org.kesler.simplereg.gui.reestr;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.reception.Reception;
import org.kesler.simplereg.gui.reestr.column.ReestrColumn;
import org.kesler.simplereg.gui.reestr.column.ReestrColumns;
import org.kesler.simplereg.gui.reestr.filter.ReceptionsFilter;
import org.kesler.simplereg.gui.reestr.filter.ReceptionsFiltersEnum;

import org.kesler.simplereg.util.ResourcesUtil;

public class ReestrView extends JFrame {

	private final boolean DEBUG = true;
	
	private ReestrViewController controller;

	private JTable reestrTable;
	private ReestrTableModel reestrTableModel;

	private FilterListModel filterListModel;
	private int selectedFilterIndex = -1;
	private ReceptionsFilter selectedFilter = null;

	public ReestrView(ReestrViewController controller) {
		super("Реестр запросов");
		this.controller = controller;
		createGUI();
	}
	

	private void createGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel filterPanel = new JPanel(new MigLayout("fill, nogrid"));

		// делаем лист для отображения и измененения набора фильтров
		filterListModel = new FilterListModel();
		final JList filterList = new JList(filterListModel);
		filterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		filterList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent lse) {
				if(lse.getValueIsAdjusting() == false) {
					selectedFilterIndex = filterList.getSelectedIndex();
					if (DEBUG) System.out.println("selected filter item: " + selectedFilterIndex);
					if (selectedFilterIndex != -1) {
						selectedFilter = controller.getFilters().get(selectedFilterIndex);
					} else {
						selectedFilter = null;
					}

				}				
			}
		});
		JScrollPane filterListScrollPane = new JScrollPane(filterList);

		////// кнопки управления набором фильтров

		final JPopupMenu filtersPopupMenu = new JPopupMenu();


		// кнопка добавления
		final JButton addFilterButton = new JButton();
		addFilterButton.setIcon(ResourcesUtil.getIcon("add.png"));
		addFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				filtersPopupMenu.show(addFilterButton, addFilterButton.getWidth(), 0);
			}
		});
		// Пункт меню - добавление фильтра по дате открытия
		JMenuItem openDateFilterMenuItem = new JMenuItem("По дате открытия");
		openDateFilterMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.addFilter(ReceptionsFiltersEnum.OPEN_DATE);
			}
		});

		// Пункт меню - добавление фильтра по состоянию
		JMenuItem statusFilterMenuItem = new JMenuItem("По состоянию");
		statusFilterMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.addFilter(ReceptionsFiltersEnum.STATUS);
			}
		});

		// Пункт меню - добавление фильтра по услугам
		JMenuItem serviceFilterMenuItem = new JMenuItem("По услуге");
		serviceFilterMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.addFilter(ReceptionsFiltersEnum.SERVICE);
			}
		});


		// Пункт меню - добавление фильтра по операторам
		JMenuItem operatorFilterMenuItem = new JMenuItem("По оператору");
		operatorFilterMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.addFilter(ReceptionsFiltersEnum.OPERATOR);
			}
		});

		// собираем всплывающее меню добавления фильтра
		filtersPopupMenu.add(openDateFilterMenuItem);
		filtersPopupMenu.add(statusFilterMenuItem);
		filtersPopupMenu.add(serviceFilterMenuItem);
		filtersPopupMenu.add(operatorFilterMenuItem);

		// кнопка реадктирования
		JButton editFilterButton = new JButton();
		editFilterButton.setIcon(ResourcesUtil.getIcon("pencil.png"));
		editFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.editFilter(selectedFilterIndex);
			}
		});


		// кнопка удаления фильтра
		JButton removeFilterButton = new JButton();
		removeFilterButton.setIcon(ResourcesUtil.getIcon("delete.png"));
		removeFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.removeFilter(selectedFilterIndex);
			}
		});

		JButton resetFiltersButton = new JButton("Очистить");
		resetFiltersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.resetFilters();
			}
		});

		JButton applyFiltersButton = new JButton("Применить");
		applyFiltersButton.setIcon(ResourcesUtil.getIcon("tick.png"));
		applyFiltersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.applyFilters();
				reestrTableModel.fireTableDataChanged();
				//setReestrTableColumnsWidth();
			}
		});


		// Собираем панель фильтра
		filterPanel.add(new JLabel("Фильтры: "), "wrap");
		filterPanel.add(filterListScrollPane, "push, growx, h 80, wrap");
		filterPanel.add(addFilterButton, "split");
		filterPanel.add(editFilterButton);
		filterPanel.add(removeFilterButton);
		filterPanel.add(resetFiltersButton, "wrap");
		filterPanel.add(applyFiltersButton,"span, center");


		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill, nogrid"));

		final JButton columnsButton = new JButton();
		columnsButton.setIcon(ResourcesUtil.getIcon("bullet_wrench.png"));
		columnsButton.setToolTipText("Изменить набор отображаемых колонок");
		columnsButton.setPreferredSize(new Dimension(16,16));
		columnsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.openColumnsDialog();
				reestrTableModel.fireTableStructureChanged();
			}
		});

		//////// Основная таблица для приемов
		reestrTableModel = new ReestrTableModel();
		reestrTable = new JTable(reestrTableModel);
		//добавляем кнопку изменения столбцов
		reestrTable.getTableHeader().setLayout(new BorderLayout());
		reestrTable.getTableHeader().add(columnsButton, BorderLayout.EAST);

		JScrollPane reestrTableScrollPane = new JScrollPane(reestrTable);

		// всплывающее меню для таблицы
		JPopupMenu reestrPopupMenu = new JPopupMenu();


		JMenuItem openReceptionMenuItem = new JMenuItem("Открыть прием");
		openReceptionMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				
				controller.openReceptionDialog();
			}
		});

		JMenuItem setReceptionStatusMenuItem = new JMenuItem("Изменить состояние");

		reestrPopupMenu.add(openReceptionMenuItem);
		reestrPopupMenu.add(setReceptionStatusMenuItem);

		reestrTable.setComponentPopupMenu(reestrPopupMenu);

		dataPanel.add(reestrTableScrollPane, "push, grow");

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				setVisible(false);
			}
		});

		buttonPanel.add(okButton);

		mainPanel.add(filterPanel, BorderLayout.NORTH);
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.pack();
		this.setLocationRelativeTo(null);
	}

	class FilterListModel extends AbstractListModel {

		@Override
		public int getSize() {
			return controller.getFilters().size();
		}

		@Override
		public String getElementAt(int index) {
			return controller.getFilters().get(index).toString();
		}

		public void filterAdded(int index) {
			fireIntervalAdded(this,index,index);
		}

		public void filterUpdated(int index) {
			fireContentsChanged(this,index,index);
		}

		public void filterRemoved(int index) {
			fireIntervalRemoved(this,index,index);
		}

		public void filtersCleared(int count) {
			fireIntervalRemoved(this,0,count-1);
		}

	}

	public FilterListModel getFilterListModel() {
		return filterListModel;
	}

	// устанавливает ширину полей таблички
	private void setReestrTableColumnsWidth() {
		List<ReestrColumn> reestrColumns = ReestrColumns.getInstance().getActiveColumns();
		reestrTable.getColumnModel().getColumn(0).setPreferredWidth(20);
		for (int i = 1; i < reestrColumns.size(); i++) {
			reestrTable.getColumnModel().getColumn(i).setPreferredWidth(reestrColumns.get(i).getWidth());
		}
	}

	class ReestrTableModel extends AbstractTableModel {

		public int getRowCount() {
			return controller.getFilteredReceptions().size();
		}

		public int getColumnCount() {
			return ReestrColumns.getInstance().getActiveColumns().size() + 1;
		}

		public String getColumnName(int column) {
			List<ReestrColumn> reestrColumns = ReestrColumns.getInstance().getActiveColumns();			

			String name = "Не опр";

			if (column == 0) {
				name = "№";
			} else {
				name = reestrColumns.get(column - 1).getName();
			}

		return name;
		}

		public Object getValueAt(int row, int column) {
			Reception reception = controller.getFilteredReceptions().get(row);
			
			List<ReestrColumn> reestrColumns = ReestrColumns.getInstance().getActiveColumns();			


			Object value = null;

			if (column == 0) {
				value = row + 1;
			} else {
				value = reestrColumns.get(column - 1).getValue(reception);
			}

			return value;
		}

		public String getToolTipText(java.awt.event.MouseEvent e) {
			String tip = null;
			java.awt.Point p = e.getPoint();
			int rowIndex = reestrTable.rowAtPoint(p);
			int colIndex = reestrTable.columnAtPoint(p);
			int realColumnIndex = reestrTable.convertColumnIndexToModel(colIndex);
			tip = getValueAt(rowIndex, realColumnIndex).toString();

			return tip;
		}

	}

}	