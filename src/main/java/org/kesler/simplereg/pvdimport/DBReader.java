package org.kesler.simplereg.pvdimport;

import org.kesler.simplereg.util.OracleUtil;

import java.sql.*;

public abstract class DBReader {

    public void read() {

            Connection conn = OracleUtil.getConnection();

            try  {
                Statement stmt = conn.createStatement();
                try  {
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
         } catch (SQLException ex) {
            System.out.println("DB Error: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    public abstract String getQuerySQL();
    public abstract void processRs(ResultSet rs) throws SQLException;


}
