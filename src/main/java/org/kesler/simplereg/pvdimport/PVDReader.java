package org.kesler.simplereg.pvdimport;

import org.apache.log4j.Logger;
import org.kesler.simplereg.util.OracleUtil;

import java.sql.*;

public abstract class PVDReader {
    private Logger log = Logger.getLogger(getClass().getSimpleName());

    public void read() throws PVDReaderException {

        try {
            Connection conn = OracleUtil.getConnection();
            Statement stmt = conn.createStatement();
            try {
                log.info("Query: " + getQuerySQL());
                ResultSet rs = stmt.executeQuery(getQuerySQL());
                try {
                    log.info("Processing >>>");
                    processRs(rs);
                    log.info("Processing complete");
                } finally {
                    rs.close();
                }
            } finally {
                stmt.close();
            }
        }  catch (SQLException ex) {
            ex.printStackTrace();
            throw new PVDReaderException(ex.getMessage());
        }

    }

    public abstract String getQuerySQL();
    public abstract void processRs(ResultSet rs) throws SQLException;


}
