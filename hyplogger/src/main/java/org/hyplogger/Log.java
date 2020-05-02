package org.hyplogger;

import org.slf4j.Logger;

import java.util.*;

import static java.util.stream.Collectors.joining;
import static org.hyplogger.Conditions.alwaysLog;
import static org.hyplogger.LogChain.DO_NOTHING;

public class Log {

    private final LevelLogger.Levels level;
    private final LogChain logChain;
    private final List<Map.Entry<String, ConditionalValue>> attributes = new ArrayList<>();

    public Log(LevelLogger.Levels level, LogChain logChain) {
        this.level = level;
        this.logChain = logChain;
    }

    public Log subject(Object subject) {
        attributes.add(entry("subject", alwaysLog(subject)));
        return this;
    }

    public Log with(String attribute, String message, Object... args) {
        attributes.add(entry(attribute, alwaysLog(String.format(message, args))));
        return this;
    }

    public Log with(String attribute, Object value) {
        attributes.add(entry(attribute, alwaysLog(value)));
        return this;
    }

    public void logTo(Logger logger) {
        if (level.isActiveFor(logger)) {
            logChain.proceedTo(DO_NOTHING, level, buildMessage(), logger);
        }
    }

    private String buildMessage() {
        return attributes.stream()
                .map(entry -> String.format("%s=%s", entry.getKey(), wrapWithDoubleQuotesIfRequired(entry.getValue().value())))
                .collect(joining(" "));
    }

    private String wrapWithDoubleQuotesIfRequired(String value) {
        if (value != null && value.matches(".*[ .,;|()\\[\\]{}'\"].*")) {
            return "\"" + value + "\"";
        }
        return value;
    }

    private Map.Entry<String, ConditionalValue> entry(String key, ConditionalValue value) {
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }
}
