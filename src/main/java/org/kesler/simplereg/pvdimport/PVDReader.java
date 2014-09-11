package org.kesler.simplereg.pvdimport;

import org.kesler.simplereg.util.OracleUtil;

import java.sql.*;

public abstract class PVDReader {

    public void read() throws PVDReaderException {

        try {
            Connection conn = OracleUtil.getConnection();
            Statement stmt = conn.createStatement();
            try {
                System.out.println("Query: " + getQuerySQL());
                ResultSet rs = stmt.executeQuery(getQuerySQL());
                try {
                    System.out.println("Processing >>>");
                    processRs(rs);
                    System.out.println("Processing complete");
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
