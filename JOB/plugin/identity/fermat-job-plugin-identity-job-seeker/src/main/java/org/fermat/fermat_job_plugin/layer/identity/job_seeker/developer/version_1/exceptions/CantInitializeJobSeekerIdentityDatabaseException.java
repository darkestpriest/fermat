package org.fermat.fermat_job_plugin.layer.identity.job_seeker.developer.version_1.exceptions;

import org.fermat.fermat_job_api.all_definition.exceptions.JOBException;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 20/06/16.
 */
public class CantInitializeJobSeekerIdentityDatabaseException extends JOBException {

    public static final String DEFAULT_MESSAGE = "CANNOT INITIALIZE JOB SEEKER DATABASE";

    /**
     * Constructor with parameters
     * @param message
     * @param cause
     * @param context
     * @param possibleReason
     */
    public CantInitializeJobSeekerIdentityDatabaseException(
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
    public CantInitializeJobSeekerIdentityDatabaseException(
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
    public CantInitializeJobSeekerIdentityDatabaseException(
            final String message,
            final Exception cause) {
        this(message, cause, "", "");
    }

    /**
     * Constructor with parameters
     * @param message
     */
    public CantInitializeJobSeekerIdentityDatabaseException(final String message) {
        this(message, null);
    }

    /**
     * Constructor with parameters
     * @param exception
     */
    public CantInitializeJobSeekerIdentityDatabaseException(final Exception exception) {
        this(exception.getMessage());
        setStackTrace(exception.getStackTrace());
    }

    /**
     * Constructor with parameters
     */
    public CantInitializeJobSeekerIdentityDatabaseException() {
        this(DEFAULT_MESSAGE);
    }
}
