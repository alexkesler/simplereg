package org.kesler.simplereg.gui.reestr.column;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.miginfocom.swing.MigLayout;


public class ReestrColumnsDialog extends JDialog {

	private final boolean DEBUG = false;

	private JFrame frame;

	private InactiveColumnsListModel inactiveColumnsListModel;
	private int selectedInactiveColumnIndex = -1;
	private ReestrColumn selectedInactiveColumn = null;

	private ActiveColumnsListModel activeColumnsListModel;
	private int selectedActiveColumnIndex = -1;
	private ReestrColumn selectedActiveColumn = null;

	private ReestrColumns reestrColumns = null;



	public ReestrColumnsDialog(JFrame frame) {
		super(frame, true);
		this.frame = frame;
		reestrColumns = ReestrColumns.getInstance();

		createGUI();
	} 

	private void createGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = new JPanel(new MigLayout("fill"));

		
		// Модель для списка неактиыных полей
		inactiveColumnsListModel = new InactiveColumnsListModel();

		// Список неактивных полей
		final JList inactiveColumnsList = new JList(inactiveColumnsListModel);
		inactiveColumnsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		inactiveColumnsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent lse) {
				if(lse.getValueIsAdjusting() == false) {
					selectedInactiveColumnIndex = inactiveColumnsList.getSelectedIndex();
					if (DEBUG) System.out.println("selected inactive item: " + selectedInactiveColumnIndex);
					if (selectedInactiveColumnIndex != -1) {
						selectedInactiveColumn = reestrColumns.getInactiveColumns().get(selectedInactiveColumnIndex);
					} else {
						selectedInactiveColumn = null;
					}

				}				
			}
		});
        inactiveColumnsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    if (reestrColumns.activateColumn(selectedInactiveColumn)) {
                        inactiveColumnsListModel.columnRemoved(selectedInactiveColumnIndex);
                        activeColumnsListModel.columnAdded(reestrColumns.getActiveColumns().size()-1);
                    }

                }
            }
        });
		JScrollPane inactiveColumnsListScrollPane = new JScrollPane(inactiveColumnsList);

		// кнопка переноса неактивных полей в активные
		JButton activateColumnButton = new JButton(">");
		activateColumnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (selectedInactiveColumnIndex != -1) {
					if (reestrColumns.activateColumn(selectedInactiveColumn)) {
						inactiveColumnsListModel.columnRemoved(selectedInactiveColumnIndex);
						activeColumnsListModel.columnAdded(reestrColumns.getActiveColumns().size()-1);
					}

				}
				
			}
		});

		// кнопка переноса активных полей в неактивные
		JButton deactivateColumnButton = new JButton("<");
		deactivateColumnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (selectedActiveColumnIndex != -1) {
					if (reestrColumns.deactivateColumn(selectedActiveColumn)) {
						activeColumnsListModel.columnRemoved(selectedActiveColumnIndex);
						inactiveColumnsListModel.columnAdded(reestrColumns.getInactiveColumns().size()-1);
					}

				}
				
			}
		});

		// Модель для списка активных полей
		activeColumnsListModel = new ActiveColumnsListModel();

		// Список активных полей
		final JList activeColumnsList = new JList(activeColumnsListModel);
		activeColumnsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		activeColumnsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent lse) {
				if(lse.getValueIsAdjusting() == false) {
					selectedActiveColumnIndex = activeColumnsList.getSelectedIndex();
					if (DEBUG) System.out.println("selected active item: " + selectedActiveColumnIndex);
					if (selectedActiveColumnIndex != -1) {
						selectedActiveColumn = reestrColumns.getActiveColumns().get(selectedActiveColumnIndex);
					} else {
						selectedActiveColumn = null;
					}

				}				
			}
		});
        activeColumnsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount()==2) {
                    if (reestrColumns.deactivateColumn(selectedActiveColumn)) {
                        activeColumnsListModel.columnRemoved(selectedActiveColumnIndex);
                        inactiveColumnsListModel.columnAdded(reestrColumns.getInactiveColumns().size()-1);
                    }
                }
            }
        });
		JScrollPane activeColumnsListScrollPane = new JScrollPane(activeColumnsList);


		// Собираем панель данных
		dataPanel.add(new JLabel("Неактивные"));
		dataPanel.add(new JLabel("Активные"), "skip,wrap");
		dataPanel.add(inactiveColumnsListScrollPane , "push,grow, w 70, spany 2");
		dataPanel.add(activateColumnButton, "split 2, flowy");
		dataPanel.add(deactivateColumnButton);
		dataPanel.add(activeColumnsListScrollPane, "push, grow, w 70, spany 2");


		/// Панель кнопок 
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		this.getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				setVisible(false);
			}
		});

		buttonPanel.add(okButton);

		// Собираем основную панель
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.setSize(600, 300);
		this.setLocationRelativeTo(frame);

	}

	// модель для списка нективных полей
	class InactiveColumnsListModel extends AbstractListModel {

		@Override
		public int getSize() {
			return reestrColumns.getInactiveColumns().size();
		}

		@Override
		public String getElementAt(int index) {
			String element = "";

			element = reestrColumns.getInactiveColumns().get(index).getName();

			return element;
		}

		public void columnAdded(int index) {
			fireIntervalAdded(this,index,index);
		}

		public void columnRemoved(int index) {
			fireIntervalRemoved(this,index,index);
		}
	}

	// модель для списка активных полей
	class ActiveColumnsListModel extends AbstractListModel {

		@Override
		public int getSize() {
			return reestrColumns.getActiveColumns().size();
		}

		@Override
		public String getElementAt(int index) {
			String element = "";

			element = reestrColumns.getActiveColumns().get(index).getName();

			return element;
		}

		public void columnAdded(int index) {
			fireIntervalAdded(this,index,index);
		}

		public void columnRemoved(int index) {
			fireIntervalRemoved(this,index,index);
		}
	}	

}