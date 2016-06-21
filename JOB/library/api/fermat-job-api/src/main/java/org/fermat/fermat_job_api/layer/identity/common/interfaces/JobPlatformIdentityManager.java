package org.fermat.fermat_job_api.layer.identity.common.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUser;

import org.fermat.fermat_job_api.all_definition.enums.ExposureLevel;
import org.fermat.fermat_job_api.all_definition.enums.Frequency;
import org.fermat.fermat_job_api.all_definition.exceptions.CantExposeIdentityException;
import org.fermat.fermat_job_api.all_definition.interfaces.JobIdentity;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantCreateJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantDeleteJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantHideIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantListJobsPlatformIdentitiesException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantPublishIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantUpdateJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.IdentityNotFoundException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.JobPlatformIdentityAlreadyExistsException;

import java.util.List;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 13/06/16.
 */
public interface JobPlatformIdentityManager<T extends JobIdentity> extends FermatManager {

    /**
     * This method returns all the identities registered in the device
     * @return
     * @throws CantListJobsPlatformIdentitiesException
     */
    List<T> getIdentitiesFromCurrentDeviceUser()
            throws CantListJobsPlatformIdentitiesException;

    /**
     * This method creates and register a new identity.
     * @param alias
     * @param deviceUser
     * @param imageProfile
     * @param exposureLevel
     * @param accuracy
     * @param frequency
     * @return
     * @throws CantCreateJobPlatformIdentityException
     * @throws JobPlatformIdentityAlreadyExistsException
     */
    T createNewIdentity(
            final String alias,
            final DeviceUser deviceUser,
            byte[] imageProfile,
            ExposureLevel exposureLevel,
            long accuracy,
            Frequency frequency) throws
            CantCreateJobPlatformIdentityException,
            JobPlatformIdentityAlreadyExistsException;

    /**
     * This method updates an existing job platform
     * @param alias
     * @param publicKey
     * @param imageProfile
     * @param accuracy
     * @param frequency
     * @param exposureLevel
     * @return
     * @throws CantUpdateJobPlatformIdentityException
     */
    T updateIdentity(
            String alias,
            String publicKey,
            byte[] imageProfile,
            long accuracy,
            Frequency frequency,
            ExposureLevel exposureLevel) throws
            CantUpdateJobPlatformIdentityException;

    /**
     * This method returns true if exists any identity in the device
     * @return
     * @throws CantListJobsPlatformIdentitiesException
     */
    boolean hasIdentity() throws CantListJobsPlatformIdentitiesException;

    /**
     * This method changes the identity status to unavailable
     * @param publicKey
     * @throws CantDeleteJobPlatformIdentityException
     */
    void deleteIdentity(final String publicKey) throws CantDeleteJobPlatformIdentityException;

    /**
     * This method contains the logic to publish an identity.
     * @param publicKey
     * @throws CantPublishIdentityException
     * @throws IdentityNotFoundException
     */
    void publishIdentity(String publicKey)
            throws CantPublishIdentityException,
            IdentityNotFoundException, CantExposeIdentityException;

    /**
     * This method contains all the logic to hide an identity
     * @param publicKey
     * @throws CantHideIdentityException
     * @throws IdentityNotFoundException
     */
    void hideIdentity(String publicKey)
            throws CantHideIdentityException,
            IdentityNotFoundException;
}
