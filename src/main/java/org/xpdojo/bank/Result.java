package org.xpdojo.bank;

public class Result {

    public static Result success() {
        return SUCCESS;
    }

    public static Result failure() {
        return FAILURE;
    }

    public boolean succeeded() {
        return succeeded;
    }

    private static Result SUCCESS = new Result(true);
    private static Result FAILURE = new Result(false);

    private boolean succeeded;

    private Result(boolean succeeded) {
        this.succeeded = succeeded;
    }
}
