package org.kesler.simplereg;

/**
 * Класс для хранения версии приложения
 */
public class Version {

    private static String version = "1.2.2";
    private static String releaseDate = "21.06.2014";

    public static String getVersion() {
        return version;
    }

    public static String getReleaseDate() {
        return releaseDate;
    }
}
