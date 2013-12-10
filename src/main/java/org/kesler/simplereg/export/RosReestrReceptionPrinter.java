package org.kesler.simplereg.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.swing.JOptionPane;


import org.kesler.simplereg.util.OptionsUtil;
import org.kesler.simplereg.logic.Reception;

public class RosReestrReceptionPrinter extends ReceptionPrinter {

	File requestTemplateFile;

	RosReestrReceptionPrinter(Reception reception) {
		super(reception);

	}

	private boolean readTemplate() {
		
		String jarPath = OptionsUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String dirSeparator = System.getProperty("file.separator");

		String requestTemplateFileName = OptionsUtil.getOption("print.request");
		
		String templateDir = new File(jarPath).getParent() + dirSeparator + "templates" + dirSeparator;

		String requestTemplatePath = templateDir + requestTemplateFileName;

		File requestTemplateFile = new File(requestTemplatePath);

		if (!requestTemplateFile.exists()) {
			JOptionPane.showMessageDialog(null,
									"Файл шаблона " + requestTemplateFileName + " не найден",
									"Ошибка",
									JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;

	}


	@Override
	public void printReception() {
		if (readTemplate()) return;

	}



}