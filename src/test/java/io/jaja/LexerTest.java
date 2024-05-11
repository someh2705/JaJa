package io.jaja;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTest {

    @Test
    void baseTokenTest() {
        Lexer lexer = new Lexer("10 + 20 * 30");

        assertEquals(new Token(TokenKind.INTLITERAL, "10"), lexer.current());
        assertEquals(new Token(TokenKind.PLUS, "+"), lexer.current());
        assertEquals(new Token(TokenKind.INTLITERAL, "20"), lexer.current());
        assertEquals(new Token(TokenKind.STAR, "*"), lexer.current());
        assertEquals(new Token(TokenKind.INTLITERAL, "30"), lexer.current());
    }
}
