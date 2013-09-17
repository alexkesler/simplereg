package org.kesler.simplereg.gui.applicator;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;


import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.logic.applicator.FL;
import org.kesler.simplereg.logic.applicator.UL;
import org.kesler.simplereg.logic.applicator.ApplicatorUL;

class ApplicatorULDialog extends JDialog {

	private JFrame frame;
	private ApplicatorUL applicatorUL;

	ApplicatorULDialog (JFrame frame) {
		super(frame,"Заявитель - юридическое лицо", true);
		this.frame = frame;

	}

	private void createGUI() {

		JPanel mainPanel = new JPanel();
	}

}