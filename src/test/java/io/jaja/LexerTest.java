package io.jaja;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class LexerTest {
    @Test
    void literalTest() {
        assertKind("10", TokenKind.INTLITERAL);
    }

    @Test
    void operatorTest() {
        assertKind("()+ *", TokenKind.LPAREN, TokenKind.RPAREN, TokenKind.PLUS, TokenKind.STAR);
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
}
