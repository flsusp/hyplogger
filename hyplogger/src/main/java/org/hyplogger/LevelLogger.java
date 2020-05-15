package org.hyplogger;

import org.slf4j.Logger;

import java.util.Stack;

public class LevelLogger {

    private static final Stack<LogStep> logSteps = new Stack<>();

    static {
        logSteps.push((level, message, throwable, logger) -> level.log(logger, message, throwable));
    }

    public static void registerLogStep(LogStep logStep) {
        logSteps.push(logStep);
    }

    public static void unregisterLogStep(LogStep logStep) {
        logSteps.remove(logStep);
    }

    public static Log level(Levels level) {
        return new Log(level, logSteps);
    }

    public static Log debug() {
        return level(Levels.DEBUG);
    }

    public static Log info() {
        return level(Levels.INFO);
    }

    public static Log warn() {
        return level(Levels.WARN);
    }

    public static Log error() {
        return level(Levels.ERROR);
    }

    public enum Levels {
        DEBUG {
            @Override
            void log(Logger logger, String message, Throwable throwable) {
                if (throwable != null) {
                    logger.debug(message, throwable);
                } else {
                    logger.debug(message);
                }
            }

            @Override
            public boolean isActiveFor(Logger logger) {
                return logger.isDebugEnabled();
            }
        }, INFO {
            @Override
            void log(Logger logger, String message, Throwable throwable) {
                if (throwable != null) {
                    logger.info(message, throwable);
                } else {
                    logger.info(message);
                }
            }

            @Override
            public boolean isActiveFor(Logger logger) {
                return logger.isInfoEnabled();
            }
        }, WARN {
            @Override
            void log(Logger logger, String message, Throwable throwable) {
                if (throwable != null) {
                    logger.warn(message, throwable);
                } else {
                    logger.warn(message);
                }
            }

            @Override
            public boolean isActiveFor(Logger logger) {
                return logger.isWarnEnabled();
            }
        }, ERROR {
            @Override
            void log(Logger logger, String message, Throwable throwable) {
                if (throwable != null) {
                    logger.error(message, throwable);
                } else {
                    logger.error(message);
                }
            }

            @Override
            public boolean isActiveFor(Logger logger) {
                return logger.isErrorEnabled();
            }
        };

        abstract void log(Logger logger, String message, Throwable throwable);

        public abstract boolean isActiveFor(Logger logger);
    }
}
