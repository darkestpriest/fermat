package org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.exceptions;

import org.fermat.fermat_job_api.all_definition.exceptions.JOBException;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 13/06/16.
 */
public class CantInitializeDatabaseException extends JOBException {

    public static final String DEFAULT_MESSAGE = "CANNOT INITIALIZE DATABASE";

    /**
     * Constructor with parameters
     * @param message
     * @param cause
     * @param context
     * @param possibleReason
     */
    public CantInitializeDatabaseException(
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
    public CantInitializeDatabaseException(
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
    public CantInitializeDatabaseException(
            final String message,
            final Exception cause) {
        this(message, cause, "", "");
    }

    /**
     * Constructor with parameters
     * @param message
     */
    public CantInitializeDatabaseException(final String message) {
        this(message, null);
    }

    /**
     * Constructor with parameters
     * @param exception
     */
    public CantInitializeDatabaseException(final Exception exception) {
        this(exception.getMessage());
        setStackTrace(exception.getStackTrace());
    }

    /**
     * Constructor with parameters
     */
    public CantInitializeDatabaseException() {
        this(DEFAULT_MESSAGE);
    }
}
