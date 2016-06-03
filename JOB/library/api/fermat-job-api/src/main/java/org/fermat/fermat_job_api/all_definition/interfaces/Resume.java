package org.fermat.fermat_job_api.all_definition.interfaces;

import org.fermat.fermat_job_api.layer.identity.job_seeker.interfaces.JobSeeker;

import java.io.Serializable;

/**
 * This class represents a Job Seeker resume (curriculum vitae)
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/06/16.
 */
public interface Resume extends Serializable {

    /**
     * This method returns the Job Seeker identity.
     * @return
     */
    JobSeeker getJobSeekerIdentity();

}
