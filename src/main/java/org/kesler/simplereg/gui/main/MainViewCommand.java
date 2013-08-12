package org.kesler.simplereg.gui.main;

public enum MainViewCommand {
	Login("Подключиться","login"), 
	NewReception("Новый прием","newReception"),
	UpdateReceptions("Обновить","updateReceptions"),
	OpenStatistic("Статистика","statistic"),
	OpenApplicators("Заявители", "applicators"),
	Services("Услуги", "services"),
	Operators("Операторы", "operators"),
	Exit("Выйти","exit");


	private String name;
	private String command;

	MainViewCommand(String name, String command) {
		this.name = name;
		this.command = command;
	}

	String getName() {
		return name;
	}

	String getCommand() {
		return command;
	}
}