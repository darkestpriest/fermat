package org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces;


import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;

import org.fermat.fermat_job_api.all_definition.enums.JobTitle;
import org.fermat.fermat_job_api.layer.sub_app_module.common.JobActorCommunityInformation;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 22/06/16.
 */
public interface JobSeekerCommunityInformation extends JobActorCommunityInformation, Serializable {

    /**
     * This method returns the Job Title.
     * @return
     */
    JobTitle getJobTitle();

    /**
     * This method sets the actor type.
     * @return
     */

    Actors getActorType();



}
