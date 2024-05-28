package io.jaja.statement;

import io.jaja.AST;
import io.jaja.bind.Parameter;
import io.jaja.token.Token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MethodDeclarationStatement implements Statement {
    private Token returnType;
    private Token methodName;
    private List<Parameter> parameters;
    private BlockStatement body;

    public MethodDeclarationStatement(Token returnType, Token methodName, List<Parameter> parameters, BlockStatement body) {
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
        for (Parameter p : parameters) {
            elements.add(p.getType());
            elements.add(p.getIdentifier());
        }
        elements.add(body);
        return elements.iterator();
    }

    @Override
    public AST lastChild() {
        return body;
    }

    public Token getReturnType() {
        return returnType;
    }

    public Token getMethodName() {
        return methodName;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public BlockStatement getBody() {
        return body;
    }
}
