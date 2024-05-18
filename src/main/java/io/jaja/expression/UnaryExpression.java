package io.jaja.expression;

import io.jaja.AST;
import io.jaja.token.Token;
import io.jaja.utils.IteratorUtils;

import java.util.Iterator;

public class UnaryExpression implements Expression {
    private Token operator;
    private Expression expression;

    public UnaryExpression(Token operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }


    @Override
    public AST lastChild() {
        return expression;
    }

    @Override
    public Iterator<AST> iterator() {
        return IteratorUtils.values(operator, expression);
    }
}
