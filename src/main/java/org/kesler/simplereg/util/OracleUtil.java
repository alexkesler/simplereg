package org.kesler.simplereg.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;

public class OracleUtil {
    private static Connection connection;
    public static synchronized Connection getConnection() throws SQLException{
        if (connection == null) createConnection();
        return connection;
    }

    public static synchronized void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection = null;
    }

    private static void createConnection() throws SQLException{
        Properties prop = new Properties();
        prop.setProperty("user", "admin");
        prop.setProperty("password", "admin");

        String serverIp = OptionsUtil.getOption("pvd.serverip");

        String url = "jdbc:oracle:thin:@" + serverIp + ":1521:xe";

        Locale prevLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return;
        }

        connection = DriverManager.getConnection(url, prop);

        Locale.setDefault(prevLocale);

    }

}
