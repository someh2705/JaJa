package io.jaja;

import io.jaja.bind.BindObject;
import io.jaja.expression.MethodInvocationExpression;
import io.jaja.token.Token;

import java.util.HashMap;

public class Environment {
    private HashMap<String, BindObject<?>> environment = new HashMap<>();
    private Environment parent;

    public Environment() {
        this(null);
    }

    public Environment(Environment parent) {
        this.parent = parent;
    }

    public void declare(Token identifier, BindObject<?> evaluate) {

        if (environment.containsKey(identifier.field)) {
            throw new Diagnostics("Duplicate declaration of " + identifier.field);
        }

        environment.put(identifier.field, evaluate);
    }

    public void assignment(Token identifier, BindObject<?> evaluate) {
        access(identifier.field);
        environment.put(identifier.field, evaluate);
    }

    public BindObject<?> call(MethodInvocationExpression expression) {
        Token identifier = expression.getIdentifier();

        return call(identifier);
    }

    public BindObject<?> call(Token identifier) {
        if (!isDeclare(identifier.field)) {
            throw new Diagnostics("Unknown field " + identifier.field);
        }

        return environment.get(identifier.field);
    }

    private boolean isDeclare(String name) {
        BindObject<?> field = environment.get(name);
        if (field != null) return true;

        field = parent.environment.get(name);
        return field != null;
    }

    private BindObject<?> access(String name) {
        BindObject<?> field = environment.get(name);

        if (field != null) return field;

        field = parent.environment.get(name);
        if (field == null) {
            throw new Diagnostics("Unknown field " + name);
        }

        return field;
    }
}
