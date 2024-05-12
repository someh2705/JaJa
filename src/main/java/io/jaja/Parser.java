package io.jaja;

import io.jaja.expression.*;

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
     * <primary> ::= <literal> | ( <expression> )
     */
    private Expression parsePrimaryExpression() {
        if (match(TokenKind.LPAREN)) {
            Expression expression = parseExpression();
            needs(TokenKind.RPAREN, ")가 필요합니다.");

            return new ParenthesesExpression(expression);
        }

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

    private void needs(TokenKind kind, String message) {
        if (!check(kind)) {
            error(message);
        }

        consume();
    }

    private void error(String message) {
        throw new RuntimeException(message);
    }
}
