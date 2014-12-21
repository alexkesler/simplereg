package org.kesler.simplereg.dao.support;

/**
 * Created by alex on 19.12.14.
 */
public class DAOException extends RuntimeException {
    public DAOException() { super(); }
    public DAOException(String message) { super(message); }
    public DAOException(Throwable throwable) { super(throwable); }
    public DAOException(String message, Throwable throwable) { super(message, throwable); }
}
