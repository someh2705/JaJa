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
        String result1 = runtime.evaluate("if (number == 10) number = number + 1;");
        String result2 = runtime.evaluate("number");
        String result3 = runtime.evaluate("if (number == 20) number++;");
        String result4 = runtime.evaluate("number");

        assertEquals("number(10)", declare);
        assertEquals("", result1);
        assertEquals("11", result2);
        assertEquals("", result3);
        assertEquals("11", result4);

        String multipleLine = new StringBuilder()
            .append("if (number == 11) {")
            .append("    number = 20;")
            .append("}")
            .toString();

        String result5 = runtime.evaluate(multipleLine);
        String result6 = runtime.evaluate("number");

        assertEquals("", result5);
        assertEquals("20", result6);
    }

    @Test
    void preOrPostDecExpressionTest() {
        Runtime runtime = new Runtime();

        String declare = runtime.evaluate("int number = 10;");
        String result1 = runtime.evaluate("++number");
        String result2 = runtime.evaluate("number");
        String result3 = runtime.evaluate("number++");
        String result4 = runtime.evaluate("number");

        assertEquals("number(10)", declare);
        assertEquals("11", result1);
        assertEquals("11", result2);
        assertEquals("11", result3);
        assertEquals("12", result4);
    }

    @Test
    void methodTest() {
        Runtime runtime = new Runtime();

        runtime.evaluate("#compile(\"fibo.jaja\")");
        String result1 = runtime.evaluate("fibo(1)");
        String result2 = runtime.evaluate("fibo(2)");
        String result3 = runtime.evaluate("fibo(3)");

        assertEquals("1", result1);
        assertEquals("1", result2);
        assertEquals("2", result3);
    }
}
