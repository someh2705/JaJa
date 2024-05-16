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

        String modify = runtime.evaluate("number = 20");
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
}
