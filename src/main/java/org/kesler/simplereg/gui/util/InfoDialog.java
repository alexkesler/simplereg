package org.kesler.simplereg.gui.util;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import net.miginfocom.swing.MigLayout;

import org.kesler.simplereg.util.ResourcesUtil;

public class InfoDialog extends JDialog {

	public static final int GREEN = 0;
	public static final int RED = 1;
	public static final int STAR = 3;
	public static final int ERROR = 4;

	private JDialog currentDialog;
	private int delay = 1000;
	private String info;
	private int mode = GREEN;

	public InfoDialog(JFrame parentFrame, String info, int delay, int mode) {
		super(parentFrame, false);
		this.delay = delay;
		this.info = info;
		this.mode = mode;
		currentDialog = this;

		createGUI();

		setLocationRelativeTo(parentFrame);


	}

	public InfoDialog(JDialog parentDialog, String info, int delay, int mode) {
		super(parentDialog, false);
		this.delay = delay;
		this.info = info;
		this.mode = mode;
		currentDialog = this;

		createGUI();

		setLocationRelativeTo(parentDialog);


	}

	public InfoDialog(JFrame parentFrame, String info, int delay) {
		this(parentFrame, info, delay, GREEN);
	}

	public InfoDialog(JDialog parentDialog, String info, int delay) {
		this(parentDialog, info, delay, GREEN);
	}


	public InfoDialog(JFrame parentFrame, String info) {
		this(parentFrame, info, 1000);
	}

	public InfoDialog(JDialog parentDialog, String info) {
		this(parentDialog, info, 1000);
	}


	private void createGUI() {
		JPanel infoPanel = new JPanel(new MigLayout("fill"));
		ImageIcon icon = null;
		switch (mode) {
			case GREEN: icon = ResourcesUtil.getIcon("bullet_green.png"); break;
			case RED: icon = ResourcesUtil.getIcon("bullet_red.png"); break;
			case STAR: icon = ResourcesUtil.getIcon("bullet_star.png"); break;
			case ERROR: icon = ResourcesUtil.getIcon("bullet_error.png"); break;
		}

		infoPanel.setBorder(BorderFactory.createMatteBorder(-1,-1,-1,-1,icon));

		JLabel infoLabel = new JLabel(info);

		infoPanel.add(infoLabel, "center");


		setContentPane(infoPanel);
		setSize(223,97);
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