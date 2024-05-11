package io.jaja;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTest {

    @Test
    void baseTokenTest() {
        Lexer lexer = new Lexer("10 + 20 * 30");

        assertEquals(TokenKind.INTLITERAL, lexer.current());
        assertEquals(TokenKind.PLUS, lexer.current());
        assertEquals(TokenKind.INTLITERAL, lexer.current());
        assertEquals(TokenKind.STAR, lexer.current());
        assertEquals(TokenKind.INTLITERAL, lexer.current());
    }
}
