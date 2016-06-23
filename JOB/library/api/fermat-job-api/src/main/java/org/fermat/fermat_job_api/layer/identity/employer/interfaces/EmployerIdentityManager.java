package org.fermat.fermat_job_api.layer.identity.employer.interfaces;

import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUser;

import org.fermat.fermat_job_api.all_definition.enums.ExposureLevel;
import org.fermat.fermat_job_api.all_definition.enums.Frequency;
import org.fermat.fermat_job_api.all_definition.enums.Industry;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantCreateJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantUpdateJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.JobPlatformIdentityAlreadyExistsException;
import org.fermat.fermat_job_api.layer.identity.common.interfaces.JobPlatformIdentityManager;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 13/06/16.
 */
public interface EmployerIdentityManager extends JobPlatformIdentityManager<Employer> {

    /**
     * This method creates a new identity with Industry
     * @param alias
     * @param deviceUser
     * @param imageProfile
     * @param exposureLevel
     * @param accuracy
     * @param frequency
     * @param industry
     * @return
     * @throws CantCreateJobPlatformIdentityException
     * @throws JobPlatformIdentityAlreadyExistsException
     */
    Employer createNewIdentity(
            final String alias,
            final DeviceUser deviceUser,
            byte[] imageProfile,
            ExposureLevel exposureLevel,
            long accuracy,
            Frequency frequency,
            Industry industry) throws
            CantCreateJobPlatformIdentityException,
            JobPlatformIdentityAlreadyExistsException;

    /**
     * This method updates an identity with jobTitle
     * @param alias
     * @param imageProfile
     * @param exposureLevel
     * @param accuracy
     * @param frequency
     * @param industry
     * @return
     * @throws CantCreateJobPlatformIdentityException
     * @throws JobPlatformIdentityAlreadyExistsException
     */
    Employer updateIdentity(
            String alias,
            String publicKey,
            byte[] imageProfile,
            long accuracy,
            Frequency frequency,
            ExposureLevel exposureLevel,
            Industry industry) throws
            CantUpdateJobPlatformIdentityException,
            JobPlatformIdentityAlreadyExistsException;

}
