package org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces;

import org.fermat.fermat_job_api.all_definition.enums.JobTitle;
import org.fermat.fermat_job_api.layer.sub_app_module.common.JobActorCommunitySearch;
import org.fermat.fermat_job_api.layer.sub_app_module.exceptions.CantGetJobActorResultException;

import java.util.List;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 22/06/16.
 */
public interface JobSeekerCommunitySearch
        extends JobActorCommunitySearch<JobSeekerCommunityInformation> {

    /**
     * This method returns the search by a given Job Title.
     * @param jobTitle
     * @return
     * @throws CantGetJobActorResultException
     */
    List<JobSeekerCommunityInformation> getResultJobTitle(JobTitle jobTitle)
            throws CantGetJobActorResultException;

}
