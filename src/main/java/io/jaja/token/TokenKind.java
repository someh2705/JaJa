package io.jaja.token;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TokenKind {
    EOF,
    WHITESPACE(TokenTag.BLANK),
    PLUS("+"),
    STAR("*"),
    LPAREN("("),
    RPAREN(")"),
    EQ("="),
    SEMICOLON(";"),
    INT("int"),
    IDENTIFIER(TokenTag.NAMED),
    INTLITERAL(TokenTag.NUMERIC),
    ;

    private final String name;
    private final TokenTag tag;
    private final Pattern pattern;

    TokenKind() {
        this(null, TokenTag.DEFAULT);
    }

    TokenKind(String name) {
        this(name, TokenTag.DEFAULT);
    }

    TokenKind(TokenTag tag) {
        this(null, tag);
    }

    TokenKind(String name, TokenTag tag) {
        this.name = name;
        this.tag = tag;

        if (name != null) {
            if (name.length() == 1) {
                pattern = Pattern.compile("^[" + name + "]");
            } else {
                pattern = Pattern.compile("^" + name);
            }
        } else {
            switch (tag) {
                case DEFAULT:
                    pattern = null;
                    break;
                case BLANK:
                    pattern = Pattern.compile("^\\s+");
                    break;
                case NUMERIC:
                    pattern = Pattern.compile("\\b(?:0|-?[1-9][0-9]*)\\b");
                    break;
                case NAMED:
                    pattern = Pattern.compile("^[a-zA-Z_$][a-zA-Z_$0-9]*");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid token tag: " + tag);
            }
        }
    }

    public String parse(String string) {
        if (pattern == null) return null;

        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group();
        }

        return null;
    }
}
