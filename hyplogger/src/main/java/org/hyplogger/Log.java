package org.hyplogger;

import org.slf4j.Logger;

import java.util.*;
import java.util.function.BooleanSupplier;

import static java.util.stream.Collectors.joining;
import static org.hyplogger.Conditions.alwaysLog;

public class Log {

    private final LevelLogger.Levels level;
    private final Collection<LogStep> logSteps;
    private final List<Map.Entry<String, ConditionalValue>> attributes = new ArrayList<>();
    private Exception exception;
    private BooleanSupplier condition = () -> true;

    public Log(LevelLogger.Levels level, Collection<LogStep> logSteps) {
        this.level = level;
        this.logSteps = logSteps;
    }

    public Log subject(Object subject) {
        return with("subject", alwaysLog(subject));
    }

    public Log with(String attribute, String message, Object... args) {
        return with(attribute, alwaysLog(String.format(message, args)));
    }

    public Log with(String attribute, Object value) {
        return with(attribute, alwaysLog(value));
    }

    public Log with(String attribute, ConditionalValue conditionalValue) {
        attributes.add(entry(attribute, conditionalValue));
        return this;
    }

    public Log exception(Exception exception) {
        this.exception = exception;
        return this;
    }

    public Log on(boolean condition) {
        return on(() -> condition);
    }

    public Log on(BooleanSupplier condition) {
        this.condition = condition;
        return this;
    }

    public void logTo(Logger logger) {
        if (level.isActiveFor(logger) && this.condition.getAsBoolean()) {
            for (LogStep logStep : logSteps) {
                logStep.execute(level, buildMessage(), exception, logger);
            }
        }
    }

    private String buildMessage() {
        return attributes.stream()
                .filter(entry -> entry.getValue().shouldLog())
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
