package org.kesler.simplereg.gui.reestr.column;

import org.kesler.simplereg.logic.reception.Reception;

public abstract class ReestrColumn {
	protected String name;
	protected String alias;
	protected int    width;

	public String getName() {
		return name;
	}

	public String getAlias() {
		return alias;
	}

	public int getWidth() {
		return width;
	}

	public abstract String getValue(Reception reception);


}


