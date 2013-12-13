package org.kesler.simplereg.export;

import java.io.File;
import java.util.Set;
import java.util.HashMap;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.awt.Desktop;

import org.docx4j.Docx4J;
import org.docx4j.wml.P;
import org.docx4j.wml.Text;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import org.kesler.simplereg.util.OptionsUtil;
import org.kesler.simplereg.logic.Reception;

public class RosReestrReceptionPrinter extends ReceptionPrinter {

	WordprocessingMLPackage wordMLPackage;
	

	HashMap<String, String> mappings; 

	public RosReestrReceptionPrinter(Reception reception) {
		super(reception);

	}

	private boolean readTemplate() {
		
		String jarPath = OptionsUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String dirSeparator = System.getProperty("file.separator");

		String requestTemplateFileName = OptionsUtil.getOption("print.request");
		
		String templateDir = new File(jarPath).getParent() + dirSeparator + "templates" + dirSeparator;

		String requestTemplatePath = templateDir + requestTemplateFileName;

		File requestTemplateFile = new File(requestTemplatePath);
			
		JOptionPane.showMessageDialog(null,
								"Открываем файл " + requestTemplatePath ,
								"Инфо",
								JOptionPane.INFORMATION_MESSAGE);

		if (!requestTemplateFile.exists()) {
			JOptionPane.showMessageDialog(null,
									"Файл шаблона " + requestTemplateFileName + " не найден",
									"Ошибка",
									JOptionPane.ERROR_MESSAGE);
			return false;
		}


		try {
			wordMLPackage = WordprocessingMLPackage.load(requestTemplateFile);
		} catch (Docx4JException e) {
			JOptionPane.showMessageDialog(null,
									"Файл " + requestTemplateFileName + " не удалось открыть",
									"Ошибка",
									JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;

	}


	@Override
	public void printReception() {
		if (!readTemplate()) return;


		// Заполняем то, что необходимо заменить
		fillMappings();

		JOptionPane.showMessageDialog(null,
								"Заменяем данные ",
								"Инфо",
								JOptionPane.INFORMATION_MESSAGE);

		// Очищаем документ

		try {
			org.docx4j.model.datastorage.migration.VariablePrepare.prepare(wordMLPackage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

		List<Object> texts = ExportUtil.getAllElementFromObject(documentPart, Text.class);

		Set<String> keys = mappings.keySet();

		// заменяем текстовые вставки
		for (String key : keys) {
			String value = mappings.get(key);
			for (Object obj: texts) {
				if(obj instanceof Text) {
					Text textElem = (Text) obj;
					String text = textElem.getValue();
					if(text.contains(key)) {
						text = text.replace(key,value);
						textElem.setValue(text);
						System.out.println("-----Замена----" + key + "--на--" + value + "-----");						
					}
				}

			}
		}

		

		saveRequest();

	}

	private void fillMappings() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy г.");

		mappings = new HashMap<String, String>();
		mappings.put("v_ReceptionCode", reception.getReceptionCode());
		mappings.put("v_CurrentDate", dateFormat.format(reception.getOpenDate()));
		mappings.put("v_Operator", reception.getOperator().getFIO());
		mappings.put("v_Service", reception.getService().getName());

	}


	private void saveRequest() {

		String jarPath = OptionsUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String dirSeparator = System.getProperty("file.separator");


		String outDir = new File(jarPath).getParent() + dirSeparator + "out" + dirSeparator;

		File outDirFile = new File(outDir);
		if (!outDirFile.exists()) outDirFile.mkdir();

		String requestPath = outDir + "request.docx";

		JOptionPane.showMessageDialog(null,
								"Сохраняем файл " + requestPath ,
								"Ошибка",
								JOptionPane.INFORMATION_MESSAGE);

		
		try {
			System.out.println("---------Saving---------");
			Docx4J.save(wordMLPackage, new File(requestPath), Docx4J.FLAG_NONE);
		} catch (Docx4JException e) {
			JOptionPane.showMessageDialog(null,
									"Файл " + requestPath + " не удалось сохранить",
									"Ошибка",
									JOptionPane.ERROR_MESSAGE);
			
		}

		Desktop desktop = null;
		if (Desktop.isDesktopSupported()) {
		    desktop = Desktop.getDesktop();
		}

		//Открытие файла:

		try {
		    desktop.open(new File(requestPath));
		} catch (IOException ioe) {
		    ioe.printStackTrace();
		}



	}



}