package org.hyplogger;

import org.slf4j.Logger;

public interface LogChainStep {

    DoNothingLogChainStep DO_NOTHING = new DoNothingLogChainStep();

    default void proceedTo(LogChainStep nextLogChainStep, LevelLogger.Levels level, String message, Logger logger) {
        proceedTo(level, message, logger);
        nextLogChainStep.proceedTo(DO_NOTHING, level, message, logger);
    }

    void proceedTo(LevelLogger.Levels level, String message, Logger logger);
}

class DoNothingLogChainStep implements LogChainStep {
    @Override
    public void proceedTo(LogChainStep nextLogChainStep, LevelLogger.Levels level, String message, Logger logger) {
    }

    @Override
    public void proceedTo(LevelLogger.Levels level, String message, Logger logger) {
    }
}
