package org.kesler.simplereg.gui.main;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.util.Properties;

import java.awt.GridBagLayout;



import org.kesler.simplereg.util.PropertiesUtil;

public class OptionsDialog extends JDialog {
	private Properties properties;

	public OptionsDialog() {
		properties = PropertiesUtil.getProperties();

	}

	private void createOptionsPane() {
		JPanel dataPanel = new JPanel(new GridBagLayout());
		dataPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

	}


}