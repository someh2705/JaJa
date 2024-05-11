package io.jaja;

public enum TokenKind {
    PLUS("+"),
    STAR("*"),
    INTLITERAL(TokenTag.NUMERIC);

    private final String name;
    private final TokenTag tag;

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
