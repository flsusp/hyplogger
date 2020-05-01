package org.hyplogger;

import org.slf4j.Logger;

public class LevelLogger {

    private static LogChainStep logChainStep;

    public static Log level(Levels level) {
        return new Log(level, getLogChainStep());
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

    private static LogChainStep getLogChainStep() {
        if (logChainStep == null) {
            logChainStep = (level, message, logger) -> level.logTo(logger, message);
        }
        return logChainStep;
    }

    public enum Levels {
        DEBUG {
            @Override
            void logTo(Logger logger, String message) {
                logger.debug(message);
            }
        }, INFO {
            @Override
            void logTo(Logger logger, String message) {
                logger.info(message);
            }
        }, WARN {
            @Override
            void logTo(Logger logger, String message) {
                logger.warn(message);
            }
        }, ERROR {
            @Override
            void logTo(Logger logger, String message) {
                logger.error(message);
            }
        };

        abstract void logTo(Logger logger, String message);
    }
}
