package io.jaja;

import io.jaja.expression.AdditiveExpression;
import io.jaja.expression.MultiplicativeExpression;
import io.jaja.expression.ParenthesesExpression;
import io.jaja.expression.PrimaryExpression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    void baseParserTest() {
        Parser parser = new Parser("10 + (20 + 30) * 40");

        Token _10 = new Token(TokenKind.INTLITERAL, "10");
        Token plus = new Token(TokenKind.PLUS, "+");
        Token _20 = new Token(TokenKind.INTLITERAL, "20");
        Token star = new Token(TokenKind.STAR, "*");
        Token _30 = new Token(TokenKind.INTLITERAL, "30");
        Token _40 = new Token(TokenKind.INTLITERAL, "40");

        PrimaryExpression __10 = new PrimaryExpression(_10);
        PrimaryExpression __20 = new PrimaryExpression(_20);
        PrimaryExpression __30 = new PrimaryExpression(_30);
        PrimaryExpression __40 = new PrimaryExpression(_40);

        AdditiveExpression additive1 = new AdditiveExpression(__20, plus, __30);
        ParenthesesExpression parentheses = new ParenthesesExpression(additive1);
        MultiplicativeExpression multiplicative = new MultiplicativeExpression(parentheses, star, __40);
        AdditiveExpression additive2 = new AdditiveExpression(__10, plus, multiplicative);

        Assertions.assertEquals(additive2, parser.parseExpression());
    }
}
