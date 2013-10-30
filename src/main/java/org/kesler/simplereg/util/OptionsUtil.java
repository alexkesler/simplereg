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

	private static String dirSeparator = System.getProperty("file.separator");

	private static Properties options = new Properties();


	/**
	* Считывает настройки приложения из файла
	*/
	public static void readOptions() {

		File currentDir = new File(".");
		FileInputStream fis = null;
		try {			
			String filePath = currentDir.getCanonicalPath() + dirSeparator + fileName;

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
		options.setProperty("db.server","localhost");
		options.setProperty("db.driver", "h2");
		options.setProperty("db.user","oper");
		options.setProperty("db.password", "qwerty123");
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

		File currentDir = new File(".");

		FileOutputStream fos = null;
		try {			
			String filePath = currentDir.getCanonicalPath() + dirSeparator + fileName;

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