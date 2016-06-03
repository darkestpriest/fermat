package org.fermat.fermat_job_api.all_definition.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/06/16.
 */
public class CantListPendingConnectionRequestsException extends FermatException {
    
    public static final String DEFAULT_MESSAGE = "CANNOT LIST THE PENDING CONNECTION REQUEST";

        /**
         * Constructor with parameters
         * @param message
         * @param cause
         * @param context
         * @param possibleReason
         */
        public CantListPendingConnectionRequestsException(
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
        public CantListPendingConnectionRequestsException(
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
        public CantListPendingConnectionRequestsException(
                final String message,
                final Exception cause) {
            this(message, cause, "", "");
        }

        /**
         * Constructor with parameters
         * @param message
         */
        public CantListPendingConnectionRequestsException(final String message) {
            this(message, null);
        }

        /**
         * Constructor with parameters
         * @param exception
         */
        public CantListPendingConnectionRequestsException(final Exception exception) {
            this(exception.getMessage());
            setStackTrace(exception.getStackTrace());
        }

        /**
         * Constructor with parameters
         */
        public CantListPendingConnectionRequestsException() {
            this(DEFAULT_MESSAGE);
        }
}
