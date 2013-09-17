package org.kesler.simplereg.gui.reception;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.util.ResourcesUtil;

import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.applicator.Applicator;

class ReceptionView extends JFrame{

	public static final int SERVICE_STATE = 0;
	public static final int APPLICATORS_STATE = 1;
	public static final int DATA_STATE = 2;
	public static final int PRINT_STATE = 3;

	private ReceptionViewController controller;
	private JButton backButton;
	private JButton nextButton;
	private JButton readyButton;
	private JButton cancelButton;

	private JTabbedPane tabbedPane;

	private ServicePanel servicePanel;
	private ApplicatorsPanel applicatorsPanel;
	private DataPanel dataPanel;
	private PrintPanel printPanel;



	public ReceptionView(ReceptionViewController controller) {
		super("Прием заявителя");
		this.controller = controller;
		createGUI();
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
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.back();
			}
		});

		nextButton = new JButton("Далее");
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.next();
			}
		});

		readyButton = new JButton("Готово");
		readyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.ready();
			}
		});

		cancelButton = new JButton("Отмена");
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

		this.setSize(700, 700);
		this.setLocationRelativeTo(null);
	}

	// Панель выбора услуги
	class ServicePanel extends JPanel {
		
		JLabel serviceNameLabel;

		ServicePanel() {
			super(new MigLayout("fillx"));

			serviceNameLabel = new JLabel();
			serviceNameLabel.setBorder(BorderFactory.createEtchedBorder());
			JButton selectServiceButton = new JButton("Выбрать");
			selectServiceButton.setIcon(ResourcesUtil.getIcon("book_previous.png"));
			selectServiceButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					controller.selectService();
				}
			});

			this.add(new JLabel("Услуга: "));
			this.add(serviceNameLabel,"pushx, grow");
			this.add(selectServiceButton, "right, wrap");

		}

		void setServiceName(String serviceName) {
			serviceNameLabel.setText("<html>"+serviceName+"</html>");
		}

	}

	// Панель выбора заявителей
	class ApplicatorsPanel extends JPanel {
		private JLabel serviceNameLabel;
		private ApplicatorsListModel applicatorsListModel;

		ApplicatorsPanel() {
			super(new MigLayout("fillx"));
			
			this.add(new JLabel("Услуга: "), "span, ay top");
			serviceNameLabel = new JLabel("Не определена");
			serviceNameLabel.setBorder(BorderFactory.createEtchedBorder());

			this.add(serviceNameLabel, "growx, wrap, gapbottom 10");

			this.add(new JLabel("Заявители: "),"wrap");

			applicatorsListModel = new ApplicatorsListModel(controller.getApplicators());
			JList applicatorsList = new JList(applicatorsListModel);
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
			ulMenuItem.setIcon(ResourcesUtil.getIcon("user_suit.png"));
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

			JButton editButton = new JButton();
			editButton.setIcon(ResourcesUtil.getIcon("pencil.png"));

			JButton deleteButton = new JButton();
			deleteButton.setIcon(ResourcesUtil.getIcon("delete.png"));

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

			void applicatorAdded(int index) {
				fireIntervalAdded(this,index,index);
			}

			void setApplicators(List<Applicator> applicators) {
				this.applicators = applicators;
			}
		}

		////// Методы для установки и обновления содержимого ApplicatorsPanel

		// Вызывается контроллером при добавлении заявителя
		void applicatorAdded(int index) {
			applicatorsListModel.applicatorAdded(index);
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

		DataPanel() {
			super(new MigLayout("fillx"));
		}

	}

	// панель для печати запроса
	class PrintPanel extends JPanel {

		PrintPanel() {
			super(new MigLayout());
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


}