package org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces;

import org.fermat.fermat_job_api.layer.sub_app_module.common.JobCommunitySubAppModuleManager;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.utils.JobSeekerCommunitySettings;

import java.io.Serializable;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 22/06/16.
 */
public interface JobSeekerCommunitySubAppModuleManager
        extends JobCommunitySubAppModuleManager<
        JobSeekerCommunitySettings,
        JobSeekerCommunitySelectableIdentity,
        JobSeekerCommunityInformation,
        JobSeekerCommunitySearch>,
        Serializable {

        /**
         * This interface only extends JobCommunitySubAppModuleManager, this can be changed in the
         * future.
         */

}
