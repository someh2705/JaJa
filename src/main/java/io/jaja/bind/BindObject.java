package io.jaja.bind;

import io.jaja.Diagnostics;

public class BindObject<T> {
    private T value;

    public BindObject(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public BindObject<?> plus(BindObject<?> other) {
        if (isInt() && other.isInt()) {
            return new BindObject<>((Integer) value + (Integer) other.value);
        }

        if (isInt() && other.isString()) {
            return new BindObject<>((int) value + (String) other.value);
        }

        if (isString() && other.isInt()) {
            return new BindObject<>((String) value + (int) other.value);
        }

        if (isString() && other.isString()) {
            return new BindObject<>((String) value + (String) other.value);
        }

        throw new Diagnostics("Unsupported Type: " + value.getClass() + " + " + other.value.getClass());
    }


    public BindObject<?> minus(BindObject<?> other) {
        if (isInt() && other.isInt()) {
            return new BindObject<>((Integer) value - (Integer) other.value);
        }

        if (isInt() && other.isString()) {
            error("Operator '-' cannot be applied to 'int', 'java.lang.String'");
        }

        if (isString() && other.isInt()) {
            error("Operator '-' cannot be applied to 'java.lang.String', 'int'");
        }

        if (isString() && other.isString()) {
            error("Operator '-' cannot be applied to 'java.lang.String', 'java.lang.String'");
        }

        throw new Diagnostics("Unsupported Type: " + value.getClass() + " - " + other.value.getClass());
    }

    public BindObject<?> mul(BindObject<?> other) {
        if (isInt() && other.isInt()) {
            return new BindObject<>((Integer) value * (Integer) other.value);
        }

        if (isInt() && other.isString()) {
            error("Operator '*' cannot be applied to 'int', 'java.lang.String'");
        }

        if (isString() && other.isInt()) {
            error("Operator '*' cannot be applied to 'java.lang.String', 'int'");
        }

        if (isString() && other.isString()) {
            error("Operator '*' cannot be applied to 'java.lang.String', 'java.lang.String'");
        }

        throw new Diagnostics("Unsupported Type: " + value.getClass() + " - " + other.value.getClass());
    }


    public BindObject<?> slash(BindObject<?> other) {
        if (isInt() && other.isInt()) {
            return new BindObject<>((Integer) value / (Integer) other.value);
        }

        if (isInt() && other.isString()) {
            error("Operator '/' cannot be applied to 'int', 'java.lang.String'");
        }

        if (isString() && other.isInt()) {
            error("Operator '/' cannot be applied to 'java.lang.String', 'int'");
        }

        if (isString() && other.isString()) {
            error("Operator '/' cannot be applied to 'java.lang.String', 'java.lang.String'");
        }

        throw new Diagnostics("Unsupported Type: " + value.getClass() + " - " + other.value.getClass());
    }

    private boolean isInt() {
        return value instanceof Integer;
    }

    private boolean isString() {
        return value instanceof String;
    }

    private BindObject<?> error(String message) {
        throw new Diagnostics(message);
    }
}
