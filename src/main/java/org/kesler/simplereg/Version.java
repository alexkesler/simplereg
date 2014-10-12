package org.kesler.simplereg;

/**
 * Класс для хранения версии приложения
 */
public class Version {

    private static String version = "1.3.6.3";
    private static String releaseDate = "12.10.2014";

    public static String getVersion() {
        return version;
    }

    public static String getReleaseDate() {
        return releaseDate;
    }
}
