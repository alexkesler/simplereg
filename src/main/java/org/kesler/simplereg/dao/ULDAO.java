package org.kesler.simplereg.dao;

import org.kesler.simplereg.logic.applicator.UL;

public interface ULDAO {
	/**
	* Сохраняет юр лицо в базу данных
	* @param ul объект юр лица
	*/
	public Long add(UL ul);

	/**
	*
	*/
	public UL getULById(Long id);
}