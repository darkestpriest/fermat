package org.fermat.fermat_job_api.all_definition.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 21/06/16.
 */
public enum JobTitle implements FermatEnum {

    ELECTRONIC_ENGINEER("EEE","Electronic Engineer"),
    JAVA_DEVELOPER("JDV","Java Developer"),
    ;

    private final String code;
    private final String friendlyName;

    /**
     * Sets the default JobTitle.
     */
    private static final JobTitle DEFAULT_JOB_TITLE = JobTitle.JAVA_DEVELOPER;

    JobTitle(final String code, final String friendlyName){
        this.code = code;
        this.friendlyName = friendlyName;
    }

    //PUBLIC METHODS

    public static JobTitle getByCode(final String code) throws InvalidParameterException {
        for (JobTitle value : values()) {
            if (value.getCode().equals(code)) return value;
        }
        throw new InvalidParameterException(
                InvalidParameterException.DEFAULT_MESSAGE,
                null, "Code Received: " + code,
                "This Code Is Not Valid for the JobTitle enum.");
    }

    //GETTERS AND SETTERS

    @Override
    public final String getCode(){
        return this.code;
    }

    /**
     * Gets the default Job title.
     * @return the default ArtExternalPlatform
     */
    public static JobTitle getDefaultJobTitle(){
        return DEFAULT_JOB_TITLE;
    }

    /**
     * This method returns the Platform friendly name.
     * @return
     */
    public String getFriendlyName() {
        return this.friendlyName;
    }

}
