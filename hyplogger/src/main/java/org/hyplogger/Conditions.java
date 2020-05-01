package org.hyplogger;

public class Conditions {

    public static ConditionalValue alwaysLog(Object value) {
        return () -> value == null ? null : value.toString();
    }
}
