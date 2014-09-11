package org.kesler.simplereg;

/**
 * Класс для хранения версии приложения
 */
public class Version {

    private static String version = "1.3.3";
    private static String releaseDate = "11.09.2014";

    public static String getVersion() {
        return version;
    }

    public static String getReleaseDate() {
        return releaseDate;
    }
}
