package io.jaja.token;

import io.jaja.utils.PatternUtils;

import java.util.ArrayList;
import java.util.List;

public enum TokenKind {
    EOF,
    EOL("\n"),
    LPAREN("("),
    RPAREN(")"),
    LBRACE("{"),
    RBRACE("}"),
    LBRACKET("["),
    RBRACKET("]"),
    SEMICOLON(";"),
    COMMA(","),
    BANG("!"),
    QUES("?"),
    COLON(":"),
    EQEQ("=="),
    LTEQ("<="),
    GTEQ(">="),
    BANGEQ("!="),
    EQ("="),
    GT(">"),
    LT("<"),
    AMPAMP("&&"),
    BARBAR("||"),
    PLUSPLUS("++"),
    SUBSUB("--"),
    PLUS("+"),
    SUB("-"),
    STAR("*"),
    SLASH("/"),
    AMP("&"),
    BAR("|"),
    CARET("^"),
    PERCENT("%"),

    VOID("void", TokenRule.SEPARATE),
    BYTE("byte", TokenRule.SEPARATE),
    SHORT("short", TokenRule.SEPARATE),
    INT("int", TokenRule.SEPARATE),
    LONG("long", TokenRule.SEPARATE),
    CHAR("char", TokenRule.SEPARATE),
    FLOAT("float", TokenRule.SEPARATE),
    DOUBLE("double", TokenRule.SEPARATE),
    BOOLEAN("boolean", TokenRule.SEPARATE),
    STRING("String", TokenRule.SEPARATE),
    NULL("null", TokenRule.SEPARATE),
    TRUE("true", TokenRule.SEPARATE),
    FALSE("false", TokenRule.SEPARATE),

    NEW("new", TokenRule.SEPARATE),
    FINAL("final", TokenRule.SEPARATE),

    PUBLIC("public", TokenRule.SEPARATE),
    PRIVATE("private", TokenRule.SEPARATE),
    CLASS("class", TokenRule.SEPARATE),
    STATIC("static", TokenRule.SEPARATE),
    IMPORT("import", TokenRule.SEPARATE),
    THIS("this", TokenRule.SEPARATE),

    FOR("for", TokenRule.SEPARATE),
    WHILE("while", TokenRule.SEPARATE),
    CONTINUE("continue", TokenRule.SEPARATE),
    IF("if", TokenRule.SEPARATE),
    ELSE("else", TokenRule.SEPARATE),
    SWITCH("switch", TokenRule.SEPARATE),
    CASE("case", TokenRule.SEPARATE),
    BREAK("break", TokenRule.SEPARATE),
    RETURN("return", TokenRule.SEPARATE),

    WHITESPACE(TokenTag.WHITESPACE, TokenRule.DEFAULT),
    IDENTIFIER(TokenTag.JAVA_IDENTIFIER),
    FLOATLITERAL(TokenTag.FLOAT_NUMERAL),
    DOUBLELITERAL(TokenTag.DOUBLE_NUMERAL),
    INTLITERAL(TokenTag.DECIMAL_NUMERAL),
    STRINGLITERAL(TokenTag.STRING),
    DOT("."),
    ;

    private final String name;
    private final TokenTag tag;
    private final TokenRule rule;

    private final static List<Character> edge = available();

    private static List<Character> available() {
        List<Character> characters = new ArrayList<>();

        for (TokenKind kind : values()) {
            if (kind.name != null && kind.rule == TokenRule.DEFAULT) {
                characters.add(kind.name.charAt(0));
            }
        }

        return characters;
    }

    TokenKind() {
        this(null, null, TokenRule.DEFAULT);
    }

    TokenKind(String name) {
        this(name, null, TokenRule.DEFAULT);
    }

    TokenKind(String name, TokenRule rule) {
        this(name, null, rule);
    }

    TokenKind(TokenTag tag) {
        this(null, tag, TokenRule.SEPARATE);
    }

    TokenKind(TokenTag tag, TokenRule rule) {
        this(null, tag, rule);
    }

    TokenKind(String name, TokenTag tag, TokenRule rule) {
        this.name = name;
        this.tag = tag;
        this.rule = rule;
    }

    public String parse(String string) {
        String result = parseImpl(string);

        if (result != null && this != WHITESPACE && rule == TokenRule.SEPARATE) {
            String next = string.substring(result.length());
            if (next.isEmpty()) return result; // "int" 인 경우 예외 처리
            if (isEdge(next)) return result; // 10; 10*20 number= 예외 처리

            boolean isWhitespace = PatternUtils.startsWith(next, TokenTag.WHITESPACE.getPattern());
            if (!isWhitespace) return null;
        }

        return result;
    }

    private String parseImpl(String string) {
        if (name != null) {
            boolean isMatch = string.startsWith(name);
            return isMatch ? name : null;
        }

        if (tag != null) {
            return PatternUtils.startMatch(string, tag.getPattern());
        }

        return null;
    }

    private boolean isEdge(String string) {
        for (Character character : edge) {
            if (string.startsWith(character.toString())) {
                return true;
            }
        }

        return false;
    }
}
