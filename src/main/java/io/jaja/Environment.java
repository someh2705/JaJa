package io.jaja;

import io.jaja.token.Token;

import java.util.HashMap;

public class Environment {
    private HashMap<String, Integer> environment = new HashMap<>();

    public void declare(Token identifier, int evaluate) {
        if (environment.containsKey(identifier.field)) {
            throw new IllegalStateException("Duplicate declaration of " + identifier.field);
        }

        environment.put(identifier.field, evaluate);
    }

    public void assignemnt(Token identifier, int evaluate) {
        if (!environment.containsKey(identifier.field)) {
            throw new IllegalStateException("Unknown field " + identifier.field);
        }

        environment.put(identifier.field, evaluate);
    }

    public int call(Token identifier) {
        if (!environment.containsKey(identifier.field)) {
            throw new IllegalStateException("Unknown field" + identifier.field);
        }

        return environment.get(identifier.field);
    }
}
