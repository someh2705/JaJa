package io.jaja;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RuntimeTest {

    @Test
    void evaluateTest() {
        Runtime runtime = new Runtime();
        String result = runtime.evaluate("10 + (20 + 30) * 40");

        assertEquals("2010", result);
    }

    @Test
    void declareVariableTest() {
        Runtime runtime = new Runtime();

        String declare = runtime.evaluate("int number = 10;");
        assertEquals("number(10)", declare);

        String ask = runtime.evaluate("number");
        assertEquals("10", ask);

        String modify = runtime.evaluate("number = 20;");
        assertEquals("20", modify);
    }

    @Test
    void equalityExpressionTest() {
        Runtime runtime = new Runtime();

        String declare = runtime.evaluate("int number = 10;");
        assertEquals("number(10)", declare);

        String equality = runtime.evaluate("number == 10");
        assertEquals("true", equality);
    }

    @Test
    void ifThenStatementTest() {
        Runtime runtime = new Runtime();

        String declare = runtime.evaluate("int number = 10;");
        String result1 = runtime.evaluate("if (number == 10) number");
        String result2 = runtime.evaluate("if (number == 20) number");

        assertEquals("number(10)", declare);
        assertEquals("10", result1);
        assertEquals("", result2);

        String multipleLine = new StringBuilder()
            .append("if (number == 10) {")
            .append("    number = 20;")
            .append("}")
            .toString();

        String result3 = runtime.evaluate(multipleLine);
        String result4 = runtime.evaluate("number");

        assertEquals("", result3);
        assertEquals("20", result4);
    }
}
