package io.jaja;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private String input;
    private int position;

    private TokenKind[] spec = TokenKind.values();
    private List<Token> tokens = new ArrayList<>();

    public Lexer(String input) {
        this.input = input;

        int pos = 0;
        StringBuilder string = new StringBuilder();
        while (pos < input.length()) {
            char c = input.charAt(pos++);

            if (c != ' ') {
                string.append(c);

                if (pos != input.length()) {
                    continue;
                }
            }

            if (string.length() > 0 || pos == input.length()) {
                String field = string.toString();
                tokens.add(new Token(specification(field), field));
                string = new StringBuilder();
            }
        }

        tokens.add(new Token(TokenKind.EOF, null));
    }

    private TokenKind specification(String string) {
        for (TokenKind kind : spec) {
            if (kind.match(string)) return kind;
        }

        throw new IllegalStateException("Unknown token: " + string);
    }

    public Token current() {
        return tokens.get(position++);
    }

    public List<Token> every() {
        return tokens;
    }
}
