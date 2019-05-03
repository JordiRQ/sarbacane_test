package com.sarbacane.entry_test.exceptions;

public class CSVException extends Exception {

    private static final long serialVersionUID = 3434191187725131861L;

    /**
     * Constructs a <tt>CSVException</tt> with the specified message.
     *
     * @param s the detail message.
     */
    public CSVException(String s) {
        super(s);
    }

}
