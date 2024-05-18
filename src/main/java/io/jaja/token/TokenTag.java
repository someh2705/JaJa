package io.jaja.token;

import java.util.regex.Pattern;

public enum TokenTag {
    WHITESPACE("[ \\t\\r\\n\\u000C]+"),
    JAVA_IDENTIFIER("[a-zA-Z$_][a-zA-Z0-9$_]*"),
    DECIMAL_NUMERAL("\\d+"),
    FLOAT_NUMERAL("((\\d+)\\.?(\\d+)?f)|((\\d+)?\\.?(\\d+)f)"),
    DOUBLE_NUMERAL("((\\d+)\\.(\\d+)?)|((\\d+)?\\.(\\d+))"),
    STRING("\".*\""),
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
