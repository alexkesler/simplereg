package org.kesler.simplereg.pvdimport;


import org.kesler.simplereg.util.OracleUtil;

import java.sql.*;

public class DBReader {

    public static void read(RSProcessor rsProcessor) {

            Connection conn = OracleUtil.getConnection();

            try  {
                Statement stmt = conn.createStatement();
                try  {
                    System.out.println("Query: " + rsProcessor.getQuerySQL());
                    ResultSet rs = stmt.executeQuery(rsProcessor.getQuerySQL());
                    try {
                        System.out.println("Processing >>>");
                        rsProcessor.processRs(rs);
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

 }
