package io.jaja.statement;

import io.jaja.AST;
import io.jaja.expression.Expression;
import io.jaja.token.Token;
import io.jaja.utils.IteratorUtils;

import java.util.Iterator;
import java.util.Objects;

public class LocalVariableDeclarationStatement implements Statement {
    private Token type;
    private Token identifier;
    private Expression expression;

    public LocalVariableDeclarationStatement(Token type, Token identifier, Expression expression) {
        this.type = type;
        this.identifier = identifier;
        this.expression = expression;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalVariableDeclarationStatement that = (LocalVariableDeclarationStatement) o;
        return Objects.equals(identifier, that.identifier) && Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, expression);
    }

    @Override
    public AST lastChild() {
        return expression;
    }

    @Override
    public Iterator<AST> iterator() {
        return IteratorUtils.values(type, identifier, expression);
    }

    public Token getType() {
        return type;
    }

    public Token getIdentifier() {
        return identifier;
    }

    public Expression getExpression() {
        return expression;
    }
}
