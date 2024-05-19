package io.jaja.expression;

import io.jaja.AST;
import io.jaja.token.Token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MethodInvocationExpression implements Expression {
    private Token identifier;
    private List<Expression> arguments;

    public MethodInvocationExpression(Token identifier, ArrayList<Expression> arguments) {
        this.identifier = identifier;
        this.arguments = arguments;
    }

    @Override
    public AST lastChild() {
        return arguments.get(arguments.size() - 1);
    }

    @Override
    public Iterator<AST> iterator() {
        List<AST> asts = new ArrayList<>();
        asts.add(identifier);
        asts.addAll(arguments);
        return asts.iterator();
    }
}
