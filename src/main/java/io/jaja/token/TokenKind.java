package io.jaja.token;

import io.jaja.utils.PatternUtils;

import java.util.regex.Pattern;

public enum TokenKind {
    EOF,
    PLUS("+"),
    STAR("*"),
    LPAREN("("),
    RPAREN(")"),
    EQ("="),
    SEMICOLON(";"),
    INT("int", TokenRule.SEPARATE),

    WHITESPACE(TokenTag.WHITESPACE, TokenRule.DEFAULT),
    INTLITERAL(TokenTag.DECIMAL_NUMERAL),
    IDENTIFIER(TokenTag.JAVA_IDENTIFIER),
    ;

    private final String name;
    private final TokenTag tag;
    private final TokenRule rule;

    private final static Pattern edge = Pattern.compile("[+*=;()]");

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
            if (PatternUtils.startsWith(next, edge)) return result; // 10; 10*20 number= 예외 처리

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
}
