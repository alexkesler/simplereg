package org.kesler.simplereg.dao;

import org.kesler.simplereg.logic.FL;

import java.util.List;

public interface FLDAO extends GenericDAO<FL>{
    public List<FL> getFLByFIO(String firstName, String surName, String parentName);
}
