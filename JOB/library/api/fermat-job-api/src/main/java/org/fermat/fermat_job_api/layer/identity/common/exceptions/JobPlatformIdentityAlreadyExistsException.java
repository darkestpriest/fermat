package org.fermat.fermat_job_api.layer.identity.common.exceptions;

import org.fermat.fermat_job_api.all_definition.exceptions.JOBException;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 13/06/16.
 */
public class JobPlatformIdentityAlreadyExistsException extends JOBException {

    public static final String DEFAULT_MESSAGE = "JOB PLATFORM IDENTITY ALREADY EXISTS";

    /**
     * Constructor with parameters
     * @param message
     * @param cause
     * @param context
     * @param possibleReason
     */
    public JobPlatformIdentityAlreadyExistsException(
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
    public JobPlatformIdentityAlreadyExistsException(
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
    public JobPlatformIdentityAlreadyExistsException(
            final String message,
            final Exception cause) {
        this(message, cause, "", "");
    }

    /**
     * Constructor with parameters
     * @param message
     */
    public JobPlatformIdentityAlreadyExistsException(final String message) {
        this(message, null);
    }

    /**
     * Constructor with parameters
     * @param exception
     */
    public JobPlatformIdentityAlreadyExistsException(final Exception exception) {
        this(exception.getMessage());
        setStackTrace(exception.getStackTrace());
    }

    /**
     * Constructor with parameters
     */
    public JobPlatformIdentityAlreadyExistsException() {
        this(DEFAULT_MESSAGE);
    }

}
