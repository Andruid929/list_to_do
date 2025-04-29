package net.druidlabs.todo.util;

public class RefuseToBeEmptyException extends NullPointerException {

    public RefuseToBeEmptyException(String s) {
            super(s);
    }

    public RefuseToBeEmptyException() {
        super();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
