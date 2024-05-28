package io.jaja;

import io.jaja.expression.*;
import io.jaja.statement.*;
import io.jaja.token.Token;
import io.jaja.token.TokenKind;
import io.jaja.utils.Printer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void methodInvocationTest() {
        Parser parser = new Parser("println(\"Hello World\")");
        Program program = parser.parse();

        assertTreeOf(
            program,
            MethodInvocationExpression.class,
                Token.class,
                PrimaryExpression.class
        );
    }

    @Test
    void methodDeclareTest() {
        TokenKind[] primitiveTypes = TokenKind.getPrimitiveTokens();

        for (TokenKind returnType : primitiveTypes) {
            for (TokenKind param1 : primitiveTypes) {
                for (TokenKind param2 : primitiveTypes) {
                    Parser parser = new Parser(returnType.getName() + " add(" + param1.getName() + " a, " + param2.getName() + " b) { return a + b; }");
                    Program program = parser.parse();

                    assertTreeOf(
                        program,
                        MethodDeclarationStatement.class,
                        Token.class, // int
                        Token.class, // add
                        Token.class, // int
                        Token.class, // a
                        Token.class, // int
                        Token.class, // b
                        BlockStatement.class,
                            ReturnStatement.class,
                                AdditiveExpression.class,
                                    PrimaryExpression.class,
                                    Token.class,
                                    PrimaryExpression.class
                    );
                }
            }
        }
    }

    @Test
    void methodReturnTest() {
        TokenKind[] primitiveTypes = TokenKind.getPrimitiveTokens();

        for (TokenKind returnType: primitiveTypes) {
            Parser parser = new Parser(returnType.getName() + " add() { return; }");
            assertThrows(Diagnostics.class, parser::parse);
        }

        Parser parser = new Parser("void add() { int number = 10;\n return number; }");
        assertThrows(Diagnostics.class, parser::parse);
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
