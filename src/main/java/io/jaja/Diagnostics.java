package io.jaja;

public class Diagnostics extends RuntimeException {
    private boolean panic;

    public Diagnostics(String message) {
        this(message, false);
    }

    public Diagnostics(String message, boolean panic) {
        super(message);
        this.panic = panic;
    }

    public boolean isPanic() {
        return panic;
    }
}
