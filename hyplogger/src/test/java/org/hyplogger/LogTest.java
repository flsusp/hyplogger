package org.hyplogger;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.slf4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hyplogger.LevelLogger.*;
import static org.mockito.Mockito.*;

class LogTest {

    @Test
    void logWithLevelDebug() {
        Logger logger = mock(Logger.class);
        doReturn(true).when(logger).isDebugEnabled();

        debug().subject("test")
                .logTo(logger);

        verify(logger).debug("subject=test");
    }

    @Test
    void doesNotLogWithLevelDebugIsDisabled() {
        Logger logger = mock(Logger.class);
        doReturn(false).when(logger).isDebugEnabled();

        debug().subject("test")
                .logTo(logger);

        verify(logger, never()).debug("subject=test");
    }

    @Test
    void logWithLevelInfo() {
        Logger logger = mock(Logger.class);
        doReturn(true).when(logger).isInfoEnabled();

        info().subject("test")
                .logTo(logger);

        verify(logger).info("subject=test");
    }

    @Test
    void doesNotLogWithLevelInfoisDisabled() {
        Logger logger = mock(Logger.class);
        doReturn(false).when(logger).isInfoEnabled();

        info().subject("test")
                .logTo(logger);

        verify(logger, never()).info("subject=test");
    }

    @Test
    void logWithLevelWarn() {
        Logger logger = mock(Logger.class);
        doReturn(true).when(logger).isWarnEnabled();

        warn().subject("test")
                .logTo(logger);

        verify(logger).warn("subject=test");
    }

    @Test
    void doesNotLogWithLevelWarnIfDisabled() {
        Logger logger = mock(Logger.class);
        doReturn(false).when(logger).isWarnEnabled();

        warn().subject("test")
                .logTo(logger);

        verify(logger, never()).warn("subject=test");
    }

    @Test
    void logWithLevelError() {
        Logger logger = mock(Logger.class);
        doReturn(true).when(logger).isErrorEnabled();

        error().subject("test")
                .logTo(logger);

        verify(logger).error("subject=test");
    }

    @Test
    void doesNotLogWithLevelErrorIfDisabled() {
        Logger logger = mock(Logger.class);
        doReturn(false).when(logger).isErrorEnabled();

        error().subject("test")
                .logTo(logger);

        verify(logger, never()).error("subject=test");
    }

    @Test
    void logGenericAttribute() {
        Logger logger = mock(Logger.class);
        doReturn(true).when(logger).isInfoEnabled();

        info().subject("test")
                .with("attribute", "value")
                .logTo(logger);

        verify(logger).info("subject=test attribute=value");
    }

    @Test
    void logGenericAttributeMessageFormattedWithArgs() {
        Logger logger = mock(Logger.class);
        doReturn(true).when(logger).isInfoEnabled();

        info().subject("test")
                .with("attribute", "messageis%s", "hello")
                .logTo(logger);

        verify(logger).info("subject=test attribute=messageishello");
    }

    @Test
    void logGenericAttributeMessageWithDoubleQuotes() {
        Logger logger = mock(Logger.class);
        doReturn(true).when(logger).isInfoEnabled();

        info().subject("test")
                .with("attribute", "my message")
                .logTo(logger);

        verify(logger).info("subject=test attribute=\"my message\"");
    }

    @Test
    void logSameAttributeTwice() {
        Logger logger = mock(Logger.class);
        doReturn(true).when(logger).isInfoEnabled();

        info().subject("test")
                .with("attribute", "value1")
                .with("attribute", "value2")
                .logTo(logger);

        verify(logger).info("subject=test attribute=value1 attribute=value2");
    }

    @Test
    void logWhenConditionalLoggingIsTrue() {
        Logger logger = mock(Logger.class);
        doReturn(true).when(logger).isInfoEnabled();

        info().subject("test")
                .on(true)
                .logTo(logger);

        verify(logger).info("subject=test");
    }

    @Test
    void doesNotLogWhenConditionalLoggingIsFalse() {
        Logger logger = mock(Logger.class);
        doReturn(true).when(logger).isInfoEnabled();

        info().subject("test")
                .on(false)
                .logTo(logger);

        verify(logger, never()).info(anyString());
    }

    @Test
    void logWhenConditionalLoggingIsSuppliedWithTrue() {
        Logger logger = mock(Logger.class);
        doReturn(true).when(logger).isInfoEnabled();

        info().subject("test")
                .on(() -> true)
                .logTo(logger);

        verify(logger).info("subject=test");
    }

    @Test
    void doesNotLogWhenConditionalLoggingIsSuppliedWithFalse() {
        Logger logger = mock(Logger.class);
        doReturn(true).when(logger).isInfoEnabled();

        info().subject("test")
                .on(() -> false)
                .logTo(logger);

        verify(logger, never()).info(anyString());
    }

    @Test
    void failsIfNoLoggerRegistered() {
        Logger logger = mock(Logger.class);
        doReturn(true).when(logger).isInfoEnabled();

        assertThatThrownBy(() -> info().subject("test")
                .call(() -> {
                }))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Log.logger should be defined");
    }

    @Test
    void logStopwatchInformationWhenEmbeddedExecution() {
        Logger logger = mock(Logger.class);
        doReturn(true).when(logger).isInfoEnabled();

        info().subject("test")
                .loggingTo(logger)
                .call(() -> {
                });

        InOrder order = inOrder(logger);
        order.verify(logger).info("subject=test status=started");
        order.verify(logger).info(ArgumentMatchers.matches("subject=test status=finished elapsed=\\d+"));
    }

    @Test
    void logStopwatchFailureInformationWhenEmbeddedExecutionFails() {
        Logger logger = mock(Logger.class);
        doReturn(true).when(logger).isInfoEnabled();

        assertThatThrownBy(() -> info().subject("test")
                .loggingTo(logger)
                .call(() -> {
                    throw new RuntimeException();
                })).isInstanceOf(RuntimeException.class);

        InOrder order = inOrder(logger);
        order.verify(logger).info("subject=test status=started");
        order.verify(logger).info(ArgumentMatchers.matches("subject=test status=failed elapsed=\\d+"));
    }

    @Test
    void logStopwatchInformationWhenEmbeddedExecutionWithResponse() {
        Logger logger = mock(Logger.class);
        doReturn(true).when(logger).isInfoEnabled();

        int value = info().subject("test")
                .loggingTo(logger)
                .call(() -> {
                    return 1;
                });

        assertThat(value).isEqualTo(1);

        InOrder order = inOrder(logger);
        order.verify(logger).info("subject=test status=started");
        order.verify(logger).info(ArgumentMatchers.matches("subject=test status=finished elapsed=\\d+"));
    }

    @Test
    void logStopwatchFailureInformationWhenEmbeddedExecutionWithResponseFails() {
        Logger logger = mock(Logger.class);
        doReturn(true).when(logger).isInfoEnabled();

        assertThatThrownBy(() -> info().subject("test")
                .loggingTo(logger)
                .call(() -> {
                    if (true) {
                        throw new RuntimeException();
                    }
                    return 1;
                })).isInstanceOf(RuntimeException.class);

        InOrder order = inOrder(logger);
        order.verify(logger).info("subject=test status=started");
        order.verify(logger).info(ArgumentMatchers.matches("subject=test status=failed elapsed=\\d+"));
    }
}
