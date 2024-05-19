package io.jaja;

import io.jaja.token.Token;
import io.jaja.token.TokenKind;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.jaja.token.TokenKind.*;

public class StackLexerTest {

    private final String stack = new StringBuilder()
        .append("public class Stack {\n")
        .append("    private int[] elements;\n")
        .append("    private int size;\n")
        .append("    private static final int DEFAULT_CAPACITY = 16;\n")
        .append("    private static int instanceOfStack;\n")
        .append("\n")
        .append("    public Stack() {\n")
        .append("        this(DEFAULT_CAPACITY);\n")
        .append("    }\n")
        .append("\n")
        .append("    public Stack(int size) {\n")
        .append("        this.elements = new int[size];\n")
        .append("        instanceOfStack++;\n")
        .append("    }\n")
        .append("\n")
        .append("    public void push(int value) {\n")
        .append("        if (size >= elements.length) {\n")
        .append("            int[] next = new int[elements.length * 2];\n")
        .append("            for (int i = 0; i < elements.length; i++) {\n")
        .append("                next[i] = elements[i];\n")
        .append("            }\n")
        .append("            elements = next;\n")
        .append("        }\n")
        .append("\n")
        .append("        elements[size++] = value;\n")
        .append("    }\n")
        .append("\n")
        .append("    public int pop() {\n")
        .append("        return elements[--size];\n")
        .append("    }\n")
        .append("\n")
        .append("    public boolean isEmpty() {\n")
        .append("        return size == 0;\n")
        .append("    }\n")
        .append("\n")
        .append("    public int getSize() {\n")
        .append("        return size;\n")
        .append("    }\n")
        .append("\n")
        .append("    public static int getInstanceOfStack() {\n")
        .append("        return instanceOfStack;\n")
        .append("    }\n")
        .append("}\n")
        .toString();

    private final ArrayList<TokenKind> kinds = new ArrayList<>();

    private void addAll(TokenKind... kinds) {
        this.kinds.addAll(Arrays.asList(kinds));
    }

    @BeforeEach
    public void setup() {
        addAll(PUBLIC, CLASS, IDENTIFIER, LBRACE);

        // elements
        addAll(PRIVATE, INT, LBRACKET, RBRACKET, IDENTIFIER, SEMICOLON);
        // size
        addAll(PRIVATE, INT, IDENTIFIER, SEMICOLON);
        // DEFAULT_CAPACITY
        addAll(PRIVATE, STATIC, FINAL, INT, IDENTIFIER, EQ, INTLITERAL, SEMICOLON);
        // instanceOfStack
        addAll(PRIVATE, STATIC, INT, IDENTIFIER, SEMICOLON);
        addAll();

        // constructor
        addAll(PUBLIC, IDENTIFIER, LPAREN, RPAREN, LBRACE);
        addAll(THIS, LPAREN, IDENTIFIER, RPAREN, SEMICOLON);
        addAll(RBRACE);
        addAll();

        // constructor(int)
        addAll(PUBLIC, IDENTIFIER, LPAREN, INT, IDENTIFIER, RPAREN, LBRACE);
        addAll(THIS, DOT, IDENTIFIER, EQ, NEW, INT, LBRACKET, IDENTIFIER, RBRACKET, SEMICOLON);
        addAll(IDENTIFIER, PLUSPLUS, SEMICOLON);
        addAll(RBRACE);
        addAll();

        // push
        addAll(PUBLIC, VOID, IDENTIFIER, LPAREN, INT, IDENTIFIER, RPAREN, LBRACE);
        addAll(IF, LPAREN, IDENTIFIER, GTEQ, IDENTIFIER, DOT, IDENTIFIER, RPAREN, LBRACE);
        addAll(INT, LBRACKET, RBRACKET, IDENTIFIER, EQ, NEW, INT, LBRACKET, IDENTIFIER, DOT, IDENTIFIER, STAR, INTLITERAL, RBRACKET, SEMICOLON);
        addAll(FOR, LPAREN, INT, IDENTIFIER, EQ, INTLITERAL, SEMICOLON, IDENTIFIER, LT, IDENTIFIER, DOT, IDENTIFIER, SEMICOLON, IDENTIFIER, PLUSPLUS, RPAREN, LBRACE);
        addAll(IDENTIFIER, LBRACKET, IDENTIFIER, RBRACKET, EQ, IDENTIFIER, LBRACKET, IDENTIFIER, RBRACKET, SEMICOLON);
        addAll(RBRACE);
        addAll(IDENTIFIER, EQ, IDENTIFIER, SEMICOLON);
        addAll(RBRACE);
        addAll();
        addAll(IDENTIFIER, LBRACKET, IDENTIFIER, PLUSPLUS, RBRACKET, EQ, IDENTIFIER, SEMICOLON);
        addAll(RBRACE);
        addAll();

        // pop
        addAll(PUBLIC, INT, IDENTIFIER, LPAREN, RPAREN, LBRACE);
        addAll(RETURN, IDENTIFIER, LBRACKET, SUBSUB, IDENTIFIER, RBRACKET, SEMICOLON);
        addAll(RBRACE);
        addAll();

        // isEmpty
        addAll(PUBLIC, BOOLEAN, IDENTIFIER, LPAREN, RPAREN, LBRACE);
        addAll(RETURN, IDENTIFIER, EQEQ, INTLITERAL, SEMICOLON);
        addAll(RBRACE);
        addAll();

        // getSize
        addAll(PUBLIC, INT, IDENTIFIER, LPAREN, RPAREN, LBRACE);
        addAll(RETURN, IDENTIFIER, SEMICOLON);
        addAll(RBRACE);
        addAll();

        // getInstanceofStack
        addAll(PUBLIC, STATIC, INT, IDENTIFIER, LPAREN, RPAREN, LBRACE);
        addAll(RETURN, IDENTIFIER, SEMICOLON);
        addAll(RBRACE);

        addAll(RBRACE);
        addAll(EOF);
    }

    @Test
    public void stackTest() {
        Lexer lexer = new Lexer(stack);
        List<Token> tokens = lexer.every();

        for (int i = 0; i < kinds.size(); i++) {
            Assertions.assertEquals(kinds.get(i), tokens.get(i).kind);
        }

        Assertions.assertEquals(kinds.size(), tokens.size());
    }
}
