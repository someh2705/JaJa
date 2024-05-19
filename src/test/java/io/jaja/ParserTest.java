package io.jaja;

import io.jaja.expression.*;
import io.jaja.statement.BlockStatement;
import io.jaja.statement.IfThenStatement;
import io.jaja.statement.LocalVariableDeclarationStatement;
import io.jaja.statement.WhileStatement;
import io.jaja.token.Token;
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
            parser.parse(),
            AdditiveExpression.class,
                Token.class,
                Token.class,
                MultiplicativeExpression.class,
                    ParenthesesExpression.class,
                        AdditiveExpression.class,
                            Token.class,
                            Token.class,
                            Token.class,
                    Token.class,
                    Token.class
        );
    }

    @Test
    void assignmentExpressionTest() {
        Parser parser = new Parser("number = 20;");

        assertTreeOf(
            parser.parse(),
            AssignmentExpression.class,
                Token.class,
                Token.class,
                Token.class
        );
    }

    @Test
    void equalityExpressionTest() {
        Parser parser = new Parser("number == 20");
        Program program = parser.parse();

        assertTreeOf(
            program,
            EqualityExpression.class,
                PrimaryExpression.class,
                Token.class,
                Token.class
        );
    }

    @Test
    void declareVariableStatementTest() {
        Parser parser = new Parser("int number = 10;");

        assertTreeOf(
            parser.parse(),
            LocalVariableDeclarationStatement.class,
                Token.class,
                Token.class
        );
    }

    @Test
    void ifThenStatementTest() {
        Parser parser = new Parser("if (number == 10) int statement = 20;");

        assertTreeOf(
            parser.parse(),
            IfThenStatement.class,
                EqualityExpression.class,
                    PrimaryExpression.class,
                    Token.class,
                    PrimaryExpression.class,
                LocalVariableDeclarationStatement.class
        );
    }

    @Test
    void whileStatementTest() {
        StringBuilder builder = new StringBuilder();
        builder.append("while (number == 10) {");
        builder.append("    int statement = 20;");
        builder.append("}");

        Parser parser = new Parser(builder.toString());
        Program program = parser.parse();

        assertTreeOf(
            program,
            WhileStatement.class,
                EqualityExpression.class,
                    PrimaryExpression.class,
                    Token.class,
                    Token.class,
                BlockStatement.class,
                    LocalVariableDeclarationStatement.class,
                        Token.class,
                        Token.class,
                        PrimaryExpression.class
        );
    }

    private void assertTreeOf(Program program, Class<? extends AST>... expected) {
        List<String> names = new ArrayList<>();

        for (Class<? extends AST> clazz : expected) {
            names.add(clazz.getSimpleName());
        }

        assertTreeOf(program.getAST(), names);
    }

    private void assertTreeOf(AST ast, List<String> expected) {
        Printer.pretty(ast);
        StringReader sr = new StringReader(out.toString());

        try (BufferedReader br = new BufferedReader(sr)) {
            for (String name : expected) {
                assertContains(br.readLine(), name);
            }
        } catch (IOException e) { }
    }

    private void assertContains(String expected, String actual) {
        Assertions.assertTrue(expected.contains(actual), "expected: <" + expected + "> but was: <" + actual + ">");
    }
}
