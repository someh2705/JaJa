package io.jaja;

import io.jaja.expression.AdditiveExpression;
import io.jaja.expression.MultiplicativeExpression;
import io.jaja.expression.PrimaryExpression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    void baseParserTest() {
        Parser parser = new Parser("10 + 20 * 30");

        Token _10 = new Token(TokenKind.INTLITERAL, "10");
        Token plus = new Token(TokenKind.PLUS, "+");
        Token _20 = new Token(TokenKind.INTLITERAL, "20");
        Token star = new Token(TokenKind.STAR, "*");
        Token _30 = new Token(TokenKind.INTLITERAL, "30");

        PrimaryExpression __10 = new PrimaryExpression(_10);
        PrimaryExpression __20 = new PrimaryExpression(_20);
        PrimaryExpression __30 = new PrimaryExpression(_30);

        MultiplicativeExpression multiplicative = new MultiplicativeExpression(__20, star, __30);
        AdditiveExpression additive = new AdditiveExpression(__10, plus, multiplicative);

        Assertions.assertEquals(additive, parser.parseExpression());
    }
}
