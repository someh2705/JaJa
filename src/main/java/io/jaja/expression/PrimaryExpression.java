package io.jaja.expression;

import io.jaja.AST;
import io.jaja.token.Token;
import io.jaja.utils.IteratorUtils;

import java.util.Iterator;
import java.util.Objects;

/**
 *
 */
public class PrimaryExpression implements Expression {
    private Token token;

    public PrimaryExpression(Token token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrimaryExpression that = (PrimaryExpression) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(token);
    }

    @Override
    public Iterator<AST> iterator() {
        return IteratorUtils.values();
    }

    @Override
    public AST lastChild() {
        return null;
    }

    @Override
    public String toString() {
        return "PrimaryExpression(Token: " + token + ")";
    }

    public Token getToken() {
        return token;
    }
}
