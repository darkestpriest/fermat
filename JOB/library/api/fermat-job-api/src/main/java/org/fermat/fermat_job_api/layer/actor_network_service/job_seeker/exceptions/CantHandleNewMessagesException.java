package org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 18/06/16.
 */
public class CantHandleNewMessagesException extends FermatException {

    public static final String DEFAULT_MESSAGE = "CANNOT HANDLE NEW MESSAGE";

    /**
     * Constructor with parameters
     * @param message
     * @param cause
     * @param context
     * @param possibleReason
     */
    public CantHandleNewMessagesException(
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
    public CantHandleNewMessagesException(
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
    public CantHandleNewMessagesException(
            final String message,
            final Exception cause) {
        this(message, cause, "", "");
    }

    /**
     * Constructor with parameters
     * @param message
     */
    public CantHandleNewMessagesException(final String message) {
        this(message, null);
    }

    /**
     * Constructor with parameters
     * @param exception
     */
    public CantHandleNewMessagesException(final Exception exception) {
        this(exception.getMessage());
        setStackTrace(exception.getStackTrace());
    }

    /**
     * Constructor with parameters
     */
    public CantHandleNewMessagesException() {
        this(DEFAULT_MESSAGE);
    }
}

