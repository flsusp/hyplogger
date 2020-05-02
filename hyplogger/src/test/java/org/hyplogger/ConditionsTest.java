package org.hyplogger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.hyplogger.Conditions.nonNull;
import static org.hyplogger.LevelLogger.debug;
import static org.mockito.Mockito.*;

class ConditionsTest {

    Logger logger = mock(Logger.class);

    @BeforeEach
    void setup() {
        doReturn(true).when(logger).isDebugEnabled();
    }

    @Nested
    class WhenConditionIsNonNull {
        @Test
        void doNotLogAttributeIfNull() {
            debug().subject("test")
                    .with("attribute", nonNull(null))
                    .logTo(logger);

            verify(logger).debug("subject=test");
        }

        @Test
        void logAttributeIfNotNull() {
            debug().subject("test")
                    .with("attribute", nonNull("not_null"))
                    .logTo(logger);

            verify(logger).debug("subject=test attribute=not_null");
        }
    }
}
