package io.jaja.bind;

import io.jaja.token.Token;

public class Parameter {
    private Token type;
    private Token identifier;

    public Parameter(Token type, Token identifier) {
        this.type = type;
        this.identifier = identifier;
    }

    public Token getType() {
        return type;
    }

    public Token getIdentifier() {
        return identifier;
    }
}
