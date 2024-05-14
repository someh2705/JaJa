package io.jaja;

import io.jaja.expression.*;
import io.jaja.statement.DeclareVariableStatement;
import io.jaja.token.TokenKind;

public class Runtime {

    private Environment environment = new Environment();

    public String evaluate(String input) {
        Parser parser = new Parser(input);
        return evaluate(parser.parseExpression());
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

        if (expression instanceof DeclareVariableStatement) {
            return evaluateDeclareVariableStatement((DeclareVariableStatement) expression);
        }

        if (expression instanceof AssignmentExpression) {
            return evaluateAssignmentExpression((AssignmentExpression) expression);
        }

        throw new Diagnostics("Unexpected expression : " + expression);
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

    private String evaluateDeclareVariableStatement(DeclareVariableStatement expression) {
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
