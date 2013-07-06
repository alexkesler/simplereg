package org.kesler.simplereg.logic;

import java.util.List;
import java.util.ArrayList;


public class ReceptionsModel {
	private List<Reception> receptions;

	public ReceptionsModel() {
		receptions = new ArrayList<Reception>();
	}

	public ReceptionsModel(List<Reception> receptions) {
		this.receptions = receptions;
	}

	public List<Reception> getReceptions() {
		return receptions;
	}
}