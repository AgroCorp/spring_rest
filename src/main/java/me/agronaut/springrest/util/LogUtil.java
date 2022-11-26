package me.agronaut.springrest.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {
    private Logger log;

    public LogUtil(Class<?> outerClass) {
        log = LogManager.getLogger(outerClass);
    }

    public void debug(String message) {
        log.debug(message);
    }

    public void debug(String message, String parameter, Object value) {
        String[] parameters = new String[1];
        parameters[0] = parameter;
        Object[] values = new Object[1];
        values[0] = value == null ? "null" : value;

        this.debug(message, parameters, values);
    }

    public void debug(String message,  String[] parameters, Object[] values) {
        StringBuilder sb = new StringBuilder(message);

        if (parameters != null && values != null && parameters.length != values.length) {
            log.error("Bad parameter and values length");
            return;
        }
        else if(parameters != null && values != null && parameters.length != 0 && values.length != 0 && parameters.length == values.length) {
            for (int i = 0; i < parameters.length; i++) {
                sb.append("\n\t[").append(parameters[i]).append("]\t").append(values[i].toString());
            }
        }
        if (log.isEnabled(Level.DEBUG)) {
            log.debug(sb.toString());
        }
    }
}
