package org.hyplogger;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

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

class ValueSupplierCondition implements ConditionalValue {

    private final Supplier valueSupplier;

    public ValueSupplierCondition(Supplier valueSupplier) {
        this.valueSupplier = valueSupplier;
    }

    @Override
    public String value() {
        Object value = valueSupplier.get();
        return value == null ? null : value.toString();
    }

    @Override
    public boolean shouldLog() {
        return true;
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
