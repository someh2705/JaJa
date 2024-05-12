package io.jaja;

import io.jaja.expression.*;

public class Runtime {

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

        throw new IllegalStateException("Unexpected expression : " + expression);
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
        return expression.getToken().field;
    }

    private String evaluateParenthesesExpression(ParenthesesExpression expression) {
        return evaluate(expression.getExpression());
    }
}
