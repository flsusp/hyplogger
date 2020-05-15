package org.hyplogger.test;

import org.assertj.core.api.AbstractThrowableAssert;
import org.hyplogger.LevelLogger;
import org.hyplogger.LogStep;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LoggerCaptor implements LogStep {

    private List<LogMessage> capturedLogMessages = new ArrayList<>();
    private Exception exception;

    public static LoggerCaptor capturingAllLogs() {
        return new LoggerCaptor();
    }

    public LoggerCaptor execute(Runnable runnable) {
        LevelLogger.registerLogStep(this);
        try {
            runnable.run();
        } catch (Exception exception) {
            this.exception = exception;
        } finally {
            LevelLogger.unregisterLogStep(this);
        }
        return this;
    }

    public AbstractThrowableAssert<?, ? extends Throwable> andExpectThatExceptionThrown() {
        return assertThat(exception);
    }

    public void execute(LevelLogger.Levels level, String message, Throwable throwable, Logger logger) {
        capturedLogMessages.add(new LogMessage(level, message, throwable));
    }

    public LoggerCaptor andExpectLogMessage(LevelLogger.Levels level, String message) {
        assertThat(capturedLogMessages)
                .as("Expected message '%s' with level %s not found.\nThe messages found are:\n%s", message, level, printCapturedMessages())
                .anyMatch(logMessage -> logMessage.level == level && logMessage.message.equals(message));
        return this;
    }

    public LoggerCaptor andExpectLogMessage(String message) {
        assertThat(capturedLogMessages)
                .as("Expected message '%s' not found.\nThe messages found are:\n%s", message, printCapturedMessages())
                .anyMatch(logMessage -> logMessage.message.equals(message));
        return this;
    }

    public LoggerCaptor andExpectLogMessage(LogMessagePredicate predicate) {
        assertThat(capturedLogMessages)
                .as("Expected to find message that %s.\nThe messages found are:\n%s", predicate.describe(), printCapturedMessages())
                .anyMatch(predicate);
        return this;
    }

    private String printCapturedMessages() {
        return this.capturedLogMessages.stream()
                .map(logMessage -> String.format("> %s: %s", logMessage.level, logMessage.message))
                .collect(Collectors.joining("\n"));
    }
}
