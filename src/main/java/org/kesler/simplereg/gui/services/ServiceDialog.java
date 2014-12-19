package org.kesler.simplereg.gui.services;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.kesler.simplereg.gui.pvd.type.PVDPackageTypeDialogController;
import org.kesler.simplereg.gui.template.TemplateListDialogController;
import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.template.Template;
import org.kesler.simplereg.logic.template.TemplateService;
import org.kesler.simplereg.util.OptionsUtil;
import org.kesler.simplereg.util.ResourcesUtil;

public class ServiceDialog extends JDialog {
	private final Logger log = Logger.getLogger(this.getClass());

	public static int NONE = -1;
	public static int OK = 0;
	public static int CANCEL = 1;

	private JDialog parentDialog;
	private int result;

	private Service service;

    private JTextField codeTextField;
	private JTextArea nameTextArea;
	private JTextField templateTextField;
	private Template template;
	private JButton viewTemplateButton;
    private JTextArea pvdtypesPurposesTextArea;
	private JCheckBox enabledCheckBox;

	public ServiceDialog(JDialog parentDialog) {
		super(parentDialog,"Создать", true);
		this.parentDialog = parentDialog;
		result = NONE;

		service = new Service();
		//service.setName("Новая услуга");
		service.setEnabled(true);
		createGUI();
	}

	public ServiceDialog(JDialog parentDialog, Service service) {
		super(parentDialog,"Изменить",true);
		this.parentDialog = parentDialog;
		result = NONE;

		this.service = service;
		createGUI();
	}

	public int getResult() {
		return result;
	}

	public Service getService() {
		return service;
	}

