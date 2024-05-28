package io.jaja.statement;

import io.jaja.AST;
import io.jaja.token.Token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MethodDeclarationStatement implements Statement {
    private Token returnType;
    private Token methodName;
    private List<Token> parameters;
    private Statement body;

    public MethodDeclarationStatement(Token returnType, Token methodName, List<Token> parameters, Statement body) {
        this.returnType = returnType;
        this.methodName = methodName;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public Iterator<AST> iterator() {
        List<AST> elements = new ArrayList<>();
        elements.add(returnType);
        elements.add(methodName);
        elements.addAll(parameters);
        elements.add(body);
        return elements.iterator();
    }

    @Override
    public AST lastChild() {
        return body;
    }
}
