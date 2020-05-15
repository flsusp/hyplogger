package org.hyplogger;

public interface ConditionalValue {

    String value();

    boolean shouldLog();

    default ConditionalValue on(boolean condition) {
        return new BooleanCondition(condition, this);
    }
}

class BooleanCondition implements ConditionalValue {

    private final boolean condition;
    private final ConditionalValue wrappedCondition;

    public BooleanCondition(boolean condition, ConditionalValue wrappedCondition) {
        this.condition = condition;
        this.wrappedCondition = wrappedCondition;
    }

    @Override
    public String value() {
        return wrappedCondition.value();
    }

    @Override
    public boolean shouldLog() {
        return condition && wrappedCondition.shouldLog();
    }
}
