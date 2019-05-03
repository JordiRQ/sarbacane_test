package com.sarbacane.entry_test.exceptions;

public class RecipientServiceException extends Exception {

    static final long serialVersionUID = -3467546682342349948L;

    /**
     * Constructs a <tt>RecipientServiceException</tt> with the specified message.
     *
     * @param s the detail message.
     */
    public RecipientServiceException(String s) {
        super(s);
    }

}
