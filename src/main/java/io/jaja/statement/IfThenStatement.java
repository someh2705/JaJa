package io.jaja.statement;

import io.jaja.expression.Expression;
import io.jaja.utils.IteratorUtils;

import java.util.Iterator;

public class IfThenStatement implements Statement {

    private Expression condition;
    private Expression statement;

    public IfThenStatement(Expression condition, Expression statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public Expression lastChild() {
        return statement;
    }

    @Override
    public Iterator<Expression> iterator() {
        return IteratorUtils.values(condition, statement);
    }

    public Expression getCondition() {
        return condition;
    }

    public Expression getStatement() {
        return statement;
    }
}
