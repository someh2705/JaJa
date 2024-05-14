package io.jaja.token;

public enum TokenRule {
    DEFAULT, // 다음 문자가 어떤것이 오든 상관 없습니다.
    SEPARATE, // 다음 문자는 WHITESPACE가 와야합니다.
}
