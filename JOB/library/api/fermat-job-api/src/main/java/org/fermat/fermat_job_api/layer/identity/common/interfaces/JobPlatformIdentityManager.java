package org.fermat.fermat_job_api.layer.identity.common.interfaces;

import org.fermat.fermat_job_api.all_definition.interfaces.JobIdentity;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantCreateJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantDeleteJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantListJobsPlatformIdentitiesException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantUpdateJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.JobPlatformIdentityAlreadyExistsException;

import java.util.List;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 13/06/16.
 */
public interface JobPlatformIdentityManager<T extends JobIdentity> {

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
     * @param imageBytes
     * @return
     * @throws CantCreateJobPlatformIdentityException
     * @throws JobPlatformIdentityAlreadyExistsException
     */
    T createNewIdentity(
            final String alias,
            final byte[] imageBytes) throws
            CantCreateJobPlatformIdentityException,
            JobPlatformIdentityAlreadyExistsException;

    /**
     * This method updates an existing job platform
     * @param publicKey
     * @param alias
     * @param imageBytes
     * @return
     * @throws CantUpdateJobPlatformIdentityException
     */
    T updateIdentity(
            final String publicKey,
            final String alias,
            final byte[] imageBytes) throws
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
}
