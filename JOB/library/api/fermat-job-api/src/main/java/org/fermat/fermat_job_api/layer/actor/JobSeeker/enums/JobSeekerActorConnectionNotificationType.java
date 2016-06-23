package org.fermat.fermat_job_api.layer.actor.JobSeeker.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * Created by Miguel Payarez (miguel_payarez@hotmail.com) on 6/23/16.
 */
public enum JobSeekerActorConnectionNotificationType implements FermatEnum {

    /**
     * To do make code more readable, please keep the elements in the Enum sorted alphabetically.
     */
    ACTOR_REVIEW_RESUME            ("JSARR"),


            ;

    private final String code;

    JobSeekerActorConnectionNotificationType(final String code) {
        this.code = code;
    }

    //PUBLIC METHODS

    public static JobSeekerActorConnectionNotificationType getByCode(String code)
            throws InvalidParameterException {
        for (JobSeekerActorConnectionNotificationType value : values()) {
            if (value.getCode().equals(code)) return value;
        }
        throw new InvalidParameterException(
                InvalidParameterException.DEFAULT_MESSAGE,
                null, "Code Received: " + code,
                "This Code Is Not Valid for the JobSeekerActorConnectionNotificationType enum.");
    }

    @Override
    public String toString() {
        return "JobSeekerActorConnectionNotificationType{" +
                "code='" + code + '\'' +
                '}';
    }

    //GETTER AND SETTERS

    @Override
    public String getCode() {
        return this.code;
    }


}
