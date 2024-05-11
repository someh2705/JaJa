package io.jaja;

import io.jaja.expression.AdditiveExpression;
import io.jaja.expression.Expression;
import io.jaja.expression.MultiplicativeExpression;
import io.jaja.expression.PrimaryExpression;

import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int position;

    public Parser(String input) {
        Lexer lexer = new Lexer(input);
        this.tokens = lexer.every();
    }

    public Expression parseExpression() {
        return parseAdditiveExpression();
    }

    /**
     * <additive expression> ::= <multiplicative expression> | <additive expression> + <multiplicative expression>
     */
    private Expression parseAdditiveExpression() {
        Expression expression = parseMultiplicativeExpression();

        while (match(TokenKind.PLUS)) {
            Token operator = previous();
            Expression right = parseMultiplicativeExpression();
            expression = new AdditiveExpression(expression, operator, right);
        }

        return expression;
    }

    /**
     * <multiplicative expression> ::= <primary> | <multiplicative expression> * <primary>
     */
    private Expression parseMultiplicativeExpression() {
        Expression expression = parsePrimaryExpression();

        while (match(TokenKind.STAR)) {
            Token operator = previous();
            Expression right = parsePrimaryExpression();
            expression = new MultiplicativeExpression(expression, operator, right);
        }

        return expression;
    }

    /**
     * <primary> ::= <literal>
     */
    private Expression parsePrimaryExpression() {
        return new PrimaryExpression(consume());
    }

    private Token peek() {
        return peek(0);
    }

    private Token peek(int offset) {
        return tokens.get(position + offset);
    }

    private Token previous() {
        return tokens.get(position - 1);
    }

    private Token consume() {
        return tokens.get(position++);
    }

    private boolean check(TokenKind kind) {
        return peek().kind == kind;
    }

    private boolean match(TokenKind kind) {
        if (check(kind)) {
            consume();
            return true;
        }

        return false;
    }
}
