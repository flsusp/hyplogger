package org.hyplogger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static org.hyplogger.Conditions.log;
import static org.hyplogger.Conditions.nonNull;
import static org.hyplogger.LevelLogger.debug;
import static org.hyplogger.Suppliers.ignoringExceptions;
import static org.mockito.Mockito.*;

class ConditionsTest {

    public static final BooleanSupplier FAILURE_BOOLEAN_CONDITION = () -> {
        throw new RuntimeException();
    };
    public static final Supplier FAILURE_CONDITION = () -> {
        throw new RuntimeException();
    };
    Logger logger = mock(Logger.class);

    @BeforeEach
    void setup() {
        doReturn(true).when(logger).isDebugEnabled();
    }

    @Nested
    class ForNonNullCondition {
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

    @Nested
    class ForBooleanCondition {
        @Test
        void doNotLogAttributeIfFalse() {
            debug().subject("test")
                    .with("attribute", log("value").on(false))
                    .logTo(logger);

            verify(logger).debug("subject=test");
        }

        @Test
        void logAttributeIfTrue() {
            debug().subject("test")
                    .with("attribute", log("value").on(true))
                    .logTo(logger);

            verify(logger).debug("subject=test attribute=value");
        }
    }

    @Nested
    class ForBooleanSupplierCondition {
        @Test
        void doNotLogAttributeIfSuppliedFalse() {
            debug().subject("test")
                    .with("attribute", log("value").on(() -> false))
                    .logTo(logger);

            verify(logger).debug("subject=test");
        }

        @Test
        void logAttributeIfSuppliedTrue() {
            debug().subject("test")
                    .with("attribute", log("value").on(() -> true))
                    .logTo(logger);

            verify(logger).debug("subject=test attribute=value");
        }

        @Nested
        class AndIgnoringExceptions {
            @Test
            void doNotLogAttributeIfSuppliedRaisesException() {
                debug().subject("test")
                        .with("attribute", log("value").on(ignoringExceptions(FAILURE_BOOLEAN_CONDITION)))
                        .logTo(logger);

                verify(logger).debug("subject=test");
            }
        }
    }

    @Nested
    class ForObjectSupplierCondition {

        @Test
        void logAttributeIfSuppliedReturns() {
            debug().subject("test")
                    .with("attribute", ignoringExceptions(() -> "value"))
                    .logTo(logger);

            verify(logger).debug("subject=test attribute=value");
        }

        @Nested
        class AndIgnoringExceptions {
            @Test
            void logNullAttributeIfSuppliedRaisesException() {
                debug().subject("test")
                        .with("attribute", ignoringExceptions(FAILURE_CONDITION))
                        .logTo(logger);

                verify(logger).debug("subject=test attribute=null");
            }
        }
    }
}
