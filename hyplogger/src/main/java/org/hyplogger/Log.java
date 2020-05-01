package org.hyplogger;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static org.hyplogger.Conditions.alwaysLog;
import static org.hyplogger.LogChainStep.DO_NOTHING;

public class Log {

    private final LevelLogger.Levels level;
    private final LogChainStep logChainStep;
    private final Map<String, ConditionalValue> attributes = new HashMap<>();

    public Log(LevelLogger.Levels level, LogChainStep logChainStep) {
        this.level = level;
        this.logChainStep = logChainStep;
    }

    public Log subject(Object subject) {
        attributes.put("subject", alwaysLog(subject));
        return this;
    }

    public void logTo(Logger logger) {
        logChainStep.proceedTo(DO_NOTHING, level, buildMessage(), logger);
    }

    private String buildMessage() {
        return attributes.entrySet().stream()
                .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue().value()))
                .collect(joining(" "));
    }
}
