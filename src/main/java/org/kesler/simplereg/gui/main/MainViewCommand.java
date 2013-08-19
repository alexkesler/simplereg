package org.kesler.simplereg.gui.main;

/**
* Задает перечень команд для основного окна программы
*/
public enum MainViewCommand {
	Login("Войти","login"),
	Logout("Выйти","logout"), 
	NewReception("Новый прием","newReception"),
	UpdateReceptions("Обновить","updateReceptions"),
	OpenStatistic("Статистика","statistic"),
	OpenApplicators("Заявители", "applicators"),
	Services("Услуги", "services"),
	Operators("Операторы", "operators"),
	Options("Настройки", "options"),
	Exit("Закрыть","exit");


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