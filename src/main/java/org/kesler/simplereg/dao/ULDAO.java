package org.kesler.simplereg.dao;

import org.kesler.simplereg.dao.support.DAOException;
import org.kesler.simplereg.logic.UL;

import java.util.List;

public interface ULDAO extends GenericDAO<UL> {
    public List<UL> getULsByShortName(String shortName) throws DAOException;
    public List<UL> getULsByFullName(String fullName) throws DAOException;
}
