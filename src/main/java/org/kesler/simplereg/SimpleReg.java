package org.kesler.simplereg;

import javax.swing.SwingUtilities;

import com.alee.laf.WebLookAndFeel;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.kesler.simplereg.gui.main.MainViewController;
import org.kesler.simplereg.util.OptionsUtil;

public class SimpleReg {
    private static Logger log;

	public static void main(String[] args) {

        log = Logger.getLogger(SimpleReg.class.getSimpleName());
        log.info("Reading options");
		OptionsUtil.readOptions();
		WebLookAndFeel.install();
		AppStarter starter = new AppStarter();
        log.info("Creating main window");
		SwingUtilities.invokeLater(starter);
	}

}

class AppStarter extends Thread {
	public void run() {
		MainViewController.getInstance().openMainView();
	}
}