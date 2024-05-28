package io.jaja;

import io.jaja.bind.BindObject;
import io.jaja.bind.FunctionObject;
import io.jaja.bind.Parameter;
import io.jaja.statement.MethodDeclarationStatement;
import io.jaja.token.Token;
import io.jaja.token.TokenKind;

import java.util.HashMap;
import java.util.List;

public class Environment {
    private HashMap<String, BindObject<?>> environment = new HashMap<>();
    private HashMap<String, FunctionObject> functions = new HashMap<>();
    private Environment parent;

    public Environment() {
        this(null);
    }

    public Environment(Environment parent) {
        this.parent = parent;
    }

    public void declare(Token identifier, BindObject<?> evaluate) {
        if (environment.containsKey(identifier.field)) {
            throw new Diagnostics("Duplicate declaration of " + identifier.field);
        }

        environment.put(identifier.field, new BindObject<>(identifier, evaluate.getValue()));
    }

    public void assignment(Token identifier, BindObject<?> evaluate) {
        access(identifier.field);
        environment.put(identifier.field, evaluate);
    }

    public MethodDeclarationStatement invoke(Token identifier, BindObject<?>... arguments) {
        try {
            BindObject<MethodDeclarationStatement> function = (BindObject<MethodDeclarationStatement>) access(identifier.field);
            MethodDeclarationStatement statement = function.getValue();
            if (statement.getParameters().size() != arguments.length) {
                throw new Diagnostics("Wrong number of arguments for " + identifier.field, true);
            }

            List<Parameter> parameters = statement.getParameters();

            for (int i = 0; i < arguments.length; i++) {
                BindObject<?> argument = arguments[i];
                Parameter parameter = parameters.get(i);
                check(parameter, argument);

                declare(parameter.getIdentifier(), argument);
            }

            return statement;
        } catch (Exception e) {
            if (e instanceof Diagnostics) {
                throw (Diagnostics) e;
            }
            throw new Diagnostics("Cannot resolve method '" + identifier.field + "'");
        }
    }


    public BindObject<?> call(Token identifier) {
        if (!isDeclare(identifier.field)) {
            throw new Diagnostics("Unknown field " + identifier.field);
        }

        return access(identifier.field);
    }

    private boolean isDeclare(String name) {
        BindObject<?> field = environment.get(name);
        if (field != null) return true;
        if (parent == null) return false;

        return parent.isDeclare(name);
    }

    private BindObject<?> access(String name) {
        BindObject<?> field = environment.get(name);

        if (field != null) return field;
        if (parent == null) return null;

        field = parent.access(name);
        if (field == null) {
            throw new Diagnostics("Unknown field " + name, true);
        }

        return field;
    }

    private void check(Parameter parameter, BindObject<?> argument) {
        Object value = argument.getValue();
        TokenKind kind = parameter.getType().kind;

        if (kind == TokenKind.BYTE) {
            if (value instanceof Byte) {
                return;
            }
        }

        if (kind == TokenKind.SHORT) {
            if (value instanceof Short) {
                return;
            }
        }

        if (kind == TokenKind.INT) {
            if (value instanceof Integer) {
                return;
            }
        }

        if (kind == TokenKind.LONG) {
            if (value instanceof Long) {
                return;
            }
        }

        if (kind == TokenKind.CHAR) {
            if (value instanceof Character) {
                return;
            }
        }

        if (kind == TokenKind.FLOAT) {
            if (value instanceof Float) {
                return;
            }
        }

        if (kind == TokenKind.DOUBLE) {
            if (value instanceof Double) {
                return;
            }
        }

        if (kind == TokenKind.BOOLEAN) {
            if (value instanceof Boolean) {
                return;
            }
        }

        if (kind == TokenKind.STRING) {
            if (value instanceof String) {
                return;
            }
        }

        throw new Diagnostics("mismatch parameter type. expect: " + kind + " actual: " + value.getClass(), true);
    }
}
