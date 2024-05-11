package io.jaja;

import io.jaja.expression.AdditiveExpression;
import io.jaja.expression.MultiplicativeExpression;
import io.jaja.expression.PrimaryExpression;
import io.jaja.utils.Printer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrinterTest {
    private PrintStream oldOut;
    private ByteArrayOutputStream out;

    @BeforeEach
    public void setUp() {
        oldOut = System.out;
        out = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(out);
        System.setOut(newOut);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(oldOut);
        oldOut = null;
        out = null;
    }

    @Test
    void baseParserTest() throws IOException {
        AdditiveExpression additive = getExpressions();

        Printer.pretty(additive);

        StringReader sr = new StringReader(out.toString());
        try (BufferedReader br = new BufferedReader(sr)) {
            assertContains(br.readLine(), "AdditiveExpression");
            assertContains(br.readLine(), "PrimaryExpression");
            assertContains(br.readLine(), "PrimaryExpression");
            assertContains(br.readLine(), "MultiplicativeExpression");
            assertContains(br.readLine(), "PrimaryExpression");
            assertContains(br.readLine(), "PrimaryExpression");
            assertContains(br.readLine(), "PrimaryExpression");
        }
    }

    @Test
    void primaryExpressionTest() {
        Token token = new Token(TokenKind.INTLITERAL, "123");
        PrimaryExpression primary = new PrimaryExpression(token);
        String pretty = primary.toString();

        assertTrue(pretty.contains(token.toString()));
    }

    private void assertContains(String expected, String actual) {
        Assertions.assertTrue(expected.contains(actual));
    }

    private AdditiveExpression getExpressions() {
        Token _10 = new Token(TokenKind.INTLITERAL, "10");
        Token plus = new Token(TokenKind.PLUS, "+");
        Token _20 = new Token(TokenKind.INTLITERAL, "20");
        Token star = new Token(TokenKind.STAR, "*");
        Token _30 = new Token(TokenKind.INTLITERAL, "30");

        PrimaryExpression __10 = new PrimaryExpression(_10);
        PrimaryExpression __20 = new PrimaryExpression(_20);
        PrimaryExpression __30 = new PrimaryExpression(_30);

        MultiplicativeExpression multiplicative = new MultiplicativeExpression(__20, star, __30);
        return new AdditiveExpression(__10, plus, multiplicative);
    }
}
