package org.hyplogger.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.hyplogger.LevelLogger.Levels.ERROR;
import static org.hyplogger.LevelLogger.error;
import static org.hyplogger.test.LogMessagePredicates.*;
import static org.hyplogger.test.LoggerCaptor.capturingAllLogs;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class LoggerCaptorTest {

    private final Logger logger = mock(Logger.class);

    @BeforeEach
    void setup() {
        doReturn(true).when(logger).isErrorEnabled();
    }

    @Test
    void captureLog() {
        capturingAllLogs()
                .execute(() -> error().subject("test").logTo(logger))
                .andExpectLogMessage(ERROR, "subject=test");
    }

    @Test
    void captureLogWithSubject() {
        capturingAllLogs()
                .execute(() -> error()
                        .subject("test")
                        .logTo(logger))
                .andExpectLogMessage(withSubject("test"));
    }

    @Test
    void captureLogWithException() {
        capturingAllLogs()
                .execute(() -> error()
                        .subject("test")
                        .exception(new RuntimeException())
                        .logTo(logger))
                .andExpectLogMessage(withSubject("test").and(exceptionOfType(RuntimeException.class)));
    }

    @Test
    void captureLogWithAttribute() {
        capturingAllLogs()
                .execute(() -> error()
                        .subject("test")
                        .with("attribute1", "value")
                        .with("attribute2", "value with double quotes")
                        .logTo(logger))
                .andExpectLogMessage(withAttribute("attribute1", "value")
                        .and(withAttribute("attribute2", "value with double quotes")));
    }

    @Test
    void captureLogAndException() {
        capturingAllLogs()
                .execute(() -> {
                    error().subject("test").logTo(logger);
                    throw new RuntimeException();
                }).andExpectLogMessage("subject=test")
                .andExpectThatExceptionThrown()
                .isInstanceOf(RuntimeException.class);
    }
}
