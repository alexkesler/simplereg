package org.kesler.simplereg.gui;

import org.kesler.simplereg.logic.Operator;

public class CurrentOperator {
	private static Operator operator = null;

	private CurrentOperator() {}

	public static void setOperator(Operator o) {
		operator = o;
	}

	public static Operator getOperator() {
		return operator;
	} 

	public static void resetOperator() {
		operator = null;
	}
}