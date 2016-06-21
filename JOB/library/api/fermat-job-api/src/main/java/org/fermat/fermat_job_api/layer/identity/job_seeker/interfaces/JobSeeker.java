package org.fermat.fermat_job_api.layer.identity.job_seeker.interfaces;

import org.fermat.fermat_job_api.all_definition.enums.ExposureLevel;
import org.fermat.fermat_job_api.all_definition.enums.JobTitle;
import org.fermat.fermat_job_api.all_definition.interfaces.JobIdentity;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/06/16.
 */
public interface JobSeeker extends JobIdentity {

    /**
     * This method returns the exposure level from this identity.
     * @return
     */
    ExposureLevel getExposureLevel();

    /**
     * This method returns the Job Title
     * @return
     */
    JobTitle getJobTitle();

}
