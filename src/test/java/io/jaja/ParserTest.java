package io.jaja;

import io.jaja.expression.*;
import io.jaja.statement.DeclareVariableStatement;
import io.jaja.statement.IfThenStatement;
import io.jaja.statement.Statement;
import io.jaja.utils.Printer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParserTest {
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
    void complexExpressionTest() {
        Parser parser = new Parser("10 + (20 + 30) * 40");

        assertTreeOf(
            parser.parseStatement(),
            AdditiveExpression.class,
                PrimaryExpression.class,
                PrimaryExpression.class,
                MultiplicativeExpression.class,
                    ParenthesesExpression.class,
                        AdditiveExpression.class,
                            PrimaryExpression.class,
                            PrimaryExpression.class,
                            PrimaryExpression.class,
                    PrimaryExpression.class,
                    PrimaryExpression.class
        );
    }

    @Test
    void assignmentExpressionTest() {
        Parser parser = new Parser("number = 20");

        assertTreeOf(
            parser.parseStatement(),
            AssignmentExpression.class,
                PrimaryExpression.class,
                PrimaryExpression.class,
                PrimaryExpression.class
        );
    }

    @Test
    void declareVariableStatementTest() {
        Parser parser = new Parser("int number = 10;");

        assertTreeOf(
            parser.parseStatement(),
            DeclareVariableStatement.class,
                PrimaryExpression.class,
                PrimaryExpression.class
        );
    }

    @Test
    void ifThenStatementTest() {
        Parser parser = new Parser("if (expression) int statement = 10;");

        assertTreeOf(
            parser.parseStatement(),
            IfThenStatement.class,
                PrimaryExpression.class,
                Statement.class
        );
    }

    private void assertTreeOf(Expression expression, Class<? extends Expression>... expected) {
        List<String> names = new ArrayList<>();

        for (Class<? extends Expression> clazz : expected) {
            names.add(clazz.getSimpleName());
        }

        assertTreeOf(expression, names);
    }

    private void assertTreeOf(Expression expression, List<String> expected) {
        Printer.pretty(expression);
        StringReader sr = new StringReader(out.toString());

        try (BufferedReader br = new BufferedReader(sr)) {
            for (String name : expected) {
                assertContains(br.readLine(), name);
            }
        } catch (IOException e) { }
    }

    private void assertContains(String expected, String actual) {
        Assertions.assertTrue(expected.contains(actual));
    }
}
