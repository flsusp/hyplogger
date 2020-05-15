package org.hyplogger;

import java.util.function.BooleanSupplier;

public interface ConditionalValue {

    String value();

    boolean shouldLog();

    default ConditionalValue on(boolean condition) {
        return new BooleanCondition(condition, this);
    }

    default ConditionalValue on(BooleanSupplier condition) {
        return new BooleanSupplierCondition(condition, this);
    }
}

class BooleanSupplierCondition implements ConditionalValue {

    private final BooleanSupplier condition;
    private final ConditionalValue wrappedCondition;

    public BooleanSupplierCondition(BooleanSupplier condition, ConditionalValue wrappedCondition) {
        this.condition = condition;
        this.wrappedCondition = wrappedCondition;
    }

    @Override
    public String value() {
        return wrappedCondition.value();
    }

    @Override
    public boolean shouldLog() {
        return condition.getAsBoolean() && wrappedCondition.shouldLog();
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
