package me.agronaut.springrest.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.InvalidParameterException;

/**
 * Use for custom log pattern
 *
 */
public class LogUtil {
    private final Logger log;

    /**
     * Constructor of LogUtil
     * @param outerClass class which we want to logging
     */
    public LogUtil(Class<?> outerClass) {
        log = LogManager.getLogger(outerClass);
    }

    /**
     * Debug log simple String message
     * @param message log message
     */
    public void debug(String message) {
        log.debug(message);
    }

    /**
     * Warn log simple String message
     * @param message log message
     */
    public void warn(String message) {
        log.warn(message);
    }

    /**
     * Error log simple String message
     * @param message log message
     */
    public void error(String message) {
        log.error(message);
    }

    /**
     * Debug log simple String message
     * @param message log message
     * @param parameter parameter to log
     * @param  value parameter's value
     */
    public void debug(String message, String parameter, Object value) {
        String[] parameters = new String[1];
        parameters[0] = parameter;
        Object[] values = new Object[1];
        values[0] = value == null ? "null" : value;

        this.debug(message, parameters, values);
    }

    /**
     * Warn log simple String message
     * @param message log message
     * @param parameter parameter to log
     * @param  value parameter's value
     */
    public void warn(String message, String parameter, Object value) {
        String[] parameters = new String[1];
        parameters[0] = parameter;
        Object[] values = new Object[1];
        values[0] = value == null ? "null" : value;

        this.warn(message, parameters, values);
    }

    /**
     * Error log simple String message
     * @param message log message
     * @param parameter parameter to log
     * @param  value parameter's value
     */
    public void error(String message, String parameter, Object value) {
        String[] parameters = new String[1];
        parameters[0] = parameter;
        Object[] values = new Object[1];
        values[0] = value == null ? "null" : value;

        this.error(message, parameters, values);
    }

    /**
     * Debug log simple String message
     * @param message log message
     * @param parameters array of parameters to log
     * @param  values array of parameter's values
     */
    public void debug(String message, String[] parameters, Object[] values) {
        StringBuilder sb = getLogBuilder(message, parameters, values);
        if (!sb.isEmpty() && log.isDebugEnabled()) {
            log.debug(sb.toString());
        }
    }

    /**
     * Warn log simple String message
     * @param message log message
     * @param parameters array of parameters to log
     * @param  values array of parameter's values
     */
    public void warn(String message, String[] parameters, Object[] values) {
        StringBuilder sb = getLogBuilder(message, parameters, values);
        if (!sb.isEmpty() && log.isWarnEnabled()) {
            log.warn(sb.toString());
        }
    }

    /**
     * Error log simple String message
     * @param message log message
     * @param parameters array of parameters to log
     * @param  values array of parameter's values
     */
    public void error(String message, String[] parameters, Object[] values) {
        StringBuilder sb = getLogBuilder(message, parameters, values);
        if (!sb.isEmpty() && log.isErrorEnabled()) {
            log.error(sb.toString());
        }
    }

    /**
     * Convert arrays to simple string for logging
     * @param message log message
     * @param parameters array of parameters Strings
     * @param values array of parameter's values
     * @return String builder with context of log text
     * @throws InvalidParameterException when parameters and values length not same
     */
    private StringBuilder getLogBuilder(String message, String[] parameters, Object[] values) throws InvalidParameterException {
        StringBuilder sb = new StringBuilder();
        if (message != null && !message.isEmpty()) {
            sb.append(message);

            if (parameters != null && values != null && parameters.length != values.length) {
                throw new InvalidParameterException("Bad parameter and values length");
            } else if (parameters != null && values != null && parameters.length != 0) {
                for (int i = 0; i < parameters.length; i++) {
                    if (parameters[i] != null && values[i] != null) {
                        sb.append("\n\t[").append(parameters[i]).append("]\t").append(values[i].toString());
                    }
                }
            }
        }
        return sb;
    }
}
