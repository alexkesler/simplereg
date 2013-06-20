package org.kesler.simplereg.logic;

import java.util.List;
import java.util.ArrayList;


public class ReceptionList {
	private List<Reception> receptions;

	public ReceptionList() {
		receptions = new ArrayList<Reception>();
	}

	public ReceptionList(List<Reception> receptions) {
		this.receptions = receptions;
	}

	public List<Reception> getReceptions() {
		return receptions;
	}
}