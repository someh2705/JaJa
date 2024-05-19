package io.jaja;

import io.jaja.expression.*;
import io.jaja.statement.*;
import io.jaja.token.TokenKind;

import java.util.ArrayList;
import java.util.Objects;

public class Runtime {

    private Environment environment = new Environment();

    public String evaluate(String input) {
        Parser parser = new Parser(input);
        return evaluate(parser.parse());
    }

    private String evaluate(Program program) {
        AST ast = program.getAST();
        return evaluate(ast);
    }

    private String evaluate(AST ast) {
        if (ast instanceof Statement) {
            return evaluate((Statement) ast);
        }

        return evaluate((Expression) ast);
    }

    private String evaluate(Expression expression) {
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

        throw new Diagnostics("Unexpected expression : " + expression);
    }

    private String evaluate(Statement statement) {
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

    private String evaluateIfThenStatement(IfThenStatement expression) {
        if (Boolean.parseBoolean(evaluate(expression.getCondition()))) {
            return evaluate(expression.getAst());
        }

        return "";
    }

    private String evaluateWhileStatement(WhileStatement statement) {
        while (Boolean.parseBoolean(evaluate(statement.getCondition()))) {
            evaluate(statement.getBody());
        }

        return "";
    }

    private String evaluateBlockStatement(BlockStatement statement) {
        ArrayList<AST> asts = statement.getAsts();
        for (AST ast: asts) {
            evaluate(ast);
        }

        return "";
    }

    private String evaluateEqualityExpression(EqualityExpression expression) {
        String left = evaluate(expression.getLeft());
        String right = evaluate(expression.getRight());

        return Objects.equals(left, right) ? "true" : "false";
    }

    private String evaluateAdditiveExpression(AdditiveExpression expression) {
        int left = Integer.parseInt(evaluate(expression.getLeft()));
        int right = Integer.parseInt(evaluate(expression.getRight()));
        return String.valueOf(left + right);
    }

    private String evaluateMultiplicativeExpression(MultiplicativeExpression expression) {
        int left = Integer.parseInt(evaluate(expression.getLeft()));
        int right = Integer.parseInt(evaluate(expression.getRight()));
        return String.valueOf(left * right);
    }

    private String evaluatePrimaryExpression(PrimaryExpression expression) {
        if (expression.getToken().kind == TokenKind.IDENTIFIER) {
            return String.valueOf(environment.call(expression.getToken()));
        }

        return expression.getToken().field;
    }

    private String evaluateParenthesesExpression(ParenthesesExpression expression) {
        return evaluate(expression.getExpression());
    }

    private String evaluateDeclareVariableStatement(LocalVariableDeclarationStatement expression) {
        int evaluate = Integer.parseInt(evaluate(expression.getExpression()));
        environment.declare(expression.getIdentifier(), evaluate);
        return expression.getIdentifier().field + "(" + evaluate + ")";
    }

    private String evaluateAssignmentExpression(AssignmentExpression expression) {
        int evaluate = Integer.parseInt(evaluate(expression.getExpression()));
        environment.assignment(expression.getIdentifier(), evaluate);
        return String.valueOf(evaluate);
    }
}
