package io.jaja.expression;

import io.jaja.AST;
import io.jaja.token.Token;
import io.jaja.utils.IteratorUtils;

import java.util.Iterator;
import java.util.Objects;

public class AssignmentExpression implements Expression {
    private Token identifier;
    private Token operator;
    private Expression expression;

    public AssignmentExpression(Token identifier, Token operator, Expression right) {
        this.identifier = identifier;
        this.operator = operator;
        this.expression = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignmentExpression that = (AssignmentExpression) o;
        return Objects.equals(identifier, that.identifier) && Objects.equals(operator, that.operator) && Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, operator, expression);
    }

    @Override
    public AST lastChild() {
        return expression;
    }

    @Override
    public Iterator<AST> iterator() {
        return IteratorUtils.values(identifier, operator, expression);
    }

    @Override
    public String toString() {
        return "AssignmentExpression";
    }

    public Token getIdentifier() {
        return identifier;
    }

    public Expression getExpression() {
        return expression;
    }
}
