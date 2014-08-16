package org.kesler.simplereg.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;


public class OracleUtil {
    private static Connection connection;
    public static synchronized Connection getConnection() {
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

    private static void createConnection() {
        Properties prop = new Properties();
        prop.setProperty("user", "admin");
        prop.setProperty("password", "admin");

        String url = "jdbc:oracle:thin:@10.10.111.243:1521:xe";

        Locale prevLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Класс не зарегистрирован");
            ex.printStackTrace();
            return;
        }
        System.out.println("Класс зарегистрирован");

        try {
            connection = DriverManager.getConnection(url, prop);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Locale.setDefault(prevLocale);

    }

}
