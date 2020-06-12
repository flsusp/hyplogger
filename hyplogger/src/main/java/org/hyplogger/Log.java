package org.hyplogger;

import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.hyplogger.Conditions.alwaysLog;

public class Log {

    private static final Map.Entry[] NO_EXTRA_ATTRIBUTES = new Map.Entry[0];
    private final LevelLogger.Levels level;
    private final Collection<LogStep> logSteps;
    private final List<Map.Entry<String, ConditionalValue>> attributes = new ArrayList<>();
    private Exception exception;
    private BooleanSupplier condition = () -> true;
    private Logger logger;

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

    public Log with(String attribute, Supplier suppliedValue) {
        attributes.add(entry(attribute, new ValueSupplierCondition(suppliedValue)));
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

    public Log loggingTo(Logger logger) {
        this.logger = logger;
        return this;
    }

    public void logTo(Logger logger) {
        loggingTo(logger).registerLog();
    }

    private String buildMessage(Map.Entry<String, ConditionalValue>... extraAttributes) {
        return Stream.concat(attributes.stream(), Stream.of(extraAttributes))
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

    private void registerLog() {
        registerLog(NO_EXTRA_ATTRIBUTES);
    }

    private void registerLog(Map.Entry<String, ConditionalValue>... extraAttributes) {
        if (logger == null) {
            throw new RuntimeException("Log.logger should be defined. Please use `#loggingTo(Logger)` or `#logTo(Logger)`.");
        }
        if (level.isActiveFor(logger) && this.condition.getAsBoolean()) {
            for (LogStep logStep : logSteps) {
                logStep.execute(level, buildMessage(extraAttributes), exception, logger);
            }
        }
    }

    public <T> T call(Callable<T> callable) {
        registerLog(entry("status", alwaysLog("started")));

        long start = System.currentTimeMillis();
        boolean failed = false;
        try {
            return callable.call();
        } catch (Exception e) {
            failed = true;
            throw new RuntimeException(e);
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            registerLog(entry("status", alwaysLog(failed ? "failed" : "finished")), entry("elapsed", alwaysLog(elapsed)));
        }
    }

    public void call(Runnable runnable) {
        call(() -> {
            runnable.run();
            return null;
        });
    }
}
