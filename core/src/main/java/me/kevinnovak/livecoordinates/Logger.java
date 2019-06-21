package me.kevinnovak.livecoordinates;

import java.util.logging.Level;

public class Logger {
    private java.util.logging.Logger _logger;
    private String _prefix;

    public Logger(java.util.logging.Logger logger, String prefix) {
        _logger = logger;
        _prefix = prefix;
    }

    public void info(String message) {
        log(Level.INFO, message);
    }

    public void warn(String message) {
        log(Level.WARNING, message);
    }

    public void error(String message) {
        log(Level.SEVERE, message);
    }

    private void log(Level level, String message) {
        _logger.log(level, _prefix + " " + message);
    }
}
