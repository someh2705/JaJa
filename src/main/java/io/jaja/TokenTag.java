package io.jaja;

public enum TokenTag {
    DEFAULT,
    NUMERIC,
    ;

    public boolean match(String string) {
        switch (this) {
            case DEFAULT:
                return false;
            case NUMERIC:
                return string.matches("\\d+");
            default:
                throw new IllegalStateException("");
        }
    }
}
