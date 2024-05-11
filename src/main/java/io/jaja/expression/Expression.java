package io.jaja.expression;

public interface Expression extends Iterable<Expression> {

    public Expression lastChild();
}
