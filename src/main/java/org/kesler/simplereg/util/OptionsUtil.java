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
			options = getDefaultOptions(); 	// Загружаем настройки по умолчанию
			saveOptions();   		// Сохраняем настройки по умолчанию
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

	private static Properties getDefaultOptions() {
		Properties defaultOptions = new Properties();
        defaultOptions.setProperty("db.server","");
        defaultOptions.setProperty("db.driver", "h2 local");
        defaultOptions.setProperty("db.user","rroper");
        defaultOptions.setProperty("db.password", "q1w2e3R$");
        defaultOptions.setProperty("db.name","simplereg");
        defaultOptions.setProperty("reg.filial", "01");
        defaultOptions.setProperty("pvd.serverip", "10.10.111.243");
        defaultOptions.setProperty("logic.initRecStatusCode", "1");
        defaultOptions.setProperty("print.request","request.docx");
        return defaultOptions;
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
        String property = options.getProperty(propName);
        if (property==null) {
            property = getDefaultOptions().getProperty(propName);
            JOptionPane.showMessageDialog(null,
                    "Не найден параметр: " + propName + ". Используем значение: " + property + ". Проверьте и сохраните настройки."
            ,"Внимание!", JOptionPane.WARNING_MESSAGE);
            options.setProperty(propName,property);
        }
		return property;
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