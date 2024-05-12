package io.jaja;

import io.jaja.expression.AdditiveExpression;
import io.jaja.expression.Expression;
import io.jaja.expression.MultiplicativeExpression;
import io.jaja.expression.PrimaryExpression;

public class Runtime {

    public String evaluate(String input) {
        Parser parser = new Parser(input);
        return evaluate(parser.parseExpression());
    }

    private String evaluate(Expression expression) {
        if (expression instanceof AdditiveExpression) {
            int left = Integer.parseInt(evaluate(((AdditiveExpression) expression).getLeft()));
            int right = Integer.parseInt(evaluate(((AdditiveExpression) expression).getRight()));
            return String.valueOf(left + right);
        }

        if (expression instanceof PrimaryExpression) {
            return ((PrimaryExpression) expression).getToken().field;
        }

        if (expression instanceof MultiplicativeExpression) {
            int left = Integer.parseInt(evaluate(((MultiplicativeExpression) expression).getLeft()));
            int right = Integer.parseInt(evaluate(((MultiplicativeExpression) expression).getRight()));
            return String.valueOf(left * right);
        }

        throw new IllegalStateException("Unexpected expression : " + expression);
    }
}
