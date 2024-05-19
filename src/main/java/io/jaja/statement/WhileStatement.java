package io.jaja.statement;

import io.jaja.AST;
import io.jaja.expression.Expression;
import io.jaja.utils.IteratorUtils;

import java.util.Iterator;

public class WhileStatement implements Statement {

    private Expression condition;
    private AST body;

    public WhileStatement(Expression condition, AST body) {
        this.condition = condition;
        this.body = body;
    }


    @Override
    public AST lastChild() {
        return body;
    }

    @Override
    public Iterator<AST> iterator() {
        return IteratorUtils.values(condition, body);
    }

    public Expression getCondition() {
        return condition;
    }

    public AST getBody() {
        return body;
    }
}
