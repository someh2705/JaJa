package io.jaja.bind;

public class ResultObject implements ShellResult {
    private final String message;

    public ResultObject() {
        this.message = "";
    }

    public ResultObject(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return message;
    }
}
