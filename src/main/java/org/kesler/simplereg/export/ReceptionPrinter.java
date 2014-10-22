package org.kesler.simplereg.export;

import org.kesler.simplereg.logic.Reception;

public abstract class ReceptionPrinter {

	protected Reception reception;

	ReceptionPrinter(Reception reception) {
		this.reception = reception;
	}

	public abstract void printReception() throws Exception;

	protected void saveFile() {
		
	}

}