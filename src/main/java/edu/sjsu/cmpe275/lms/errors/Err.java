package edu.sjsu.cmpe275.lms.errors;

/**
 * POJO Class to handle error messages
 */
public class Err {
    boolean anError;
    String message;

    public Err() {
        this.anError = false;
        this.message = "";
    }

    public Err(boolean anError, String message) {
        this.anError = anError;
        this.message = message;
    }

    public boolean isAnError() {
        return anError;
    }

    public void setAnError(boolean anError) {
        this.anError = anError;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
