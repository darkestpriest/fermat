package org.fermat.fermat_job_api.all_definition.enums;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

import org.fermat.fermat_job_api.all_definition.interfaces.JobEnum;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 22/06/16.
 */
public enum Industry implements JobEnum {

    EDUCATION("EDN","Education"),
    SOFTWARE_DEVELOPMENT("SDT","Software Development"),
    ;

    private final String code;
    private final String friendlyName;

    /**
     * Sets the default Industry.
     */
    private static final Industry DEFAULT_INDUSTRY = Industry.SOFTWARE_DEVELOPMENT;

    Industry(final String code, final String friendlyName){
        this.code = code;
        this.friendlyName = friendlyName;
    }

    //PUBLIC METHODS

    public static Industry getByCode(final String code) throws InvalidParameterException {
        for (Industry value : values()) {
            if (value.getCode().equals(code)) return value;
        }
        throw new InvalidParameterException(
                InvalidParameterException.DEFAULT_MESSAGE,
                null, "Code Received: " + code,
                "This Code Is Not Valid for the Industry enum.");
    }

    //GETTERS AND SETTERS

    @Override
    public final String getCode(){
        return this.code;
    }

    /**
     * Gets the default Industry.
     * @return the default Industry
     */
    public static Industry getDefaultIndustry(){
        return DEFAULT_INDUSTRY;
    }

    /**
     * This method returns the Industry friendly name.
     * @return
     */
    public String getFriendlyName() {
        return this.friendlyName;
    }

}
