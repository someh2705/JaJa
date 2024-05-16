package io.jaja;

import io.jaja.expression.*;
import io.jaja.statement.DeclareVariableStatement;
import io.jaja.statement.IfThenStatement;
import io.jaja.statement.Statement;
import io.jaja.token.Token;
import io.jaja.token.TokenKind;

import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int position;

    public Parser(String input) {
        Lexer lexer = new Lexer(input);
        this.tokens = lexer.every();
    }

    public Expression parseStatement() {
        if (match(TokenKind.IF)) return parseIfThenStatement();

        return parseExpression();
    }

    public Expression parseIfThenStatement() {
        needs(TokenKind.LPAREN);
        Expression expression = parseExpression();
        needs(TokenKind.RPAREN);
        Expression statement = parseStatement();

        return new IfThenStatement(expression, statement);
    }

    public Expression parseExpression() {
        return declareVariableStatement();
    }

    private Expression declareVariableStatement() {
        if (match(TokenKind.INT)) {
            Token identifier = consume();
            needs(TokenKind.EQ);
            Expression expression = parseStatement();
            needs(TokenKind.SEMICOLON);
            return new DeclareVariableStatement(identifier, expression);
        }

        return assignemntExpression();
    }

    /**
     * <assignment> ::= <additive expression> | <identifier> <assignment operator> <assignment>
     */
    private Expression assignemntExpression() {
        if (peek().kind == TokenKind.IDENTIFIER) {
            if (peek(1).kind == TokenKind.EQ) {
                Token identifier = consume();
                Token operator = consume();
                Expression right = assignemntExpression();
                return new AssignmentExpression(identifier, operator, right);
            }
        }

        return parseEqualityExpression();
    }

    private Expression parseEqualityExpression() {
        Expression left = parseAdditiveExpression();
        while (match(TokenKind.EQEQ)) {
            Token operator = previous();
            Expression right = parseAdditiveExpression();
            left = new EqualityExpression(left, operator, right);
        }

        return left;
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
            Expression expression = parseStatement();
            needs(TokenKind.RPAREN);

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

    private void needs(TokenKind kind) {
        needs(kind, "Unexpected Token: expected " + kind + ", actual " + peek());
    }

    private void needs(TokenKind kind, String message) {
        if (!check(kind)) {
            error(message);
        }

        consume();
    }

    private void error(String message) {
        throw new Diagnostics(message);
    }
}
