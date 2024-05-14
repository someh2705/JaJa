package io.jaja.token;

import java.util.regex.Pattern;

public enum TokenTag {
    _ZERO("0"),
    _NON_ZERO_DIGIT("[1-9]"),
    _JAVA_LETTER("[a-zA-Z$_]"),
    _JAVA_LETTER_OR_DIGIT("[a-zA-Z0-9$_]"),
    _NUMERIC("[0-9]"),

    _DIGIT(_ZERO.or(_NON_ZERO_DIGIT)),

    _DECIMAL_NUMERAL1(_ZERO.regex),
    _DECIMAL_NUMERAL2(_NON_ZERO_DIGIT.regex + _DIGIT.exit()),

    WHITESPACE("[ \\t\\r\\n\\u000C]+"),
    DECIMAL_NUMERAL(_DECIMAL_NUMERAL1.or(_DECIMAL_NUMERAL2)),
    JAVA_IDENTIFIER(_JAVA_LETTER.regex + _JAVA_LETTER_OR_DIGIT.star()),
    ;

    private final String regex;
    private final Pattern pattern;

    TokenTag(String regex) {
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    public Pattern getPattern() {
        return pattern;
    }

    private String star() {
        return regex + "*";
    }

    private String exit() {
        return "[" + regex + "]" + "?";
    }

    private String or(TokenTag other) {
        return regex + "|" + other.regex;
    }

}
