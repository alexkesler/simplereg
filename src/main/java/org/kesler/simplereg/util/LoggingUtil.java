package org.kesler.simplereg.util;

import org.apache.log4j.*;



public abstract class LoggingUtil {

    private static Logger log = Logger.getLogger(LoggingUtil.class);

    public static void updateLog4j4User(String user) {
        if (user.isEmpty()) user="anonym";
        log.info("Switching logging to user: " + user);
        Logger logger = Logger.getRootLogger();

        RollingFileAppender appender = new RollingFileAppender();
        appender.setName("file");
        appender.setFile("log/" + user + "/simplereg.log");
        appender.setLayout(new PatternLayout("%d{dd MMM yyyy HH:mm:ss} %-5p %c{1} - %m%n"));
        appender.setMaxFileSize("1MB");
        appender.setMaxBackupIndex(5);
        appender.activateOptions();

        logger.removeAppender("file");
        logger.addAppender(appender);

        log.info("Switched logging to user: " + user);
    }

    public static void reinitLog4j() {
        log.info("Reinit logging");
        Logger logger = Logger.getRootLogger();

        Logger.getRootLogger().getLoggerRepository();

        RollingFileAppender appender = new RollingFileAppender();
        appender.setName("file");
        appender.setFile("log/simplereg.log");
        appender.setLayout(new PatternLayout("%d{dd MMM yyyy HH:mm:ss} %-5p %c{1} - %m%n"));
        appender.setMaxFileSize("1MB");
        appender.setMaxBackupIndex(5);
        appender.activateOptions();

        logger.removeAppender("file");
        logger.addAppender(appender);


        log.info("Logging reinit successful");
    }
}
