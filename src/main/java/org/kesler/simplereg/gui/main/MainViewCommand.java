package org.kesler.simplereg.gui.main;

/**
* Задает перечень команд для основного окна программы
*/
public enum MainViewCommand {
	Login("Войти","login","connect.png"),
	Logout("Выйти","logout", "disconnect.png"),
    About("О программе","about","information.png"),
	NewReception("Новый прием","newReception", "asterisk_yellow.png"),
    NewReceptionFromPVD("Получить из ПК ПВД", "newReceptionFromPVD", "book_previous.png"),
    CheckReceptionStatus("Проверить статус / Выдать","checkReceptionStatus", "zoom.png"),
	UpdateReceptions("Обновить","updateReceptions", "arrow_refresh.png"),
	OpenStatistic("Статистика","statistic", "chart_pie.png"),
	OpenReceptionsReestr("Реестр","reestr","table.png"),
	OpenReceptionStatusChangesReestr("Реестр изменений состояний","statuschangesreestr","table_gear.png"),
	FLs("Физ лица", "fls", "group.png"),
	ULs("Юр лица", "uls", "chart_organisation.png"),
	Services("Услуги", "services", "book.png"),
	ReceptionStatuses("Статусы дел","statuses","flag_yellow.png"),
	RealtyObjects("Объекты недвижимости","realtyObjects","building.png"),
	RealtyObjectTypes("Типы недвижимости","realtyObjectTypes","house.png"),	
	Operators("Операторы", "operators", "user_suit.png"),
	Options("Настройки", "options", "wrench.png"),
    FIAS("Загрузка ФИАС","fias","building.png"),
    Issue("Выдача результата", "issue", "issue.png"),
	Exit("Закрыть","exit", "door_out.png");


	private String name;
	private String command;
	private String iconName;

	MainViewCommand(String name, String command) {
		this.name = name;
		this.command = command;
	}

	MainViewCommand(String name, String command, String iconName) {
		this.name = name;
		this.command = command;
		this.iconName = iconName;
	}

	String getName() {
		return name;
	}

	String getCommand() {
		return command;
	}

	String getIconName() {
		return iconName;
	}
}