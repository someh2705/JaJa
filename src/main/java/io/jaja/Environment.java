package io.jaja;

import io.jaja.token.Token;

import java.util.HashMap;

public class Environment {
    private HashMap<String, Integer> environment = new HashMap<>();

    public void declare(Token identifier, int evaluate) {
        if (environment.containsKey(identifier.field)) {
            throw new Diagnostics("Duplicate declaration of " + identifier.field);
        }

        environment.put(identifier.field, evaluate);
    }

    public void assignment(Token identifier, int evaluate) {
        if (!environment.containsKey(identifier.field)) {
            throw new Diagnostics("Unknown field " + identifier.field);
        }

        environment.put(identifier.field, evaluate);
    }

    public int call(Token identifier) {
        if (!environment.containsKey(identifier.field)) {
            throw new Diagnostics("Unknown field" + identifier.field);
        }

        return environment.get(identifier.field);
    }
}
