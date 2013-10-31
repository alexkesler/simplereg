package org.kesler.simplereg.gui.util;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.util.ResourcesUtil;

public class InfoDialog extends JDialog {

	private JDialog currentDialog;
	private int delay = 1000;
	private String info;

	public InfoDialog(JFrame parentFrame, String info, int delay) {
		super(parentFrame, false);
		this.delay = delay;
		this.info = info;
		currentDialog = this;

		createGUI();

		setLocationRelativeTo(parentFrame);


	}

	public InfoDialog(JDialog parentDialog, String info, int delay) {
		super(parentDialog, false);
		this.delay = delay;
		this.info = info;
		currentDialog = this;

		createGUI();

		setLocationRelativeTo(parentDialog);


	}

	private void createGUI() {
		JPanel infoPanel = new JPanel(new MigLayout("fill"));
		infoPanel.setBorder(BorderFactory.createMatteBorder(-1,-1,-1,-1,ResourcesUtil.getIcon("bullet_green.png")));

		JLabel infoLabel = new JLabel(info);

		infoPanel.add(infoLabel, "center");


		setContentPane(infoPanel);
		setSize(300,100);
		setUndecorated(true);

	}


	public void showInfo() {
		Timer timer = new Timer(delay, new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				currentDialog.setVisible(false);
				currentDialog.dispose();
			}
		});
		timer.setRepeats(false);
		timer.start();

		currentDialog.setVisible(true);
		// timer.stop();
		// timer = null;

	}

}