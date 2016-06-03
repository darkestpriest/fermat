package org.fermat.fermat_job_api.all_definition.exceptions;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/06/16.
 */
public class CantAcceptConnectionRequestException extends JOBException {

    public static final String DEFAULT_MESSAGE = "CANNOT ACCEPT CONNECTION REQUEST";

    /**
     * Constructor with parameters
     * @param message
     * @param cause
     * @param context
     * @param possibleReason
     */
    public CantAcceptConnectionRequestException(
            final String message,
            final Exception cause,
            final String context,
            final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    /**
     * Constructor with parameters
     * @param cause
     * @param context
     * @param possibleReason
     */
    public CantAcceptConnectionRequestException(
            Exception cause,
            String context,
            String possibleReason) {
        super(DEFAULT_MESSAGE , cause, context, possibleReason);
    }

    /**
     * Constructor with parameters
     * @param message
     * @param cause
     */
    public CantAcceptConnectionRequestException(
            final String message,
            final Exception cause) {
        this(message, cause, "", "");
    }

    /**
     * Constructor with parameters
     * @param message
     */
    public CantAcceptConnectionRequestException(final String message) {
        this(message, null);
    }

    /**
     * Constructor with parameters
     * @param exception
     */
    public CantAcceptConnectionRequestException(final Exception exception) {
        this(exception.getMessage());
        setStackTrace(exception.getStackTrace());
    }

    /**
     * Constructor with parameters
     */
    public CantAcceptConnectionRequestException() {
        this(DEFAULT_MESSAGE);
    }
}
