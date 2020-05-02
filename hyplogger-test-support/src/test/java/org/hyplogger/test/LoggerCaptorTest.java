package org.hyplogger.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.hyplogger.LevelLogger.Levels.ERROR;
import static org.hyplogger.LevelLogger.error;
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
        LoggerCaptor.capturingAllLogs()
                .execute(() -> error().subject("test").logTo(logger))
                .andExpectLoggedMessage(ERROR, "subject=test");
    }

    @Test
    void captureLogAndException() {
        LoggerCaptor.capturingAllLogs()
                .execute(() -> {
                    error().subject("test").logTo(logger);
                    throw new RuntimeException();
                }).andExpectLoggedMessage("subject=test")
                .andExpectThatExceptionThrown()
                .isInstanceOf(RuntimeException.class);
    }
}
