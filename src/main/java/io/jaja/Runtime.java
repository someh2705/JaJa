package io.jaja;

import io.jaja.bind.BindObject;
import io.jaja.expression.*;
import io.jaja.statement.*;
import io.jaja.token.TokenKind;

import java.util.ArrayList;

public class Runtime {

    private Environment environment = new Environment();
    private final BindObject<String> emptyObject = new BindObject<>("");
    private final BindObject<Boolean> trueObject = new BindObject<>(true);
    private final BindObject<Boolean> falseObject = new BindObject<>(false);

    public String evaluate(String input) {
        Parser parser = new Parser(input);
        return String.valueOf(evaluate(parser.parse()).getValue());
    }

    private BindObject<?> evaluate(Program program) {
        AST ast = program.getAST();
        return evaluate(ast);
    }

    private BindObject<?> evaluate(AST ast) {
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

        throw new Diagnostics("Unexpected expression : " + expression);
    }

    private BindObject<?> evaluate(Statement statement) {
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

        throw new Diagnostics("Unexpected statement : " + statement);
    }

    private BindObject<?> evaluateIfThenStatement(IfThenStatement expression) {
        if ((Boolean) evaluate(expression.getCondition()).getValue()) {
            return evaluate(expression.getAst());
        }

        return emptyObject;
    }

    private BindObject<?> evaluateWhileStatement(WhileStatement statement) {
        while ((Boolean) evaluate(statement.getCondition()).getValue()) {
            evaluate(statement.getBody());
        }

        return emptyObject;
    }

    private BindObject<?> evaluateBlockStatement(BlockStatement statement) {
        ArrayList<AST> asts = statement.getAsts();
        for (AST ast: asts) {
            evaluate(ast);
        }

        return emptyObject;
    }

    private BindObject<?> evaluateEqualityExpression(EqualityExpression expression) {
        BindObject<?> left = evaluate(expression.getLeft());
        BindObject<?> right = evaluate(expression.getRight());

        return left.getValue().equals(right.getValue()) ? trueObject : falseObject;
    }

    private BindObject<?> evaluateMethodInvocationExpression(MethodInvocationExpression expression) {
        Environment local = new Environment(environment);
        return local.call(expression);
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

        throw new Diagnostics("Unexpected Operator: " + expression.getOperator().kind.getName());
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

        throw new Diagnostics("Unexpected Operator: " + expression.getOperator().kind.getName());
    }

    private BindObject<?> evaluatePrimaryExpression(PrimaryExpression expression) {
        if (expression.getToken().kind == TokenKind.IDENTIFIER) {
            return environment.call(expression.getToken());
        }

        if (expression.getToken().kind == TokenKind.INTLITERAL) {
            return new BindObject<>(Integer.valueOf(expression.getToken().field));
        }

        return new BindObject<>(expression.getToken().field);
    }

    private BindObject<?> evaluateParenthesesExpression(ParenthesesExpression expression) {
        return evaluate(expression.getExpression());
    }

    private BindObject<?> evaluateDeclareVariableStatement(LocalVariableDeclarationStatement expression) {
        BindObject<?> evaluate = evaluate(expression.getExpression());
        environment.declare(expression.getIdentifier(), evaluate);

        return new BindObject<>(expression.getIdentifier().field + "(" + evaluate.getValue() + ")");
    }

    private BindObject<?> evaluateAssignmentExpression(AssignmentExpression expression) {
        BindObject<?> evaluate = evaluate(expression.getExpression());
        environment.assignment(expression.getIdentifier(), evaluate);

        return evaluate;
    }
}
