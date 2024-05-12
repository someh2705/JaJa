package io.jaja;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LexerTest {
    @Test
    void literalTest() {
        assertKind("10", TokenKind.INTLITERAL);
    }

    @Test
    void operatorTest() {
        assertKind("()+ *", TokenKind.LPAREN, TokenKind.RPAREN, TokenKind.PLUS, TokenKind.STAR);
    }

    @Test
    void identifierTest() {
        assertKind("number", TokenKind.IDENTIFIER);
        assertKind("_number", TokenKind.IDENTIFIER);
        assertKind("number1", TokenKind.IDENTIFIER);
        assertKind("number1_number2", TokenKind.IDENTIFIER);
        assertBadToken("1number");
    }

    @Test
    void primitiveTokenTest() {
        assertKind("int", TokenKind.INT);
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
        assertThrows(IllegalStateException.class, () -> new Lexer(string).current());
    }
}
