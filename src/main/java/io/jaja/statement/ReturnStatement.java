package io.jaja.statement;

import io.jaja.AST;
import io.jaja.expression.Expression;
import io.jaja.utils.IteratorUtils;

import java.util.Iterator;

public class ReturnStatement implements Statement {
    private Expression expression;

    public ReturnStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public AST lastChild() {
        return expression;
    }

    @Override
    public Iterator<AST> iterator() {
        return IteratorUtils.values(expression);
    }

    @Override
    public String toString() {
        return "ReturnStatement{" +
            "expression=" + expression +
            '}';
    }
}
