package io.jaja.bind;

import io.jaja.Diagnostics;
import io.jaja.token.Token;
import io.jaja.token.TokenKind;

public class BindObject<T> implements ShellResult {
    private Token identifier;
    private T value;

    public BindObject(Token identifier, T value) {
        this.identifier = identifier;
        this.value = value;
    }

    public BindObject(T value) {
        this(null, value);
    }

    public Token getIdentifier() {
        return identifier;
    }

    public T getValue() {
        return value;
    }

    public BindObject<?> plus(BindObject<?> other) {
        if (isInt() && other.isInt()) {
            return new BindObject<>(identifier, (Integer) value + (Integer) other.value);
        }

        if (isInt() && other.isString()) {
            return new BindObject<>(identifier, (int) value + (String) other.value);
        }

        if (isString() && other.isInt()) {
            return new BindObject<>(identifier, (String) value + (int) other.value);
        }

        if (isString() && other.isString()) {
            return new BindObject<>(identifier, (String) value + (String) other.value);
        }

        throw new Diagnostics("Unsupported Type: " + value.getClass() + " + " + other.value.getClass());
    }


    public BindObject<?> minus(BindObject<?> other) {
        if (isInt() && other.isInt()) {
            return new BindObject<>(identifier, (Integer) value - (Integer) other.value);
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
            return new BindObject<>(identifier, (Integer) value * (Integer) other.value);
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
            return new BindObject<>(identifier, (Integer) value / (Integer) other.value);
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

    public void expect(Token returnType) {
        switch (returnType.kind) {
            case BYTE:
                assertIsValidType(value instanceof Byte, returnType.kind);
                break;
            case SHORT:
                assertIsValidType(value instanceof Short, returnType.kind);
                break;
            case INT:
                assertIsValidType(value instanceof Integer, returnType.kind);
                break;
            case LONG:
                assertIsValidType(value instanceof Long, returnType.kind);
                break;
            case CHAR:
                assertIsValidType(value instanceof Character, returnType.kind);
                break;
            case FLOAT:
                assertIsValidType(value instanceof Float, returnType.kind);
                break;
            case DOUBLE:
                assertIsValidType(value instanceof Double, returnType.kind);
                break;
            case BOOLEAN:
                assertIsValidType(value instanceof Boolean, returnType.kind);
                break;
            default:
                throw new Diagnostics("Unexpected Type: " + returnType.kind);
        }
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

    @Override
    public String message() {
        if (value instanceof String) {
            return "\"" + value + "\"";
        }
        return String.valueOf(value);
    }

    private void assertIsValidType(boolean isValid, TokenKind returnType) {
        if (!isValid) {
            throw new Diagnostics("Require Type: " + returnType + " Provided: " + value);
        }
    }
}
