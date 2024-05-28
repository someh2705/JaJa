package io.jaja.bind;

public class ReturnObject extends RuntimeException {
    private BindObject<?> value;

    public ReturnObject(BindObject<?> value) {
        this.value = value;
    }

    public BindObject<?> getValue() {
        return value;
    }
}
