package io.jaja.expression;

import io.jaja.AST;
import io.jaja.token.Token;
import io.jaja.utils.IteratorUtils;

import java.util.Iterator;

public class EqualityExpression implements Expression {

    private Expression left;
    private Token operator;
    private Expression right;

    public EqualityExpression(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }


    @Override
    public Expression lastChild() {
        return right;
    }

    @Override
    public Iterator<AST> iterator() {
        return IteratorUtils.values(left, operator, right);
    }

    public Expression getLeft() {
        return left;
    }

    public Token getOperator() {
        return operator;
    }

    public Expression getRight() {
        return right;
    }
}
