package org.kesler.simplereg;

import javax.swing.SwingUtilities;

import org.kesler.simplereg.gui.main.MainViewController;
import org.kesler.simplereg.util.OptionsUtil;

public class SimpleReg {

	public static void main(String[] args) {
		
		OptionsUtil.readOptions();
		AppStarter starter = new AppStarter();
		SwingUtilities.invokeLater(starter);
	}

}

class AppStarter extends Thread {
	public void run() {
		MainViewController.getInstance().openMainView();
	}
}