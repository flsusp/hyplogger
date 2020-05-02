package org.hyplogger;

import org.slf4j.Logger;

public interface LogStep {

    void execute(LevelLogger.Levels level, String message, Logger logger);
}
