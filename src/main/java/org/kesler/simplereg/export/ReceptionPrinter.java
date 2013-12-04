package org.kesler.simplereg.export;

import org.kesler.simplereg.logic.reception.Reception;

public abstract class ReceptionPrinter {

	protected Reception reception;

	ReceptionPrinter(Reception reception) {
		this.reception = reception;
	}

	public abstract void printReception();

	protected void saveFile() {
		
	}

}