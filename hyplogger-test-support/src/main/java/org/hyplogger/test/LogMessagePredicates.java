package org.hyplogger.test;

import static java.lang.String.format;

public class LogMessagePredicates {

    public static LogMessagePredicate withSubject(String subject) {
        return new LogMessagePredicate() {
            @Override
            public String describe() {
                return format("has subject '%s'", subject);
            }

            @Override
            public boolean test(LogMessage logMessage) {
                return logMessage.message.contains(format("subject=%s", subject))
                        || logMessage.message.contains(format("subject=\"%s\"", subject));
            }
        };
    }

    public static LogMessagePredicate withAttribute(String attribute, String value) {
        return new LogMessagePredicate() {
            @Override
            public String describe() {
                return format("has attribute '%s' with value '%s'", attribute, value);
            }

            @Override
            public boolean test(LogMessage logMessage) {
                return logMessage.message.contains(format("%s=%s", attribute, value))
                        || logMessage.message.contains(format("%s=\"%s\"", attribute, value));
            }
        };
    }
}
