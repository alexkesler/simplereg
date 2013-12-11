package org.kesler.simplereg.export;

import java.io.File;
import java.util.HashMap;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.swing.JOptionPane;

import org.docx4j.Docx4J;
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

		MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

		fillMappings();

		JOptionPane.showMessageDialog(null,
								"Заменяем данные " ,
								"Инфо",
								JOptionPane.INFORMATION_MESSAGE);

		try {
			documentPart.variableReplace(mappings);
			System.out.println("-------Replacing----------");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
									"Не удалось заменить данные",
									"Ошибка",
									JOptionPane.ERROR_MESSAGE);
			
		}	
		

		saveRequest();
		
	}

	private void fillMappings() {

		mappings = new HashMap<String, String>();
		mappings.put("#ReceptionCode#", reception.getReceptionCode());

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

	}



}