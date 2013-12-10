package org.kesler.simplereg.dao;

import java.util.List;

import org.kesler.simplereg.logic.UL;

public interface ULDAO {
	/**
	* Сохраняет юр лицо в базу данных
	* @param ul объект юр лица
	*/
	public Long addUL(UL ul);

	/**
	* Обновляет юр лицо в базе данных
	* @param ul объект юр лица
	*/
	public void updateUL(UL ul);

	/**
	* Читаем юр лицо из базы данных
	* @param id индекс записи в БД
	* @return Юридическое лицо либо null при неудаче
	*/
	public UL getULById(Long id);

	/**
	* Читаем всех юр лиц из базы данных
	* @return Список всех юр лиц, сохраненных в базе данных
	*/
	public List<UL> getAllULs();

	/**
	* Удаляет юр лицо из базы данных
	* @param ul объект юр лица
	*/
	public void deleteUL(UL ul);
}