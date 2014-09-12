package org.kesler.simplereg.gui.reestr;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.alee.extended.date.DateSelectionListener;
import com.alee.extended.date.WebDateField;
import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.reception.ReceptionStatusesModel;
import org.kesler.simplereg.gui.reestr.column.ReestrColumn;
import org.kesler.simplereg.gui.reestr.column.ReestrColumns;
import org.kesler.simplereg.logic.reception.filter.ReceptionsFilter;

import org.kesler.simplereg.util.ResourcesUtil;

import static org.kesler.simplereg.logic.reception.filter.ReceptionsFiltersEnum.*;

public class ReestrView extends JFrame {

	private final boolean DEBUG = true;
	
	private ReestrViewController controller;

	private JTable reestrTable;
	private ReestrTableModel reestrTableModel;

	private Action openReceptionAction;
	private Action changeReceptionsStatusAction;
    private Action selectMainReceptionAction;
    private Action resetMainReceptionAction;
	private Action removeReceptionsAction;

    private WebDateField fromDateField;
    private WebDateField toDateField;
	private FilterListModel filterListModel;
	private int selectedFilterIndex = -1;
	private ReceptionsFilter selectedFilter = null;

	public ReestrView(ReestrViewController controller, JFrame parentFrame) {
		super("Реестр запросов");
		this.controller = controller;
		createGUI();

		this.setLocationRelativeTo(parentFrame);
	}
	
	public void setReceptions(List<Reception> receptions) {
		reestrTableModel.setReceptions(receptions);
	}

