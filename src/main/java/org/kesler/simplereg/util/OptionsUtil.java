package org.kesler.simplereg.util;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.swing.JOptionPane;

public class OptionsUtil {

	private static final String fileName = "init.properties";

	private static String currentDir;

	private static Properties options = new Properties();

	static {

		String jarPath = OptionsUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String dirSeparator = System.getProperty("file.separator");
		
		currentDir = new File(jarPath).getParent() + dirSeparator;
	}

	public static String getCurrentDir() {
		return currentDir;
	}


	/**
	* Считывает настройки приложения из файла
	*/
	public static void readOptions() {

		// String jarPath = OptionsUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();

		// File currentDir = new File(jarPath).getParentFile();
		FileInputStream fis = null;
		try {			
			String filePath = currentDir + fileName;

			fis = new FileInputStream(filePath);

			options.loadFromXML(fis);

		} catch (FileNotFoundException fnfe) {
			JOptionPane.showMessageDialog(null,
									"Файл настроек не найден. Будут применены настройки по умолчанию."+
									"\nПроведите корректную настройку приложения.",
									"Файл не найден",
									JOptionPane.ERROR_MESSAGE);
			setDefaultOptions();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null,
									ioe.toString(),
									"Ошибка ввода-вывода",
									JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				if (fis != null) {
					fis.close();				
				}					
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(null,
									ioe.toString(),
									"Ошибка ввода-вывода",
									JOptionPane.ERROR_MESSAGE);
				
			}
		}
	}

	private static void setDefaultOptions() {
		options = new Properties();
		options.setProperty("db.server","");
		options.setProperty("db.driver", "h2 local");
		options.setProperty("db.user","rroper");
		options.setProperty("db.password", "q1w2e3R$");
		options.setProperty("logic.initRecStatusCode", "1");
	}


	/**
	* Возвращает настройки приложения в классе {@link java.util.Properties}
	*/
	public static Properties getOptions() {
		if (options == null) {
			readOptions();
		}
		return options;
	}

	/**
	* Возвращает опцию из настроек приложения, по имени
	* @param propName Наименоание опции
	*/
	public static String getOption(String propName) {
		if (options == null) {
			readOptions();
		}
		return options.getProperty(propName);
	}

	/**
	* Задает опцию в настройках приложения, по имени
	* @param propName Наименоание опции
	*/
	public static void setOption(String propName, String value) {
		if (options == null) {
			readOptions();
		}
		options.setProperty(propName, value);
	}

	/**
	* Сохраняет настройки приложения в файл
	*/
	public static void saveOptions() {


		FileOutputStream fos = null;
		try {			
			String filePath = currentDir + fileName;

			fos = new FileOutputStream(filePath);

			options.storeToXML(fos,"blanc");

		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null,
										ioe.getMessage(),
										"Ошибка ввода-вывода",
										JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(null,
									ioe.toString(),
									"Ошибка ввода-вывода",
									JOptionPane.ERROR_MESSAGE);				
			}
			
		}

	}

}