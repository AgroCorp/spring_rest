package me.agronaut.springrest.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.InvalidParameterException;

public class LogUtil {
    private final Logger log;

    public LogUtil(Class<?> outerClass) {
        log = LogManager.getLogger(outerClass);
    }


    public void debug(String message) {
        log.debug(message);
    }
    public void warn(String message) {log.warn(message);}
    public void error(String message) {log.error(message);}

    public void debug(String message, String parameter, Object value) {
        String[] parameters = new String[1];
        parameters[0] = parameter;
        Object[] values = new Object[1];
        values[0] = value == null ? "null" : value;

        this.debug(message, parameters, values);
    }
    public void warn(String message, String parameter, Object value) {
        String[] parameters = new String[1];
        parameters[0] = parameter;
        Object[] values = new Object[1];
        values[0] = value == null ? "null" : value;

        this.warn(message, parameters, values);
    }
    public void error(String message, String parameter, Object value) {
        String[] parameters = new String[1];
        parameters[0] = parameter;
        Object[] values = new Object[1];
        values[0] = value == null ? "null" : value;

        this.error(message, parameters, values);
    }

    public void debug(String message,  String[] parameters, Object[] values) {
        if (log.isEnabled(Level.DEBUG)) {
            StringBuilder sb = getLogBuilder(message, parameters, values);
            if (!sb.isEmpty()) {
                log.debug(sb.toString());
            }
        }
    }

    public void warn(String message, String[] parameters, Object[] values) {
        if (log.isEnabled(Level.WARN)) {
            StringBuilder sb = getLogBuilder(message, parameters, values);
            if (!sb.isEmpty()) {
                log.warn(sb.toString());
            }
        }
    }
    public void error(String message, String[] parameters, Object[] values) {
        if (log.isEnabled(Level.ERROR)) {
            StringBuilder sb = getLogBuilder(message, parameters, values);
            if (!sb.isEmpty()) {
                log.error(sb.toString());
            }
        }
    }

    private StringBuilder getLogBuilder(String message, String[] parameters, Object[] values) throws InvalidParameterException {
        StringBuilder sb = new StringBuilder();
        if (message != null) {
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
