package edu.sjsu.cmpe275.lms.errors;

/**
 * POJO Class to handle error messages
 */
public class Err {
    boolean anError;
    String message;

    /**
     * The default constructor
     */
    public Err() {
        this.anError = false;
        this.message = "";
    }

    /**
     * @param anError
     * @param message
     */
    public Err(boolean anError, String message) {
        this.anError = anError;
        this.message = message;
    }

    /**
     *
     * @return
     */
    public boolean isAnError() {
        return anError;
    }

    /**
     *
     * @param anError
     */
    public void setAnError(boolean anError) {
        this.anError = anError;
    }

    /**
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
