package io.jaja.statement;

import io.jaja.expression.Expression;
import io.jaja.expression.PrimaryExpression;
import io.jaja.token.Token;
import io.jaja.utils.IteratorUtils;

import java.util.Iterator;
import java.util.Objects;

public class DeclareVariableStatement implements Statement {
    private Token identifier;
    private Expression expression;

    public DeclareVariableStatement(Token identifier, Expression assignment) {
        this.identifier = identifier;
        this.expression = assignment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeclareVariableStatement that = (DeclareVariableStatement) o;
        return Objects.equals(identifier, that.identifier) && Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, expression);
    }

    @Override
    public Expression lastChild() {
        return expression;
    }

    @Override
    public Iterator<Expression> iterator() {
        return IteratorUtils.values(new PrimaryExpression(identifier), expression);
    }

    public Token getIdentifier() {
        return identifier;
    }

    public Expression getExpression() {
        return expression;
    }
}
