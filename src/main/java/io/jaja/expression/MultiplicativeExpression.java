package io.jaja.expression;

import io.jaja.token.Token;
import io.jaja.utils.IteratorUtils;

import java.util.Iterator;
import java.util.Objects;

/**
 *
 */
public class MultiplicativeExpression implements Expression {
    private Expression left;
    private Token operator;
    private Expression right;

    public MultiplicativeExpression(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiplicativeExpression that = (MultiplicativeExpression) o;
        return Objects.equals(left, that.left) && Objects.equals(operator, that.operator) && Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, operator, right);
    }

    @Override
    public Iterator<Expression> iterator() {
        return IteratorUtils.values(left, new PrimaryExpression(operator), right);
    }

    @Override
    public Expression lastChild() {
        return right;
    }

    @Override
    public String toString() {
        return "MultiplicativeExpression";
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }
}