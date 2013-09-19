package org.kesler.simplereg.dao;

import java.util.List;

import org.kesler.simplereg.logic.applicator.FL;

/**
* Определяет методы для доступа к записям таблицы физических лиц
*/
public interface FLDAO {
	/**
	* Сохраняет в БД новое физ лицо
	* @param fl физическое лицо 
	* @return код сохраненной записи, в случае неудачи возвращает null
	*/
	public Long addFL(FL fl);

	/**
	* Сохраняет в БД новое физ лицо
	* @param fl физическое лицо 
	*/
	public void updateFL(FL fl);

	/**
	* Читает из базы данных {@link org.kesler.simplereg.logic.applicator.FL}
	* @param id код записи в базе данных
	* @return объект {@link org.kesler.simplereg.logic.applicator.FL} из базы данных
	*/
	public FL getFLById(Long id);

	/**
	* Читает из базы данных список {@link org.kesler.simplereg.logic.applicator.FL}
	* @return список {@link org.kesler.simplereg.logic.applicator.FL} из базы данных
	*/
	public List<FL> getAllFLs();

	/**
	* Удаляет из базы данных запись, соответствующую объекту {@link org.kesler.simplereg.logic.applicator.FL}
	* @param fl физическое лицо 
	*/
	public void deleteFL(FL fl);
}