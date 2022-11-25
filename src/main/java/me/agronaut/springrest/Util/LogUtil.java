package me.agronaut.springrest.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {
    private static Logger log;

    public LogUtil(Class<?> outerClass) {
        log = LogManager.getLogger(outerClass);
    }

    public void debug(String message) {

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

        if ((parameters == null || parameters.length == 0) && (values == null || values.length == 0)) {
            log.debug(sb.toString());
        } else {
            if (parameters != null && values != null && parameters.length != values.length) {
                log.error("Bad parameter and values length");
                return;
            }
            for (int i = 0; i < parameters.length; i++) {
                sb.append("\n\t[").append(parameters[i]).append("]\t").append(values[i].toString());
            }
            log.debug(sb.toString());
        }
    }
}
