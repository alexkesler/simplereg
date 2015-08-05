package org.kesler.simplereg;

/**
 * Класс для хранения версии приложения
 */
public class Version {

    private static String version = "1.3.8.10";
    private static String releaseDate = "05.08.2015";

    public static String getVersion() {
        return version;
    }

    public static String getReleaseDate() {
        return releaseDate;
    }
}
