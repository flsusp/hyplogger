package org.hyplogger.test;

import java.util.Objects;
import java.util.function.Predicate;

public interface LogMessagePredicate extends Predicate<LogMessage> {

    default String describe() {
        return "matches predicate";
    }

    default LogMessagePredicate and(LogMessagePredicate other) {
        Objects.requireNonNull(other);
        return new LogMessagePredicate() {
            @Override
            public String describe() {
                return LogMessagePredicate.this.describe() + " and " + other.describe();
            }

            @Override
            public boolean test(LogMessage logMessage) {
                return LogMessagePredicate.this.test(logMessage) && other.test(logMessage);
            }
        };
    }
}
