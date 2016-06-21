package org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_identity.interfaces;

import org.fermat.fermat_job_api.all_definition.enums.ExposureLevel;
import org.fermat.fermat_job_api.all_definition.enums.Frequency;
import org.fermat.fermat_job_api.all_definition.enums.JobTitle;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantCreateJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantUpdateJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.JobPlatformIdentityAlreadyExistsException;
import org.fermat.fermat_job_api.layer.identity.job_seeker.interfaces.JobSeeker;
import org.fermat.fermat_job_api.layer.sub_app_module.common.JobIdentitySubAppModuleManager;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_identity.JobSeekerPreferenceSettings;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 21/06/16.
 */
public interface JobSeekerIdentityModuleManager
        extends JobIdentitySubAppModuleManager<JobSeekerPreferenceSettings, JobSeeker> {

        /**
         * This method creates and register a new identity.
         * @param alias
         * @param imageProfile
         * @param exposureLevel
         * @param accuracy
         * @param frequency
         * @return
         * @throws CantCreateJobPlatformIdentityException
         * @throws JobPlatformIdentityAlreadyExistsException
         */
        JobSeeker createNewIdentity(
                final String alias,
                byte[] imageProfile,
                ExposureLevel exposureLevel,
                long accuracy,
                Frequency frequency,
                JobTitle title) throws
                CantCreateJobPlatformIdentityException,
                JobPlatformIdentityAlreadyExistsException;

        /**
         * This method updates an identity with jobTitle
         * @param alias
         * @param imageProfile
         * @param exposureLevel
         * @param accuracy
         * @param frequency
         * @param title
         * @return
         * @throws CantCreateJobPlatformIdentityException
         * @throws JobPlatformIdentityAlreadyExistsException
         */
        JobSeeker updateIdentity(
                String alias,
                String publicKey,
                byte[] imageProfile,
                long accuracy,
                Frequency frequency,
                ExposureLevel exposureLevel,
                JobTitle title) throws
                CantUpdateJobPlatformIdentityException,
                JobPlatformIdentityAlreadyExistsException;
}
