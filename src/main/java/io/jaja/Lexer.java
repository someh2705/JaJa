package io.jaja;

import io.jaja.token.Token;
import io.jaja.token.TokenKind;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private int position;

    private TokenKind[] spec = TokenKind.values();
    private List<Token> tokens = new ArrayList<>();

    public Lexer(String input) {
        int pos = 0;
        while (pos < input.length()) {
            Token token = specification(input.substring(pos));
            if (token.kind != TokenKind.WHITESPACE) {
                tokens.add(token);
            }
            pos += token.field.length();
        }

        tokens.add(new Token(TokenKind.EOF, null));
    }

    private Token specification(String string) {
        for (TokenKind kind : spec) {
            String field = kind.parse(string);
            if (field != null) {
                return new Token(kind, field);
            }
        }

        throw new Diagnostics("Unknown token: " + string);
    }

    public Token current() {
        return tokens.get(position++);
    }

    public List<Token> every() {
        return tokens;
    }
}
