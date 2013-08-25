package org.kesler.simplereg.util;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.swing.JOptionPane;

public class PropertiesUtil {

	private static final String fileName = "init.properties";
	private static String dirSeparator = System.getProperty("file.separator");

	private static Properties properties = new Properties();


	/**
	* Считывает настройки приложения из файла
	*/
	public static void readProperties() {

		File currentDir = new File(".");
		FileInputStream fis = null;
		try {			
			String filePath = currentDir.getCanonicalPath() + dirSeparator + fileName;

			fis = new FileInputStream(filePath);

			properties.loadFromXML(fis);

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
		properties = new Properties();
		properties.setProperty("db.server","localhost");
		properties.setProperty("db.driver", "h2");
		properties.setProperty("db.user","oper");
		properties.setProperty("db.password", "qwerty123");
	}


	/**
	* Возвращает настройки приложения в классе {@link: java.util.Properties}
	*/
	public static Properties getProperties() {
		if (properties == null) {
			readProperties();
		}
		return properties;
	}


	/**
	* Сохраняет настройки приложения в файл
	*/
	public static void saveProperties() {

		File currentDir = new File(".");
		FileOutputStream fos = null;
		try {			
			String filePath = currentDir.getCanonicalPath() + dirSeparator + fileName;

			fos = new FileOutputStream(filePath);

			properties.storeToXML(fos,"blanc");

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