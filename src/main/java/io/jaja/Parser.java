package io.jaja;

import io.jaja.expression.*;
import io.jaja.statement.*;
import io.jaja.token.Token;
import io.jaja.token.TokenKind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int position;

    public Parser(String input) {
        Lexer lexer = new Lexer(input);
        this.tokens = lexer.every();
    }

    public Program parse() {
        return new Program(parseImpl());
    }

    private AST parseImpl() {
        return parseImpl(position);
    }

    private AST parseImpl(int current) {
        try {
            Statement statement = parseStatement();
            return statement;
        } catch (Diagnostics _error) {
            position = current;
            Expression expression = parseExpression();
            return expression;
        }
    }

    private Statement parseStatement() {
        if (match(TokenKind.LBRACE)) return parseBlockStatement();
        if (match(TokenKind.IF)) return parseIfThenStatement();
        if (match(TokenKind.WHILE)) return parseWhileStatement();

        return parseLocalVariableDeclarationStatement();
    }

    private Statement parseBlockStatement() {
        ArrayList<AST> asts = new ArrayList<>();

        do {
            asts.add(parseImpl());
        } while (!match(TokenKind.RBRACE));

        return new BlockStatement(asts);
    }

    private Statement parseIfThenStatement() {
        needs(TokenKind.LPAREN);
        Expression expression = parseExpression();
        needs(TokenKind.RPAREN);
        AST ast = parseImpl();

        return new IfThenStatement(expression, ast);
    }

    private Statement parseWhileStatement() {
        needs(TokenKind.LPAREN);
        Expression expression = parseExpression();
        needs(TokenKind.RPAREN);
        AST ast = parseImpl();
        return new WhileStatement(expression, ast);
    }

    private Expression parseExpression() {
        return assignemntExpression();
    }

    private Statement parseLocalVariableDeclarationStatement() {
        Token type = parseUnannType();

        if (type == null) {
            error("Unexpected Token: " + consume() + " is not type");
        }

        needs(TokenKind.IDENTIFIER);
        Token identifier = previous();
        needs(TokenKind.EQ);
        Expression expression = parseExpression();
        needs(TokenKind.SEMICOLON);
        return new LocalVariableDeclarationStatement(type, identifier, expression);
    }

    private Token parseUnannType() {
        Token primitive = parsePrimitiveType();
        if (primitive != null) return primitive;

        Token reference = parseReferenceType();
        if (reference != null) return reference;

        return null;
    }

    private Token parseReferenceType() {
        if (match(TokenKind.STRING)) return previous();

        return null;
    }

    private Token parsePrimitiveType() {
        Token numeric = parseNumericType();
        if (numeric != null) return numeric;

        if (match(TokenKind.BOOLEAN)) return previous();

        return null;
    }

    private Token parseNumericType() {
        if (match(TokenKind.BYTE, TokenKind.SHORT, TokenKind.INT, TokenKind.LONG, TokenKind.CHAR, TokenKind.FLOAT, TokenKind.DOUBLE)) {
            return previous();
        }

        return null;
    }

    private Expression assignemntExpression() {
        if (peek().kind == TokenKind.IDENTIFIER) {
            if (peek(1).kind == TokenKind.EQ) {
                Token identifier = consume();
                Token operator = consume();
                Expression right = assignemntExpression();
                needs(TokenKind.SEMICOLON);
                return new AssignmentExpression(identifier, operator, right);
            }
        }

        return parseEqualityExpression();
    }

    private Expression parseEqualityExpression() {
        Expression left = parseAdditiveExpression();
        while (match(TokenKind.EQEQ, TokenKind.BANGEQ)) {
            Token operator = previous();
            Expression right = parseAdditiveExpression();
            left = new EqualityExpression(left, operator, right);
        }

        return left;
    }

    private Expression parseAdditiveExpression() {
        Expression expression = parseMultiplicativeExpression();

        while (match(TokenKind.PLUS, TokenKind.SUB)) {
            Token operator = previous();
            Expression right = parseMultiplicativeExpression();
            expression = new AdditiveExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression parseMultiplicativeExpression() {
        Expression expression = parsePrimaryExpression();

        while (match(TokenKind.STAR, TokenKind.SLASH, TokenKind.PERCENT)) {
            Token operator = previous();
            Expression right = parsePrimaryExpression();
            expression = new MultiplicativeExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression parseUnaryExpression() {
        if (match(TokenKind.PLUSPLUS, TokenKind.SUBSUB)) {
            return parsePreIncOrDecExpression();
        }

        if (match(TokenKind.PLUS, TokenKind.SUB)) {
            Token operator = previous();
            return new UnaryExpression(operator, parseUnaryExpression());
        }

        return parseUnaryExpressionNotPlusMinusExpression();
    }

    private Expression parsePreIncOrDecExpression() {
        needs(TokenKind.PLUSPLUS, TokenKind.SUBSUB);
        Token operator = previous();
        Expression expression = parseUnaryExpression();

        return new PreIncOrDecExpression(operator, expression);
    }

    private Expression parseUnaryExpressionNotPlusMinusExpression() {
        if (match(TokenKind.BANG)) {
            Token operator = previous();
            Expression expression = parseUnaryExpression();
            return new UnaryExpression(operator, expression);
        }

        Expression expression = parsePrimaryExpression();

        if (match(TokenKind.PLUSPLUS, TokenKind.SUBSUB)) {
            Token operator = previous();
            return new PostIncOrDecExpression(operator, expression);
        }

        return expression;
    }

    private Expression parsePrimaryExpression() {
        if (match(TokenKind.LPAREN)) {
            Expression expression = parseExpression();
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

    private boolean match(TokenKind... kinds) {
        for (TokenKind kind: kinds) {
            if (check(kind)) {
                consume();
                return true;
            }
        }

        return false;
    }

    private void needs(TokenKind... kinds) {
        needs("Unexpected Token: expected " + Arrays.toString(kinds) + ", actual " + peek(), kinds);
    }

    private void needs(String message, TokenKind... kinds) {
        for (TokenKind kind: kinds) {
            if (check(kind)) {
                consume();
                return;
            }
        }

        error(message);
    }

    private void error(String message) {
        throw new Diagnostics(message);
    }
}
