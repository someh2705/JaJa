package io.jaja;

import io.jaja.bind.BindObject;
import io.jaja.bind.ResultObject;
import io.jaja.bind.ReturnObject;
import io.jaja.bind.ShellResult;
import io.jaja.builtin.BuiltInMethod;
import io.jaja.expression.*;
import io.jaja.statement.*;
import io.jaja.token.Token;
import io.jaja.token.TokenKind;
import io.jaja.utils.Printer;

import java.util.ArrayList;
import java.util.Arrays;

public class Runtime {

    private boolean isShowAST = false;
    private Environment environment = new Environment();
    private final ResultObject nothing = new ResultObject();
    private final BindObject<String> emptyObject = new BindObject<>("");
    private final BindObject<Boolean> trueObject = new BindObject<>(true);
    private final BindObject<Boolean> falseObject = new BindObject<>(false);

    public String evaluate(String input) {
        Parser parser = new Parser(input);
        return evaluate(parser.parse()).message();
    }

    private ShellResult evaluate(Program program) {
        AST ast = program.getAST();
        if (isShowAST) {
            Printer.pretty(ast);
        }
        return evaluate(ast);
    }

    private ShellResult evaluate(AST ast) {
        if (ast instanceof Statement) {
            return evaluate((Statement) ast);
        }

        return evaluate((Expression) ast);
    }

    private BindObject<?> evaluate(Expression expression) {
        if (expression instanceof AdditiveExpression) {
            return evaluateAdditiveExpression((AdditiveExpression) expression);
        }

        if (expression instanceof MultiplicativeExpression) {
            return evaluateMultiplicativeExpression((MultiplicativeExpression) expression);
        }

        if (expression instanceof ParenthesesExpression) {
            return evaluateParenthesesExpression((ParenthesesExpression) expression);
        }

        if (expression instanceof PrimaryExpression) {
            return evaluatePrimaryExpression((PrimaryExpression) expression);
        }

        if (expression instanceof AssignmentExpression) {
            return evaluateAssignmentExpression((AssignmentExpression) expression);
        }

        if (expression instanceof EqualityExpression) {
            return evaluateEqualityExpression((EqualityExpression) expression);
        }

        if (expression instanceof MethodInvocationExpression) {
            return evaluateMethodInvocationExpression((MethodInvocationExpression) expression);
        }

        if (expression instanceof PostIncOrDecExpression) {
            return evaluatePostIncOrDecExpression((PostIncOrDecExpression) expression);
        }

        if (expression instanceof PreIncOrDecExpression) {
            return evaluatePreIncOrDecExpression((PreIncOrDecExpression) expression);
        }

        throw new Diagnostics("Unexpected expression : " + expression);
    }

    private ResultObject evaluate(Statement statement) {
        if (statement instanceof LocalVariableDeclarationStatement) {
            return evaluateDeclareVariableStatement((LocalVariableDeclarationStatement) statement);
        }

        if (statement instanceof IfThenStatement) {
            return evaluateIfThenStatement((IfThenStatement) statement);
        }

        if (statement instanceof WhileStatement) {
            return evaluateWhileStatement((WhileStatement) statement);
        }

        if (statement instanceof BlockStatement) {
            return evaluateBlockStatement((BlockStatement) statement);
        }

        if (statement instanceof ReturnStatement) {
            throw new ReturnObject(evaluateReturnStatement((ReturnStatement) statement));
        }

        if (statement instanceof MethodDeclarationStatement) {
            return evaluateMethodDeclarationStatement((MethodDeclarationStatement) statement);
        }

        throw new Diagnostics("Unexpected statement : " + statement);
    }

    private BindObject<?> evaluateMethodBody(Token returnType, BlockStatement body) {
        for (AST statement : body) {
            try {
                evaluate(statement);
            } catch (ReturnObject o) {
                BindObject<?> value = o.getValue();
                value.expect(returnType);
                return value;
            }
        }
        return emptyObject;
    }

    private BindObject<?> evaluateReturnStatement(ReturnStatement statement) {
        if (statement instanceof VoidReturnStatement) {
            return emptyObject;
        }

        return evaluate(((PrimaryReturnStatement) statement).getExpression());
    }

    private ResultObject evaluateIfThenStatement(IfThenStatement expression) {
        if ((Boolean) evaluate(expression.getCondition()).getValue()) {
            evaluate(expression.getAst());
        }

        return nothing;
    }

    private ResultObject evaluateWhileStatement(WhileStatement statement) {
        while ((Boolean) evaluate(statement.getCondition()).getValue()) {
            evaluate(statement.getBody());
        }

        return nothing;
    }

    private ResultObject evaluateBlockStatement(BlockStatement statement) {
        ArrayList<AST> asts = statement.getAsts();
        for (AST ast: asts) {
            evaluate(ast);
        }

        return nothing;
    }

    private ResultObject evaluateMethodDeclarationStatement(MethodDeclarationStatement method) {
        environment.declare(method.getMethodName(), new BindObject<>(method));
        return new ResultObject(method.getMethodName().field);
    }

    private BindObject<?> evaluateEqualityExpression(EqualityExpression expression) {
        BindObject<?> left = evaluate(expression.getLeft());
        BindObject<?> right = evaluate(expression.getRight());

        return left.getValue().equals(right.getValue()) ? trueObject : falseObject;
    }

