package org.kesler.simplereg.gui.reestr.column;

import org.kesler.simplereg.logic.Reception;

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

	public abstract Object getValue(Reception reception);


}


