package io.jaja.statement;

import io.jaja.AST;
import io.jaja.expression.Expression;
import io.jaja.utils.IteratorUtils;

import java.util.Iterator;

public class IfThenStatement implements Statement {

    private Expression condition;
    private AST ast;

    public IfThenStatement(Expression condition, AST ast) {
        this.condition = condition;
        this.ast = ast;
    }

    @Override
    public AST lastChild() {
        return ast;
    }

    @Override
    public Iterator<AST> iterator() {
        return IteratorUtils.values(condition, ast);
    }

    public Expression getCondition() {
        return condition;
    }

    public AST getAst() {
        return ast;
    }
}
