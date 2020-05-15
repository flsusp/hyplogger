package org.hyplogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BooleanSupplier;

import static org.hyplogger.LevelLogger.warn;

public class Suppliers {

    private static final Logger logger = LoggerFactory.getLogger(Suppliers.class);

    public static BooleanSupplier ignoringExceptions(BooleanSupplier supplier) {
        return () -> {
            try {
                return supplier.getAsBoolean();
            } catch (Exception e) {
                warn().exception(e).logTo(logger);
                return false;
            }
        };
    }
}
