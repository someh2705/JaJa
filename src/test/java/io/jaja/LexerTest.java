package io.jaja;

import io.jaja.token.TokenKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LexerTest {
    @Test
    void literalTest() {

        assertKind("0", TokenKind.INTLITERAL);
        assertBadToken("0123");
    }

    @Test
    void edgeTest() {
        String[] w = {"", " "};

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    for (int l = 0; l < 2; l++) {
                        assertKind(
                            "10" + w[i] + "+" + w[j] + "20" + w[k] + "*" + w[l] + "30",
                            TokenKind.INTLITERAL, TokenKind.PLUS, TokenKind.INTLITERAL, TokenKind.STAR, TokenKind.INTLITERAL
                        );
                    }
                }
            }
        }

        assertKind("(10 + 20)", TokenKind.LPAREN, TokenKind.INTLITERAL, TokenKind.PLUS, TokenKind.INTLITERAL, TokenKind.RPAREN);

        assertKind("int integer = 10;", TokenKind.INT, TokenKind.IDENTIFIER, TokenKind.EQ, TokenKind.INTLITERAL, TokenKind.SEMICOLON);
        assertKind("int integer= 10;", TokenKind.INT, TokenKind.IDENTIFIER, TokenKind.EQ, TokenKind.INTLITERAL, TokenKind.SEMICOLON);
        assertKind("int integer =10;", TokenKind.INT, TokenKind.IDENTIFIER, TokenKind.EQ, TokenKind.INTLITERAL, TokenKind.SEMICOLON);
        assertKind("int integer=10;", TokenKind.INT, TokenKind.IDENTIFIER, TokenKind.EQ, TokenKind.INTLITERAL, TokenKind.SEMICOLON);
    }

    @Test
    void operatorTest() {
        assertKind("()+ *", TokenKind.LPAREN, TokenKind.RPAREN, TokenKind.PLUS, TokenKind.STAR);
    }

    @Test
    void identifierTest() {
        assertKind("number", TokenKind.IDENTIFIER);
        assertKind("_number", TokenKind.IDENTIFIER);
        assertKind("number1_number2", TokenKind.IDENTIFIER);
        assertKind("integer", TokenKind.IDENTIFIER);
        assertBadToken("1number");
    }

    @Test
    void primitiveTokenTest() {
        assertKind("int", TokenKind.INT);
        assertKind(" int  ", TokenKind.INT);
    }

    private void assertKind(String string, TokenKind expected) {
        assertSame(expected, new Lexer(string).current().kind);
    }

    private void assertKind(String string, TokenKind... expected) {
        Lexer lexer = new Lexer(string);
        for (TokenKind kind : expected) {
            assertSame(kind, lexer.current().kind);
        }
    }

    private void assertBadToken(String string) {
        assertThrows(Diagnostics.class, () -> new Lexer(string).current());
    }
}
