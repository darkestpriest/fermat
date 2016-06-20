package org.fermat.fermat_job_plugin.layer.identity.job_seeker.developer.version_1.structure;

import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantCreateJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantDeleteJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantListJobsPlatformIdentitiesException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantUpdateJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.JobPlatformIdentityAlreadyExistsException;
import org.fermat.fermat_job_api.layer.identity.job_seeker.interfaces.JobSeeker;
import org.fermat.fermat_job_api.layer.identity.job_seeker.interfaces.JobSeekerIdentityManager;

import java.util.List;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 20/06/16.
 */
public class JobSeekerIdentityPluginManager implements JobSeekerIdentityManager {
    @Override
    public List<JobSeeker> getIdentitiesFromCurrentDeviceUser() throws CantListJobsPlatformIdentitiesException {
        return null;
    }

    @Override
    public JobSeeker createNewIdentity(String alias, byte[] imageBytes) throws CantCreateJobPlatformIdentityException, JobPlatformIdentityAlreadyExistsException {
        return null;
    }

    @Override
    public JobSeeker updateIdentity(String publicKey, String alias, byte[] imageBytes) throws CantUpdateJobPlatformIdentityException {
        return null;
    }

    @Override
    public boolean hasIdentity() throws CantListJobsPlatformIdentitiesException {
        return false;
    }

    @Override
    public void deleteIdentity(String publicKey) throws CantDeleteJobPlatformIdentityException {

    }
}
