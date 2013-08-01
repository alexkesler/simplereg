package org.kesler.simplereg.gui.statistic;

public class StatisticViewController {
	private static StatisticViewController instance = null;
	private StatisticView view;

	private StatisticViewController() {
		view = new StatisticView();
	}

	public static synchronized StatisticViewController getInstance() {
		if (instance == null) {
			instance = new StatisticViewController();
		}

		return instance;
	}

	public void openView() {
		view.setVisible(true);
	}

}