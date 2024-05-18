package io.jaja.token;

import io.jaja.AST;
import io.jaja.utils.IteratorUtils;

import java.util.Iterator;
import java.util.Objects;

public class Token implements AST {
    public final TokenKind kind;
    public final String field;

    public Token(TokenKind kind, String field) {
        this.kind = kind;
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return kind == token.kind && Objects.equals(field, token.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, field);
    }

    @Override
    public String toString() {
        String name = field == null ? "EOF" : field.equals("\n") ? "\\n" : field;

        return "Token{" +
            "kind=" + kind +
            ", field='" + name + '\'' +
            '}';
    }

    @Override
    public AST lastChild() {
        return this;
    }

    @Override
    public Iterator<AST> iterator() {
        return IteratorUtils.values();
    }
}
