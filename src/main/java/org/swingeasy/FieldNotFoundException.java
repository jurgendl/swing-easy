package org.swingeasy;

/**
 * {@link ObjectWrapper} fout
 * 
 * @author jdlandsh
 */
public class FieldNotFoundException extends RuntimeException {
    /** serialVersionUID */
    private static final long serialVersionUID = 5863861684947177500L;

    /**
     * Creates a new FieldNotFoundException object.
     */
    public FieldNotFoundException() {
        super();
    }

    /**
     * Creates a new FieldNotFoundException object.
     * 
     * @param message
     */
    public FieldNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a new FieldNotFoundException object.
     * 
     * @param message
     * @param cause
     */
    public FieldNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new FieldNotFoundException object.
     * 
     * @param cause
     */
    public FieldNotFoundException(Throwable cause) {
        super(cause);
    }
}