    private BindObject<?> evaluateMethodInvocationExpression(MethodInvocationExpression expression) {

        BindObject<?>[] arguments = new BindObject[expression.getArguments().size()];
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = evaluate(expression.getArguments().get(i));
        }

        if (expression.isBuiltIn()) {
            return evaluateBuiltInMethodInvocation(expression, arguments);
        }

        Environment current = environment;
        environment = new Environment(environment);
        MethodDeclarationStatement method = environment.invoke(expression.getIdentifier(), arguments);
        BindObject<?> result = evaluateMethodBody(method.getReturnType(), method.getBody());
        environment = current;

        return result;
    }

    private BindObject<?> evaluateBuiltInMethodInvocation(MethodInvocationExpression expression, BindObject<?>[] arguments) {
        Token builtIn = expression.getIdentifier();

        if (builtIn.kind == TokenKind.COMPILE) {
            if (arguments.length != 1) {
                throw new Diagnostics("#compile needs 1 arguments, but :" + Arrays.toString(arguments));
            }
            Program program = BuiltInMethod.compileFile((String) arguments[0].getValue());
            evaluate(program);
        }

        if (builtIn.kind == TokenKind.PRINT) {
            if (arguments.length != 1) {
                throw new Diagnostics("#print needs 1 arguments, but :" + Arrays.toString(arguments));
            }
            BuiltInMethod.println(arguments[0]);
        }

        if (builtIn.kind == TokenKind.PARSE) {
            if (arguments.length != 0) {
                throw new Diagnostics("#print needs 0 arguments, but :" + Arrays.toString(arguments));
            }
            isShowAST = !isShowAST;
            System.out.println("[DEBUG] isShowAST: " + isShowAST);
        }

        return emptyObject;
    }

    private BindObject<?> evaluateAdditiveExpression(AdditiveExpression expression) {
        BindObject<?> left = evaluate(expression.getLeft());
        BindObject<?> right = evaluate(expression.getRight());

        if (expression.getOperator().kind == TokenKind.PLUS) {
            return left.plus(right);
        }

        if (expression.getOperator().kind == TokenKind.SUB) {
            return left.minus(right);
        }

        throw new Diagnostics("Unexpected Operator: " + expression.getOperator().kind);
    }

    private BindObject<?> evaluateMultiplicativeExpression(MultiplicativeExpression expression) {
        BindObject<?> left = evaluate(expression.getLeft());
        BindObject<?> right = evaluate(expression.getRight());

        if (expression.getOperator().kind == TokenKind.STAR) {
            return left.mul(right);
        }

        if (expression.getOperator().kind == TokenKind.SLASH) {
            return left.slash(right);
        }

        throw new Diagnostics("Unexpected Operator: " + expression.getOperator().kind);
    }

    private BindObject<?> evaluatePrimaryExpression(PrimaryExpression expression) {
        if (expression.getToken().kind == TokenKind.IDENTIFIER) {
            return environment.call(expression.getToken());
        }

        if (expression.getToken().kind == TokenKind.INTLITERAL) {
            return new BindObject<>(Integer.valueOf(expression.getToken().field));
        }

        return new BindObject<>(expression.getToken(), expression.getToken().field);
    }

    private BindObject<?> evaluateParenthesesExpression(ParenthesesExpression expression) {
        return evaluate(expression.getExpression());
    }

    private ResultObject evaluateDeclareVariableStatement(LocalVariableDeclarationStatement expression) {
        BindObject<?> evaluate = evaluate(expression.getExpression());
        environment.declare(expression.getIdentifier(), evaluate);

        return new ResultObject(expression.getIdentifier().field + "(" + evaluate.getValue() + ")");
    }

    private BindObject<?> evaluateAssignmentExpression(AssignmentExpression expression) {
        BindObject<?> evaluate = evaluate(expression.getExpression());
        environment.assignment(expression.getIdentifier(), evaluate);

        return evaluate;
    }

    private BindObject<?> evaluatePreIncOrDecExpression(PreIncOrDecExpression expression) {
        BindObject<?> current = evaluate(expression.getExpression());
        Token identifier = current.getIdentifier();

        if (expression.getOperator().kind == TokenKind.PLUSPLUS) {
            current = current.plus(new BindObject<>(1));
            environment.assignment(identifier, current);
            return current;
        }

        if (expression.getOperator().kind == TokenKind.SUBSUB) {
            current = current.plus(new BindObject<>(-1));
            environment.assignment(identifier, current);
            return current;
        }

        throw new Diagnostics("Unexpected Operator: " + expression.getOperator().kind);
    }

    private BindObject<?> evaluatePostIncOrDecExpression(PostIncOrDecExpression expression) {
        BindObject<?> current = evaluate(expression.getExpression());
        Token identifier = current.getIdentifier();

        if (expression.getOperator().kind == TokenKind.PLUSPLUS) {
            environment.assignment(identifier, current.plus(new BindObject<>(1)));
            return current;
        }

        if (expression.getOperator().kind == TokenKind.SUBSUB) {
            environment.assignment(identifier, current.plus(new BindObject<>(-1)));
            return current;
        }

        throw new Diagnostics("Unexpected Operator: " + expression.getOperator().kind);
    }
}
