package org.kesler.simplereg.gui;

import javax.swing.JFrame;

import org.kesler.simplereg.Reception; 


public class ReceptionView extends JFrame {
	private Reception reception = null;
	private MainViewController controller = null;

	public ReceptionView(MainViewController controller) {
		this.controller = controller;
		createGUI();
	}

	public ReceptionView(MainViewController controller, Reception reception) {
		this.controller = controller;
		this.reception = reception;
		createGUI();
	}

	private void createGUI() {

	}

}