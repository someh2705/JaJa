package io.jaja.expression;

import io.jaja.utils.IteratorUtils;

import java.util.Iterator;
import java.util.Objects;

public class ParenthesesExpression implements Expression {

    private Expression expression;

    public ParenthesesExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParenthesesExpression that = (ParenthesesExpression) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(expression);
    }

    @Override
    public Expression lastChild() {
        return expression;
    }

    @Override
    public Iterator<Expression> iterator() {
        return IteratorUtils.values(expression);
    }

    @Override
    public String toString() {
        return "ParenthesesExpression";
    }
}
