package org.hyplogger;

import org.slf4j.Logger;

public class LevelLogger {

    private static LogChain logChain;

    public static Log level(Levels level) {
        return new Log(level, getLogChain());
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

    private static LogChain getLogChain() {
        if (logChain == null) {
            logChain = (level, message, logger) -> level.log(logger, message);
        }
        return logChain;
    }

    public enum Levels {
        DEBUG {
            @Override
            void log(Logger logger, String message) {
                logger.debug(message);
            }

            @Override
            public boolean isActiveFor(Logger logger) {
                return logger.isDebugEnabled();
            }
        }, INFO {
            @Override
            void log(Logger logger, String message) {
                logger.info(message);
            }

            @Override
            public boolean isActiveFor(Logger logger) {
                return logger.isInfoEnabled();
            }
        }, WARN {
            @Override
            void log(Logger logger, String message) {
                logger.warn(message);
            }

            @Override
            public boolean isActiveFor(Logger logger) {
                return logger.isWarnEnabled();
            }
        }, ERROR {
            @Override
            void log(Logger logger, String message) {
                logger.error(message);
            }

            @Override
            public boolean isActiveFor(Logger logger) {
                return logger.isErrorEnabled();
            }
        };

        abstract void log(Logger logger, String message);

        public abstract boolean isActiveFor(Logger logger);
    }
}
