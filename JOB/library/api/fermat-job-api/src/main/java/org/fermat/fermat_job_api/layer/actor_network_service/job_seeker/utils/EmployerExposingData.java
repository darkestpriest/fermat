package org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.utils;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;

import org.fermat.fermat_job_api.all_definition.enums.Industry;
import org.fermat.fermat_job_api.all_definition.interfaces.JobActorExposingData;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 13/06/16.
 */
public class EmployerExposingData extends JobActorExposingData {

    /**
     * Constructor with parameters
     *
     * @param publicKey
     * @param alias
     * @param image
     */
    public EmployerExposingData(
            String publicKey,
            String alias,
            byte[] image,
            Location location,
            String industryString) {
        super(publicKey, alias, image, location);
        Industry industryTemp;
        try {
            industryTemp = Industry.getByCode(industryString);
        } catch (InvalidParameterException e) {
            industryTemp = Industry.getDefaultIndustry();
        }
        this.jobEnum = industryTemp;
    }

    /**
     * Constructor with parameters
     * @param publicKey
     * @param alias
     * @param image
     * @param location
     * @param industry
     */
    public EmployerExposingData(
            String publicKey,
            String alias,
            byte[] image,
            Location location,
            Industry industry) {
        super(publicKey, alias, image, location, industry);
    }

    /**
     * This method returns the Industry.
     * @return
     */
    public Industry getExtraDataEnum() {
        return (Industry) jobEnum;
    }
}
