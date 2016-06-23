package org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.utils;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;

import org.fermat.fermat_job_api.all_definition.enums.JobTitle;
import org.fermat.fermat_job_api.all_definition.interfaces.JobActorExposingData;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/06/16.
 */
public class JobSeekerExposingData extends JobActorExposingData {

    /**
     * Constructor with parameters
     *
     * @param publicKey
     * @param alias
     * @param image
     */
    public JobSeekerExposingData(
            String publicKey,
            String alias,
            byte[] image,
            Location location,
            String jobTitleString) {
        super(publicKey, alias, image, location);
        JobTitle title;
        try {
            title = JobTitle.getByCode(jobTitleString);
        } catch (InvalidParameterException e) {
            title = JobTitle.getDefaultJobTitle();
        }
        this.jobEnum = title;
    }

    /**
     * Constructor with parameters
     * @param publicKey
     * @param alias
     * @param image
     * @param location
     * @param jobTitle
     */
    public JobSeekerExposingData(
            String publicKey,
            String alias,
            byte[] image,
            Location location,
            JobTitle jobTitle) {
        super(publicKey, alias, image, location, jobTitle);
    }

    /**
     * This method returns the JobTitle.
     * @return
     */
    public JobTitle getExtraDataEnum() {
        return (JobTitle) jobEnum;
    }
}
