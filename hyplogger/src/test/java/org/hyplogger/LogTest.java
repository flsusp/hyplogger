package org.hyplogger;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.hyplogger.LevelLogger.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LogTest {

    @Test
    void logSubjectWithLevelDebug() {
        Logger logger = mock(Logger.class);

        debug().subject("test")
                .logTo(logger);

        verify(logger).debug("subject=test");
    }

    @Test
    void logSubjectWithLevelInfo() {
        Logger logger = mock(Logger.class);

        info().subject("test")
                .logTo(logger);

        verify(logger).info("subject=test");
    }

    @Test
    void logSubjectWithLevelWarn() {
        Logger logger = mock(Logger.class);

        warn().subject("test")
                .logTo(logger);

        verify(logger).warn("subject=test");
    }

    @Test
    void logSubjectWithLevelError() {
        Logger logger = mock(Logger.class);

        error().subject("test")
                .logTo(logger);

        verify(logger).error("subject=test");
    }
}