	private void createGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new MigLayout("fill"));

        // Панель быстрого поиска
        JPanel searchPanel = new JPanel(new MigLayout("fill"));
        searchPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Быстрый поиск"));

        final JTextField receptionCodeSearchTextField = new JTextField(10);
        receptionCodeSearchTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filterString = receptionCodeSearchTextField.getText();
                controller.searchByReceptionCode(filterString);
                receptionCodeSearchTextField.requestFocus();
            }
        });

        JButton applyReceptionCodeSearchButton = new JButton();
        applyReceptionCodeSearchButton.setIcon(ResourcesUtil.getIcon("accept.png"));
        applyReceptionCodeSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filterString = receptionCodeSearchTextField.getText();
                controller.searchByReceptionCode(filterString);
                receptionCodeSearchTextField.requestFocus();
            }
        });

        JButton resetReceptionCodeSearchButton = new JButton();
        resetReceptionCodeSearchButton.setIcon(ResourcesUtil.getIcon("delete.png"));
        resetReceptionCodeSearchButton.setToolTipText("Очистить");
        resetReceptionCodeSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                receptionCodeSearchTextField.setText("");
                controller.searchByReceptionCode("");
            }
        });

        final JTextField rosreestrCodeSearchTextField = new JTextField(10);
        rosreestrCodeSearchTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filterString = rosreestrCodeSearchTextField.getText();
                controller.searchByRosreestrCode(filterString);
                rosreestrCodeSearchTextField.requestFocus();
            }
        });

        JButton applyRosreestrCodeSearchButton = new JButton();
        applyRosreestrCodeSearchButton.setIcon(ResourcesUtil.getIcon("accept.png"));
        applyRosreestrCodeSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filterString = rosreestrCodeSearchTextField.getText();
                controller.searchByRosreestrCode(filterString);
                rosreestrCodeSearchTextField.requestFocus();
            }
        });

        JButton resetRosreestrCodeSearchButton = new JButton();
        resetRosreestrCodeSearchButton.setIcon(ResourcesUtil.getIcon("delete.png"));
        resetRosreestrCodeSearchButton.setToolTipText("Очистить");
        resetRosreestrCodeSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rosreestrCodeSearchTextField.setText("");
                controller.searchByRosreestrCode("");
            }
        });


        searchPanel.add(new JLabel("Код дела"));
        searchPanel.add(receptionCodeSearchTextField);
        searchPanel.add(applyReceptionCodeSearchButton);
        searchPanel.add(resetReceptionCodeSearchButton, "wrap");
        searchPanel.add(new JLabel("Код Росреестра"));
        searchPanel.add(rosreestrCodeSearchTextField);
        searchPanel.add(applyRosreestrCodeSearchButton);
        searchPanel.add(resetRosreestrCodeSearchButton, "wrap");


		JPanel filterPanel = new JPanel(new MigLayout("fill"));
        filterPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Фильтр"));

        fromDateField = new WebDateField(controller.getFromOpenDate());
        fromDateField.addDateSelectionListener(new DateSelectionListener() {
            @Override
            public void dateSelected(Date date) {
                controller.setFromOpenDate(date);
            }
        });

        toDateField = new WebDateField(controller.getToOpenDate());
        toDateField.addDateSelectionListener(new DateSelectionListener() {
            @Override
            public void dateSelected(Date date) {
                controller.setToOpenDate(date);
            }
        });

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
        filterList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount()==2) {
                    controller.editFilter(selectedFilterIndex);
                }
            }
        });
		JScrollPane filterListScrollPane = new JScrollPane(filterList);

		////// кнопки управления набором фильтров

		final JPopupMenu filtersPopupMenu = new JPopupMenu();


		// кнопка добавления
		final JButton addFilterButton = new JButton();
		addFilterButton.setIcon(ResourcesUtil.getIcon("add.png"));
        addFilterButton.setToolTipText("Добавить");
		addFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				filtersPopupMenu.show(addFilterButton, addFilterButton.getWidth(), 0);
			}
		});
		// Пункт меню - добавление фильтра по дате открытия
		JMenuItem openDateFilterMenuItem = new JMenuItem("По дате открытия");
		openDateFilterMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.addFilter(OPEN_DATE);
			}
		});

		// Пункт меню - добавление фильтра по коду филиала;
		JMenuItem filialFilterMenuItem = new JMenuItem("По коду филиала");
		filialFilterMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.addFilter(FILIAL);
			}
		});

		// Пункт меню - добавление фильтра по предварительной записи
		JMenuItem byRecordFilterMenuItem = new JMenuItem("По предв записи");
		byRecordFilterMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.addFilter(BY_RECORD);
			}
		});

		// Пункт меню - добавление фильтра по состоянию
		JMenuItem statusFilterMenuItem = new JMenuItem("По состоянию");
		statusFilterMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.addFilter(STATUS);
			}
		});

        // Пункт меню - добавление фильтра по услугам
		JMenuItem serviceFilterMenuItem = new JMenuItem("По услуге");
		serviceFilterMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.addFilter(SERVICE);
			}
		});


		// Пункт меню - добавление фильтра по операторам
		JMenuItem operatorFilterMenuItem = new JMenuItem("По оператору");
		operatorFilterMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.addFilter(OPERATOR);
			}
		});

        // Пункт меню - добавление фильтра по операторам
        JMenuItem flMenuItem = new JMenuItem("По физ лицу");
        flMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                controller.addFilter(FL);
            }
        });


        // Пункт меню - добавление фильтра по операторам
        JMenuItem ulMenuItem = new JMenuItem("По юр лицу");
        ulMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                controller.addFilter(UL);
            }
        });

		// Пункт меню - добавление фильтра по дате на выдачу результата
		JMenuItem toIssueDateFilterMenuItem = new JMenuItem("По дате на выдачу");
		toIssueDateFilterMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.addFilter(TO_ISSUE_DATE);
			}
		});

		// Пункт меню - добавление фильтра по месту получения результата
		JMenuItem resultInMFCFilterMenuItem = new JMenuItem("По месту получения результата");
		resultInMFCFilterMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.addFilter(RESULT_IN_MFC);
			}
		});

        // Пункт меню - добавление фильтра по месту получения результата
        JMenuItem mainFilterMenuItem = new JMenuItem("Для основных/дополнительных дел");
        mainFilterMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                controller.addFilter(MAIN);
            }
        });


        // собираем всплывающее меню добавления фильтра
		filtersPopupMenu.add(openDateFilterMenuItem);
		filtersPopupMenu.add(filialFilterMenuItem);
		filtersPopupMenu.add(byRecordFilterMenuItem);
		filtersPopupMenu.add(statusFilterMenuItem);
 		filtersPopupMenu.add(serviceFilterMenuItem);
		filtersPopupMenu.add(operatorFilterMenuItem);
        filtersPopupMenu.add(flMenuItem);
        filtersPopupMenu.add(ulMenuItem);
		filtersPopupMenu.add(toIssueDateFilterMenuItem);
		filtersPopupMenu.add(resultInMFCFilterMenuItem);
        filtersPopupMenu.add(mainFilterMenuItem);

		// кнопка реадктирования
		JButton editFilterButton = new JButton();
		editFilterButton.setIcon(ResourcesUtil.getIcon("pencil.png"));
        editFilterButton.setToolTipText("Изменить");
		editFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.editFilter(selectedFilterIndex);
			}
		});


		// кнопка удаления фильтра
		JButton removeFilterButton = new JButton();
		removeFilterButton.setIcon(ResourcesUtil.getIcon("delete.png"));
        removeFilterButton.setToolTipText("Удалить");
		removeFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.removeFilter(selectedFilterIndex);
			}
		});

		// кнопка очистки фильтра
		JButton resetFiltersButton = new JButton("Очистить");
		resetFiltersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.resetFilters();
                receptionCodeSearchTextField.setText("");
                rosreestrCodeSearchTextField.setText("");
			}
		});

		/// Применить фильтры
		JButton applyFiltersButton = new JButton("Применить");
		applyFiltersButton.setIcon(ResourcesUtil.getIcon("tick.png"));
		applyFiltersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.applyFilters();
				reestrTableModel.fireTableDataChanged();
				//setReestrTableColumnsWidth();
			}
		});

        /// Перечитать из БД и применить фильтры
        JButton readFromDBButton = new JButton("Перечитать");
        readFromDBButton.setIcon(ResourcesUtil.getIcon("database_refresh.png"));
        readFromDBButton.setToolTipText("Перечитать из базы данных");
        readFromDBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.readFromDBAndApplyFilters();
            }
        });



		// Собираем панель фильтра
        filterPanel.add(new JLabel("Дата открытия: c "), "span, split 5");
        filterPanel.add(fromDateField);
        filterPanel.add(new JLabel(" по "));
        filterPanel.add(toDateField);
        filterPanel.add(readFromDBButton, "wrap");
		filterPanel.add(filterListScrollPane, "push, w 500, h 80");
		filterPanel.add(applyFiltersButton,"pushy, grow, wrap");
		filterPanel.add(addFilterButton, "split");
		filterPanel.add(editFilterButton);
		filterPanel.add(removeFilterButton);
		filterPanel.add(resetFiltersButton, "wrap");

        JPanel exportPanel = new JPanel();
        exportPanel.setLayout(new BoxLayout(exportPanel,BoxLayout.Y_AXIS));
        exportPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Ведомости"));

        JButton exportSelectedColumnsButton = new JButton("Список");
        exportSelectedColumnsButton.setIcon(ResourcesUtil.getIcon("table.png"));
        exportSelectedColumnsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                controller.exportSelectedColumns();
            }
        });

        JButton exportForArchiveButton = new JButton("Для архива");
        exportForArchiveButton.setIcon(ResourcesUtil.getIcon("package.png"));
        exportForArchiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.exportForArchive();
            }
        });

        JButton exportForReturnButton = new JButton("Возврат");
        exportForReturnButton.setIcon(ResourcesUtil.getIcon("arrow_undo.png"));
        exportForReturnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.exportForReturn();
            }
        });

        exportPanel.add(exportSelectedColumnsButton);
        exportPanel.add(exportForArchiveButton);
        exportPanel.add(exportForReturnButton);

        topPanel.add(searchPanel);
        topPanel.add(filterPanel);
        topPanel.add(exportPanel);


		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill, nogrid"));

		final JButton columnsButton = new JButton();
		columnsButton.setIcon(ResourcesUtil.getIcon("wrench.png"));
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

		reestrTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent lse) {
				if (reestrTable.getSelectedRows().length==0) {
					openReceptionAction.setEnabled(false);
					changeReceptionsStatusAction.setEnabled(false);
                    selectMainReceptionAction.setEnabled(false);
                    resetMainReceptionAction.setEnabled(false);
					removeReceptionsAction.setEnabled(false);
				} else if (reestrTable.getSelectedRows().length==1) {
					openReceptionAction.setEnabled(true);
					changeReceptionsStatusAction.setEnabled(true);
                    selectMainReceptionAction.setEnabled(true);
                    resetMainReceptionAction.setEnabled(true);
					removeReceptionsAction.setEnabled(true);
					removeReceptionsAction.putValue(Action.NAME, "Удалить запрос");
				} else {
					openReceptionAction.setEnabled(false);
					changeReceptionsStatusAction.setEnabled(true);
                    selectMainReceptionAction.setEnabled(true);
                    resetMainReceptionAction.setEnabled(true);
					removeReceptionsAction.setEnabled(true);
					removeReceptionsAction.putValue(Action.NAME, "Удалить запросы");
				}
			}
		});

		/// добавление реакции на двойной клик - открытие приема на просмотр
		reestrTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ev) {
				if (ev.getClickCount() == 2) {
					int selectedReceptionIndex = reestrTable.getSelectedRow();
					controller.openReceptionDialog(selectedReceptionIndex);
					reestrTableModel.fireTableDataChanged();
				}
			}
		});



		JScrollPane reestrTableScrollPane = new JScrollPane(reestrTable);

		///////////// всплывающее меню для таблицы
		JPopupMenu reestrPopupMenu = new JPopupMenu();


		openReceptionAction = new OpenReceptionAction();
		openReceptionAction.setEnabled(false);
		JMenuItem openReceptionMenuItem = new JMenuItem(openReceptionAction);


		changeReceptionsStatusAction = new ChangeReceptionsStatusAction();
		changeReceptionsStatusAction.setEnabled(false);
		JMenu setReceptionStatusMenu = new JMenu(changeReceptionsStatusAction);
		List<ReceptionStatus> statuses = ReceptionStatusesModel.getInstance().getReceptionStatuses();
		for (ReceptionStatus status: statuses) {
			ChangeReceptionsStatusAction action = new ChangeReceptionsStatusAction(status);
			JMenuItem statusMenuItem = new JMenuItem(action);

			setReceptionStatusMenu.add(statusMenuItem);
		}

        JMenu mainReceptionMenu = new JMenu("Основное дело");

        selectMainReceptionAction = new SelectMainReceptionAction();
        selectMainReceptionAction.setEnabled(false);
        JMenuItem selectMainReceptionMenuItem = new JMenuItem(selectMainReceptionAction);

        resetMainReceptionAction = new ResetMainReceptionAction();
        resetMainReceptionAction.setEnabled(false);
        JMenuItem resetMainReceptionMenuItem = new JMenuItem(resetMainReceptionAction);

        mainReceptionMenu.add(selectMainReceptionMenuItem);
        mainReceptionMenu.add(resetMainReceptionMenuItem);

		removeReceptionsAction = new RemoveReceptionsAction();
		removeReceptionsAction.setEnabled(false);
		JMenuItem removeReceptionsMenuItem = new JMenuItem(removeReceptionsAction);

		reestrPopupMenu.add(openReceptionMenuItem);
		reestrPopupMenu.add(setReceptionStatusMenu);
        reestrPopupMenu.add(mainReceptionMenu);
		reestrPopupMenu.add(removeReceptionsMenuItem);

		reestrTable.setComponentPopupMenu(reestrPopupMenu);

		dataPanel.add(reestrTableScrollPane, "push, grow");

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		this.getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				setVisible(false);
			}
		});


		buttonPanel.add(okButton);


		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.pack();
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

    // Модель для управления списком фильтров
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

	public void tableDataChanged() {
		reestrTableModel.fireTableDataChanged();
	}

	public void tableStructureChanged() {
		reestrTableModel.fireTableStructureChanged();
	}


	class ReestrTableModel extends AbstractTableModel {

		private List<Reception> receptions;

		ReestrTableModel() {
			receptions = new ArrayList<Reception>();
		}

		void setReceptions(List<Reception> receptions) {
			this.receptions = receptions;
			fireTableDataChanged();
		}

		public int getRowCount() {
			return receptions.size();
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
			Reception reception = receptions.get(row);
			
			List<ReestrColumn> reestrColumns = ReestrColumns.getInstance().getActiveColumns();			


			Object value = null;

			if (column == 0) {
				value = row + 1;
			} else {
				value = reestrColumns.get(column - 1).getValue(reception);
			}

			return value;
		}

		public String getToolTipText(MouseEvent e) {
			String tip = null;
			java.awt.Point p = e.getPoint();
			int rowIndex = reestrTable.rowAtPoint(p);
			int colIndex = reestrTable.columnAtPoint(p);
			int realColumnIndex = reestrTable.convertColumnIndexToModel(colIndex);
			tip = getValueAt(rowIndex, realColumnIndex).toString();

			return tip;
		}


	}

	class OpenReceptionAction extends AbstractAction {
		OpenReceptionAction() {
			super("Открыть запрос");
		}

		public void actionPerformed(ActionEvent ev) {
			int selectedReceptionIndex = reestrTable.getSelectedRow();
			controller.openReceptionDialog(selectedReceptionIndex);
		}
	}

	class ChangeReceptionsStatusAction extends AbstractAction {
		
		private ReceptionStatus status = null;

		ChangeReceptionsStatusAction() {
			super("Изменить состояние");
		}

		ChangeReceptionsStatusAction(ReceptionStatus status) {
			super(status.getName());
			this.status = status;
		}

		public void actionPerformed(ActionEvent ev) {
			if (status == null) return;
			int[] selectedReceptionsIndexes = reestrTable.getSelectedRows();
			controller.changeReceptionsStatus(selectedReceptionsIndexes, status);
		}
	}

    class SelectMainReceptionAction extends AbstractAction {
        SelectMainReceptionAction() {super("Выбрать");}

        @Override
        public void actionPerformed(ActionEvent e) {
            int[] selectedReceptionsIndexes = reestrTable.getSelectedRows();
            controller.selectMainReception(selectedReceptionsIndexes);
        }
    }

    class ResetMainReceptionAction extends AbstractAction {
        ResetMainReceptionAction() {super("Сбросить");}

        @Override
        public void actionPerformed(ActionEvent e) {
            int[] selectedReceptionsIndexes = reestrTable.getSelectedRows();
            controller.resetMainReception(selectedReceptionsIndexes);
        }
    }

    class RemoveReceptionsAction extends AbstractAction {
		RemoveReceptionsAction() {
			super("Удалить запросы");
		}

		public void actionPerformed (ActionEvent ev) {
			int[] selectedReceptionsIndexes = reestrTable.getSelectedRows();
			controller.removeReceptions(selectedReceptionsIndexes);			
		}
	}

}	