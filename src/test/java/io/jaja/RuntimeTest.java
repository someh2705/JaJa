package io.jaja;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RuntimeTest {

    @Test
    void evaluateTest() {
        Runtime runtime = new Runtime();
        String result = runtime.evaluate("10 + 20 * 30");

        assertEquals("610", result);
    }
}
