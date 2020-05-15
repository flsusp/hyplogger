package org.hyplogger;

public class Conditions {

    public static ConditionalValue alwaysLog(Object value) {
        return log(value);
    }

    public static ConditionalValue nonNull(Object value) {
        return new ConditionalValue() {
            @Override
            public String value() {
                return value == null ? null : value.toString();
            }

            @Override
            public boolean shouldLog() {
                return value != null;
            }
        };
    }

    public static ConditionalValue log(Object value) {
        return new ConditionalValue() {
            @Override
            public String value() {
                return value == null ? null : value.toString();
            }

            @Override
            public boolean shouldLog() {
                return true;
            }
        };
    }
}
