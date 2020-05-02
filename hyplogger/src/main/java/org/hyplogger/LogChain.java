package org.hyplogger;

import org.slf4j.Logger;

public interface LogChain {

    DoNothingLogChain DO_NOTHING = new DoNothingLogChain();

    default void proceedTo(LogChain logChain, LevelLogger.Levels level, String message, Logger logger) {
        proceedTo(level, message, logger);
        logChain.proceedTo(DO_NOTHING, level, message, logger);
    }

    void proceedTo(LevelLogger.Levels level, String message, Logger logger);
}

class DoNothingLogChain implements LogChain {
    @Override
    public void proceedTo(LogChain logChain, LevelLogger.Levels level, String message, Logger logger) {
    }

    @Override
    public void proceedTo(LevelLogger.Levels level, String message, Logger logger) {
    }
}
