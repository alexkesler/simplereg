package org.kesler.simplereg.gui.reception;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
// import java.beans.PropertyChangeEvent;
// import java.beans.PropertyChangeListener;

import net.miginfocom.swing.MigLayout;
import com.alee.extended.date.WebDateField;

import org.kesler.simplereg.util.ResourcesUtil;

import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.Applicator;

class MakeReceptionView extends JDialog{

	public static final int SERVICE_STATE = 0;
	public static final int APPLICATORS_STATE = 1;
	public static final int DATA_STATE = 2;
	public static final int PRINT_STATE = 3;

	private MakeReceptionViewController controller;
	private JButton backButton;
	private JButton nextButton;
	private JButton readyButton;
	private JButton cancelButton;

    private JDialog currentDialog;

	private JTabbedPane tabbedPane;

	private ServicePanel servicePanel;
	private ApplicatorsPanel applicatorsPanel;
	private DataPanel dataPanel;
	private PrintPanel printPanel;



	public MakeReceptionView(MakeReceptionViewController controller, JFrame parentFrame) {
		super(parentFrame, "Прием заявителя", true);
		this.controller = controller;
        currentDialog = this;
		createGUI();
		setLocationRelativeTo(parentFrame);
	}

	public MakeReceptionView(MakeReceptionViewController controller, JDialog parentDialog) {
		super(parentDialog, "Прием заявителя", true);
		this.controller = controller;
        currentDialog = this;
		createGUI();
		setLocationRelativeTo(parentDialog);
	}

	public void showView() {
		setVisible(true);
		dispose();
	}

	private void createGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel padPanel = new JPanel(new GridLayout(1,1));

