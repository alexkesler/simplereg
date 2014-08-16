package org.kesler.simplereg.pvdimport;

import java.sql.ResultSet;
import java.sql.SQLException;


public interface RSProcessor {
    public String getQuerySQL();
    public void processRs(ResultSet rs) throws SQLException;
}
