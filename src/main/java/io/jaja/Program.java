package io.jaja;

import io.jaja.expression.Expression;
import io.jaja.statement.Statement;
import io.jaja.utils.IteratorUtils;

import java.util.Iterator;

public class Program implements AST {

    private Expression expression;
    private Statement statement;

    public Program(AST ast) {
        if (ast instanceof Expression) {
            expression = (Expression) ast;
        } else {
            statement = (Statement) ast;
        }
    }

    @Override
    public Iterator<AST> iterator() {
        if (expression != null) {
            return IteratorUtils.values(expression);
        }

        return IteratorUtils.values(statement);
    }

    @Override
    public AST lastChild() {
        if (expression != null) {
            return expression;
        }

        return statement;
    }

    public AST getAST() {
        if (expression != null) {
            return expression;
        }

        return statement;
    }
}
