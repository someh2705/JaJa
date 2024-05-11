package io.jaja;

public enum TokenKind {
    EOF,
    PLUS("+"),
    STAR("*"),
    INTLITERAL(TokenTag.NUMERIC);

    private final String name;
    private final TokenTag tag;

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
    }

    public boolean match(String string) {
        if (name != null && name.equals(string)) return true;
        return tag.match(string);
    }
}