		// создаем панель с вкладками
		tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.LEFT);
		//tabbedPane.setEnabled(false);


		servicePanel = new ServicePanel();
		applicatorsPanel = new ApplicatorsPanel();
		dataPanel = new DataPanel();
		printPanel = new PrintPanel();

		tabbedPane.add("Выбор услуги", servicePanel);
		tabbedPane.add("Заявители", applicatorsPanel);
		tabbedPane.add("Ввод данных", dataPanel);
		tabbedPane.add("Печать", printPanel);

		padPanel.add(tabbedPane);

		// Создаем панель кнопок, добавляем кнопки
		JPanel buttonPanel = new JPanel();

		backButton = new JButton("Назад");
		backButton.setIcon(ResourcesUtil.getIcon("resultset_previous.png"));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.back();
			}
		});

		nextButton = new JButton("Далее");
		nextButton.setIcon(ResourcesUtil.getIcon("resultset_next.png"));
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.next();
			}
		});

		readyButton = new JButton("Сохранить");
		readyButton.setIcon(ResourcesUtil.getIcon("tick.png"));
		readyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.ready();
			}
		});

		cancelButton = new JButton("Отмена");
		cancelButton.setIcon(ResourcesUtil.getIcon("cross.png"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.cancel();
			}
		});

		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(readyButton); 
		buttonPanel.add(cancelButton);


		mainPanel.add(padPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.add(mainPanel, BorderLayout.CENTER);

		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent ev) {
                int res = JOptionPane.showConfirmDialog(currentDialog,"Закрыть без сохранения?","Внимание",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
                if (res == JOptionPane.YES_OPTION) controller.cancel();
            }
        });

		this.setSize(700, 700);
		this.setLocationRelativeTo(null);
	}

	// Панель выбора услуги
	class ServicePanel extends JPanel {
		
		JTextField receptionCodeTextField;
		JLabel serviceNameLabel;
		JCheckBox byRecordCheckBox;

		ServicePanel() {
			super(new MigLayout("fillx"));

			receptionCodeTextField = new JTextField(15);
			/// При потере фокуса запоминаем код дела
			receptionCodeTextField.addFocusListener(new java.awt.event.FocusListener() {
				public void focusGained(java.awt.event.FocusEvent ev) {}
				public void focusLost(java.awt.event.FocusEvent ev) {
					controller.setReceptionCode(receptionCodeTextField.getText());
				}
			});

            JButton regenerateReceptionCodeButton = new JButton();
            regenerateReceptionCodeButton.setIcon(ResourcesUtil.getIcon("arrow_refresh.png"));
            regenerateReceptionCodeButton.setToolTipText("Пересчитать код дела");
            regenerateReceptionCodeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.regenerateReceptionCode();
                }
            });



			serviceNameLabel = new JLabel();
			serviceNameLabel.setBorder(BorderFactory.createEtchedBorder());
			JButton selectServiceButton = new JButton("Выбрать");
			selectServiceButton.setIcon(ResourcesUtil.getIcon("book_previous.png"));
			selectServiceButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					controller.selectService();
				}
			});

			byRecordCheckBox = new JCheckBox("По записи");
			byRecordCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					controller.setReceptionByRecord(byRecordCheckBox.isSelected());
				}
			});

			this.add(new JLabel("Код дела: "),"span, split 3");
			this.add(receptionCodeTextField);
            this.add(regenerateReceptionCodeButton, "wrap");
			this.add(new JLabel("Услуга: "),"ay top");
			this.add(serviceNameLabel,"pushx, grow");
			this.add(selectServiceButton, "right, wrap");
			this.add(byRecordCheckBox,"span,wrap");

		}

		void setReceptionCode(String receptionCode) {
			receptionCodeTextField.setText(receptionCode);
		}

		void setServiceName(String serviceName) {
			serviceNameLabel.setText("<html>"+serviceName+"</html>");
		}

		void setByRecord(boolean byRecord) {
			byRecordCheckBox.setSelected(byRecord);
		}

	}

	// Панель выбора заявителей
	class ApplicatorsPanel extends JPanel {
		private JLabel serviceNameLabel;
		private ApplicatorsListModel applicatorsListModel;
		private int selectedApplicatorIndex;

		ApplicatorsPanel() {
			super(new MigLayout("fillx"));
			
			this.add(new JLabel("Услуга: "), "span, ay top");
			serviceNameLabel = new JLabel("Не определена");
			serviceNameLabel.setBorder(BorderFactory.createEtchedBorder());

			this.add(serviceNameLabel, "growx, wrap, gapbottom 10");

			this.add(new JLabel("Заявители: "),"wrap");

			// Добавляем список заявителей
			applicatorsListModel = new ApplicatorsListModel(controller.getApplicators());
			JList applicatorsList = new JList(applicatorsListModel);

			applicatorsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			applicatorsList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent lse) {
					selectedApplicatorIndex = lse.getFirstIndex();
				}
			});

			JScrollPane applicatorsListScrollPane = new JScrollPane(applicatorsList);


			this.add(applicatorsListScrollPane, "span, growx");

			
			// Кнопка добавления заявителя
			final JButton addButton = new JButton();
			addButton.setIcon(ResourcesUtil.getIcon("add.png"));

			final JPopupMenu applicatorSelectorPopupMenu = new JPopupMenu();
			JMenuItem flMenuItem = new JMenuItem("Физ. лицо");
			flMenuItem.setIcon(ResourcesUtil.getIcon("user.png"));
			flMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					controller.addApplicatorFL();
				}
			});
			JMenuItem ulMenuItem = new JMenuItem("Юр. лицо");
			ulMenuItem.setIcon(ResourcesUtil.getIcon("chart_organisation.png"));
			ulMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					controller.addApplicatorUL();
				}
			});

			applicatorSelectorPopupMenu.add(flMenuItem);
			applicatorSelectorPopupMenu.add(ulMenuItem);

			addButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					applicatorSelectorPopupMenu.show(addButton,addButton.getWidth(),0);
				}
			});

			// Кнопка редактирования заявителя 
			JButton editButton = new JButton();
			editButton.setIcon(ResourcesUtil.getIcon("pencil.png"));
			editButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					controller.editApplicator(selectedApplicatorIndex);
				}
			});

			// Кнопка удаления заявителя
			JButton deleteButton = new JButton();
			deleteButton.setIcon(ResourcesUtil.getIcon("delete.png"));
			deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					controller.removeApplicator(selectedApplicatorIndex);
				}
			});


			this.add(addButton,"split");
			this.add(editButton);
			this.add(deleteButton, "wrap");

		}

		// Модель данных для JList заявителей
		class ApplicatorsListModel<Applicator> extends AbstractListModel {
			List<Applicator> applicators;

			ApplicatorsListModel(List<Applicator> applicators) {
				this.applicators = applicators;
			}

			@Override
			public Applicator getElementAt(int index) {
				return applicators.get(index);
			}

			@Override
			public int getSize() {
				return applicators.size();
			}

			public void applicatorAdded(int index) {
				fireIntervalAdded(this,index,index);
			}

			public void applicatorUpdated(int index) {
				fireContentsChanged(this,index,index);
			}

			public void applicatorRemoved(int index) {
				fireIntervalRemoved(this,index,index);
			}

			void setApplicators(List<Applicator> applicators) {
				this.applicators = applicators;
				fireContentsChanged(this, 0, applicators.size()-1);
			}
		}

		////// Методы для установки и обновления содержимого ApplicatorsPanel

		// Вызывается контроллером при добавлении заявителя
		void applicatorAdded(int index) {
			applicatorsListModel.applicatorAdded(index);
		}

		void applicatorUpdated(int index) {
			applicatorsListModel.applicatorUpdated(index);
		}

		void applicatorRemoved(int index) {
			applicatorsListModel.applicatorRemoved(index);
		}

		// Вызывается ApplicatorsReceptionViewState
		void setApplicators(List<Applicator> applicators) {
			applicatorsListModel.setApplicators(applicators);
		}

		void setServiceName(String serviceName) {
			serviceNameLabel.setText("<html>" + serviceName + "</html>");
		} 


	}

	// панель для ввода даннных по услуге
	class DataPanel extends JPanel {

		JLabel realtyObjectNameLabel;

		WebDateField toIssueWebDateField;

		JTextField rosreestrCodeTextField;

		JCheckBox resultInMFCCheckBox;

		DataPanel() {
			super(new MigLayout("fillx"));

			realtyObjectNameLabel = new JLabel("Не определено");
			realtyObjectNameLabel.setBorder(BorderFactory.createEtchedBorder());

			JButton selectRealtyObjectButton = new JButton();
			selectRealtyObjectButton.setIcon(ResourcesUtil.getIcon("book_previous.png"));
			selectRealtyObjectButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					controller.selectRealtyObject();
				}
			});

			toIssueWebDateField = new WebDateField();
			toIssueWebDateField.addDateSelectionListener(new com.alee.extended.date.DateSelectionListener() {
				public void dateSelected(Date date) {
					controller.setToIssueDate(date);
				}
			});


			rosreestrCodeTextField = new JTextField(15);
			rosreestrCodeTextField.addFocusListener(new java.awt.event.FocusListener() {
				public void focusGained(java.awt.event.FocusEvent ev) {}
				public void focusLost(java.awt.event.FocusEvent ev) {
					controller.setRosreestrCode(rosreestrCodeTextField.getText());
				}

			});


			resultInMFCCheckBox = new JCheckBox("Результат получать в МФЦ");
			resultInMFCCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					controller.setResultInMFC(resultInMFCCheckBox.isSelected());
				}
			});

			this.add(new JLabel("Объект недвижимости"), "wrap");
			this.add(realtyObjectNameLabel, "span, split 2, growx");
			this.add(selectRealtyObjectButton, "ay top, wrap");

			this.add(new JLabel("Срок выдачи результата"));
			this.add(toIssueWebDateField, "w 150, wrap");

			this.add(new JLabel("Код дела Росреестра: "));
			this.add(rosreestrCodeTextField, "wrap");

			this.add(resultInMFCCheckBox,"wrap");

		}

		public void setRealtyObjectName(String name) {
			realtyObjectNameLabel.setText("<html>" + name + "</html>");
		}

		Date getToIssueDate() {
			Date toIssueDate = toIssueWebDateField.getDate();
			return toIssueDate;
		}

		void setToIssueDate(Date toIssueDate) { 
			toIssueWebDateField.setDate(toIssueDate);
		}

		String getRosreestrCode() {
			return rosreestrCodeTextField.getText();
		}

		void setRosreestrCode(String rosreestrCode) {
			rosreestrCodeTextField.setText(rosreestrCode);
		}

		void setResultInMFC(boolean resultInMFC) {
			resultInMFCCheckBox.setSelected(resultInMFC);
		}

	}

	// панель для печати запроса
	class PrintPanel extends JPanel {

		PrintPanel() {
			super(new MigLayout());

			JButton printButton = new JButton("Распечатать запрос");
			printButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					controller.printRequest();
				}
			});

			this.add(printButton);
		}

	}

	// отработка нажатия общих кнопок

	JButton getBackButton() {
		return backButton;
	}

	JButton getNextButton() {
		return nextButton;
	}

	JButton getReadyButton() {
		return readyButton;
	}

	JTabbedPane getTabbedPane() {
		return tabbedPane;
	}


	// возвращает панели для доступа к их содержимому
	ServicePanel getServicePanel() {
		return servicePanel;
	}

	ApplicatorsPanel getApplicatorsPanel() {
		return applicatorsPanel;
	}

	DataPanel getDataPanel() {
		return dataPanel;
	}


}