	private void createGUI() {

		// Основная панель
		JPanel mainPanel = new JPanel(new BorderLayout());

		// Панель данных
		JPanel dataPanel = new JPanel(new MigLayout("fill"));

        codeTextField = new JTextField(10);

		nameTextArea = new JTextArea();
		nameTextArea.setLineWrap(true);
		nameTextArea.setWrapStyleWord(true);
		JScrollPane nameTextAreaScrollPane = new JScrollPane(nameTextArea);

		/// Поля для управления шаблоном запроса к услуге

		templateTextField  = new JTextField();
		viewTemplateButton = new JButton(ResourcesUtil.getIcon("magnifier.png"));
		viewTemplateButton.setToolTipText("Просмотреть");
		viewTemplateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewTemplate();
			}
		});

		JButton selectTemplateButton = new JButton(ResourcesUtil.getIcon("book_previous.png"));
		selectTemplateButton.setToolTipText("Выбрать из справочника");
		selectTemplateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				template = TemplateListDialogController.getInstance().showSelectDialog(ServiceDialog.this);
				updateTemplateTextField();
			}
		});
		templateTextField.setEnabled(false);
        pvdtypesPurposesTextArea = new JTextArea();
        JScrollPane pvdTypesTextAreaScrollPane = new JScrollPane(pvdtypesPurposesTextArea);

        JButton selectPVDTypesButton = new JButton("...");
        selectPVDTypesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectPVDTypes();
            }
        });

		enabledCheckBox = new JCheckBox("Действующая");

        // Собираем панель данных
        dataPanel.add(new JLabel("Номер соглашения"), "span, split 2");
        dataPanel.add(codeTextField, "wrap");
		dataPanel.add(new JLabel("Наименование: "), "wrap");
		dataPanel.add(nameTextAreaScrollPane, "span,pushy, grow");
		dataPanel.add(new JLabel("Шаблон"), "wrap");
		dataPanel.add(templateTextField,"span, split 3, grow");
		dataPanel.add(viewTemplateButton);
		dataPanel.add(selectTemplateButton);
        dataPanel.add(new JLabel("Коды типов ПК ПВД"), "wrap");
        dataPanel.add(pvdTypesTextAreaScrollPane, "span, split 2, push, grow");
        dataPanel.add(selectPVDTypesButton);
		dataPanel.add(enabledCheckBox);

		// Панель кнопок
		JPanel buttonPanel = new JPanel();

		JButton okButton = new JButton("Ok");
		okButton.setIcon(ResourcesUtil.getIcon("accept.png"));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (saveServiceFromGUI()) { // если удалось сохранить услугу
					result = OK;
					setVisible(false);					
				} 
			}
		});

		JButton cancelButton = new JButton("Отмена");
		cancelButton.setIcon(ResourcesUtil.getIcon("cancel.png"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				result = CANCEL;
				setVisible(false);
			}
		});

		// собираем панель кнопок

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		// собираем главную панель
		mainPanel.add(dataPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.setContentPane(mainPanel);
		this.setSize(500, 300);
		this.setLocationRelativeTo(parentDialog);

		loadGUIFromService();

		nameTextArea.requestFocus();

	}

	private void loadGUIFromService() {

        String code = service.getCode();
        if(code == null) code = "";

        codeTextField.setText(code);

        String name = service.getName();
		if (name == null) name = "";

		nameTextArea.setText(name);

		updateTemplateTextField();

		loadTemplate();

		Boolean enabled = service.getEnabled();
		if (enabled == null) enabled = false;

        String pvdtypesPurposesString = service.getPvdtypesPurposes()==null?"":service.getPvdtypesPurposes();
        pvdtypesPurposesTextArea.setText(pvdtypesPurposesString.replace(",", "\n"));

		enabledCheckBox.setSelected(enabled);
	}

	private boolean saveServiceFromGUI() {

        String code = codeTextField.getText();
        service.setCode(code);

		String name = nameTextArea.getText();
		if (!name.isEmpty()) {
			service.setName(name);
		} else {
			JOptionPane.showMessageDialog(this, "Наименование услуги не может быть пустым", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		service.setTemplateUuid(template.getUUID());

        String pvdtypesPurposesString = pvdtypesPurposesTextArea.getText().replaceAll("\n",",");
        service.setPvdtypesPurposes(pvdtypesPurposesString);

		service.setEnabled(enabledCheckBox.isSelected());

		return true;
	}

	private void updateTemplateTextField() {
		templateTextField.setText(template==null?"Не определен":template.getName());
		viewTemplateButton.setEnabled(template!=null);
	}

    private void selectPVDTypes() {

        String typeIDsString = pvdtypesPurposesTextArea.getText().replaceAll("\n",",");
        typeIDsString = PVDPackageTypeDialogController.getInstance().showDialog(this, typeIDsString);

        pvdtypesPurposesTextArea.setText(typeIDsString.replaceAll(",", "\n"));

    }

	private void viewTemplate() {
		if (template==null) return;


		OptionsUtil.createOutDir();

		String filePath = OptionsUtil.getCurrentDir()+"out"+OptionsUtil.getDirSeparator()+template.getName()+".docx";
		File file = new File(filePath);
		try {
			file.createNewFile();
		} catch (IOException e) {
			log.error("Can't create file: " + filePath);
			JOptionPane.showMessageDialog(this,
					"Не удалось создать файл: " + e.getMessage(),
					"Ошибка",
					JOptionPane.ERROR_MESSAGE);
		}
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			log.error("Can't find file for open");
			JOptionPane.showMessageDialog(this,
					"Не удалось открыть файл для записи" + e,
					"Ошибка",
					JOptionPane.ERROR_MESSAGE);
		}

		if (fileOutputStream != null) {
			try {
				fileOutputStream.write(template.getData());
			} catch (IOException e) {
				log.error("Cant write file", e);
				JOptionPane.showMessageDialog(this,
						"Ошибка при записи файла",
						"Ошибка",
						JOptionPane.ERROR_MESSAGE);
			} finally {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					log.error("Cant close OutputStream", e);
				}
			}
		}


		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			//Открытие файла:

			try {
				desktop.open(file);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}


	}

	private void loadTemplate() {
		TemplateLoadWorker templateLoadWorker = new TemplateLoadWorker();
		templateLoadWorker.execute();
	}


	class TemplateLoadWorker extends SwingWorker<Template, Void> {
		private final Logger log = Logger.getLogger(this.getClass());

		private TemplateService templateService;
		public TemplateLoadWorker() {
			templateService	= new TemplateService();
		}

		@Override
		protected Template doInBackground() throws Exception {
			Template t = templateService.getTemplateByUUID(service.getTemplateUuid());

			if (t == null) {
				t = templateService.getDefaultTemplate();
			}

			return t;
		}

		@Override
		protected void done() {
			try {
				Template t = get();
				template = t;
				updateTemplateTextField();
			} catch (InterruptedException e) {
				log.error("Interrupted",e);
			} catch (ExecutionException e) {
				log.error("Error reading template",e);
			}
		}

	}

}
