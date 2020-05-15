package org.hyplogger.test;

import org.hyplogger.LevelLogger;

public class LogMessage {

    public final LevelLogger.Levels level;
    public final String message;
    public final Throwable throwable;

    LogMessage(LevelLogger.Levels level, String message, Throwable throwable) {
        this.level = level;
        this.message = message;
        this.throwable = throwable;
    }
}
