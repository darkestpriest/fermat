package org.fermat.fermat_job_plugin.layer.identity.employer.developer.version_1.structure;

import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;
import com.bitdubai.fermat_api.layer.osa_android.location_system.LocationManager;
import com.bitdubai.fermat_api.layer.osa_android.location_system.exceptions.CantGetDeviceLocationException;
import com.bitdubai.fermat_pip_api.layer.user.device_user.exceptions.CantGetLoggedInDeviceUserException;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUser;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUserManager;

import org.fermat.fermat_job_api.all_definition.enums.ExposureLevel;
import org.fermat.fermat_job_api.all_definition.enums.Frequency;
import org.fermat.fermat_job_api.all_definition.enums.Industry;
import org.fermat.fermat_job_api.all_definition.exceptions.CantExposeIdentitiesException;
import org.fermat.fermat_job_api.all_definition.exceptions.CantExposeIdentityException;
import org.fermat.fermat_job_api.all_definition.interfaces.JobActorExposingData;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces.JobSeekerManager;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.utils.EmployerExposingData;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantChangeExposureLevelException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantCreateJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantDeleteJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantGetIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantHideIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantListJobsPlatformIdentitiesException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantPublishIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantUpdateJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.IdentityNotFoundException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.JobPlatformIdentityAlreadyExistsException;
import org.fermat.fermat_job_api.layer.identity.employer.interfaces.Employer;
import org.fermat.fermat_job_api.layer.identity.employer.interfaces.EmployerIdentityManager;
import org.fermat.fermat_job_plugin.layer.identity.employer.developer.version_1.EmployerIdentityPluginRoot;
import org.fermat.fermat_job_plugin.layer.identity.employer.developer.version_1.database.EmployerIdentityDatabaseDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 20/06/16.
 */
public class EmployerIdentityPluginManager implements EmployerIdentityManager {

    /**
     * Represents the plugin root class
     */
    private EmployerIdentityPluginRoot jobSeekerIdentityPluginRoot;

    /**
     * Represents the plugin database dao.
     */
    private EmployerIdentityDatabaseDao jobSeekerIdentityDatabaseDao;

    /**
     * Represents the device user manager.
     */
    DeviceUserManager deviceUserManager;

    /**
     * Represents the Job Seeker Actor Network Service manager.
     */
    JobSeekerManager jobSeekerManager;

    /**
     * Represents the location plugin manager.
     */
    LocationManager locationManager;

    private int identitiesListed = 0;

    /**
     * Default constructor with parameters.
     * @param jobSeekerIdentityPluginRoot
     * @param jobSeekerIdentityDatabaseDao
     * @param deviceUserManager
     * @param jobSeekerManager
     * @param locationManager
     */
    public EmployerIdentityPluginManager(
            EmployerIdentityPluginRoot jobSeekerIdentityPluginRoot,
            EmployerIdentityDatabaseDao jobSeekerIdentityDatabaseDao,
            DeviceUserManager deviceUserManager,
            JobSeekerManager jobSeekerManager,
            LocationManager locationManager ) {
        this.jobSeekerIdentityPluginRoot = jobSeekerIdentityPluginRoot;
        this.jobSeekerIdentityDatabaseDao = jobSeekerIdentityDatabaseDao;
        this.deviceUserManager = deviceUserManager;
        this.jobSeekerManager = jobSeekerManager;
        this.locationManager = locationManager;
    }

    /**
     * This method returns all the identities registered in the device
     * @return
     * @throws CantListJobsPlatformIdentitiesException
     */
    @Override
    public List<Employer> getIdentitiesFromCurrentDeviceUser()
            throws CantListJobsPlatformIdentitiesException {
        try {
            List<Employer> jobSeekerList =
                    jobSeekerIdentityDatabaseDao.listIdentitiesFromDeviceUser(
                            deviceUserManager.getLoggedInDeviceUser());
            //Determinate the number of identities in database.
            identitiesListed = jobSeekerList.size();
            return jobSeekerList;
        } catch (CantGetLoggedInDeviceUserException e) {
            throw new CantListJobsPlatformIdentitiesException(
                    e,
                    "Listing identities in current device user",
                    "Cannot get Logged in device user");
        }
    }

    /**
     * This method creates and register a new identity with default JobTitle
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
    @Override
    public Employer createNewIdentity(
            final String alias,
            final DeviceUser deviceUser,
            byte[] imageProfile,
            ExposureLevel exposureLevel,
            long accuracy,
            Frequency frequency)
            throws CantCreateJobPlatformIdentityException,
            JobPlatformIdentityAlreadyExistsException {
        return jobSeekerIdentityDatabaseDao.createNewEmployerIdentity(
                alias,
                deviceUser,
                imageProfile,
                exposureLevel,
                accuracy,
                frequency,
                Industry.getDefaultIndustry()
        );
    }

    /**
     * This method creates a new identity with jobTitle
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
    @Override
    public Employer createNewIdentity(
            final String alias,
            final DeviceUser deviceUser,
            byte[] imageProfile,
            ExposureLevel exposureLevel,
            long accuracy,
            Frequency frequency,
            Industry industry)
            throws CantCreateJobPlatformIdentityException,
            JobPlatformIdentityAlreadyExistsException {
        return jobSeekerIdentityDatabaseDao.createNewEmployerIdentity(
                alias,
                deviceUser,
                imageProfile,
                exposureLevel,
                accuracy,
                frequency,
                industry
        );
    }

    /**
     * This method updates an existing identity with default job title
     * @param alias
     * @param publicKey
     * @param imageProfile
     * @param accuracy
     * @param frequency
     * @param exposureLevel
     * @return
     * @throws CantUpdateJobPlatformIdentityException
     */
    @Override
    public Employer updateIdentity(
            String alias,
            String publicKey,
            byte[] imageProfile,
            long accuracy,
            Frequency frequency,
            ExposureLevel exposureLevel
    ) throws CantUpdateJobPlatformIdentityException {
        return jobSeekerIdentityDatabaseDao.updateCryptoBrokerIdentity(
                alias,
                publicKey,
                imageProfile,
                accuracy,
                frequency,
                exposureLevel,
                Industry.getDefaultIndustry());
    }

    /**
     * This method updates an identity with jobTitle
     * @param alias
     * @param publicKey
     * @param imageProfile
     * @param accuracy
     * @param frequency
     * @param exposureLevel
     * @param industry
     * @return
     * @throws CantUpdateJobPlatformIdentityException
     */
    @Override
    public Employer updateIdentity(
            String alias,
            String publicKey,
            byte[] imageProfile,
            long accuracy,
            Frequency frequency,
            ExposureLevel exposureLevel,
            Industry industry
    ) throws CantUpdateJobPlatformIdentityException {
        return jobSeekerIdentityDatabaseDao.updateCryptoBrokerIdentity(
                alias,
                publicKey,
                imageProfile,
                accuracy,
                frequency,
                exposureLevel,
                industry);
    }

    /**
     * This method returns true if exists any identity in the device
     * @return
     * @throws CantListJobsPlatformIdentitiesException
     */
    @Override
    public boolean hasIdentity() throws CantListJobsPlatformIdentitiesException {
        if(identitiesListed>0)
            return true;
        return false;
    }

    @Override
    public void deleteIdentity(String publicKey) throws CantDeleteJobPlatformIdentityException {
        //TODO: To implement
    }

    /**
     * This method contains the logic to publish an identity.
     * @param publicKey
     * @throws CantPublishIdentityException
     * @throws IdentityNotFoundException
     */
    @Override
    public void publishIdentity(String publicKey)
            throws CantPublishIdentityException,
            IdentityNotFoundException, CantExposeIdentityException {
        try {
            this.jobSeekerIdentityDatabaseDao.changeExposureLevel(
                    publicKey,
                    ExposureLevel.PUBLISH);
            Location location = locationManager.getLocation();
            Employer employer = jobSeekerIdentityDatabaseDao.getIdentity(publicKey);
            EmployerExposingData jobSeekerExposingData = new EmployerExposingData(
                    publicKey,
                    employer.getAlias(),
                    employer.getProfileImage(),
                    location,
                    employer.getIndustry());
            jobSeekerManager.exposeIdentity(jobSeekerExposingData);
        } catch (CantChangeExposureLevelException e) {
            throw new CantPublishIdentityException(
                    e,
                    "Publishing an identity",
                    "Cannot change the exposure level");
        } catch (CantGetDeviceLocationException e) {
            throw new CantPublishIdentityException(
                    e,
                    "Publishing an identity",
                    "Cannot change get the device location");
        } catch (CantGetIdentityException e) {
            throw new CantPublishIdentityException(
                    e,
                    "Publishing an identity",
                    "Cannot change get an identity");
        }
    }

    /**
     * This method contains all the logic to hide an identity
     * @param publicKey
     * @throws CantHideIdentityException
     * @throws IdentityNotFoundException
     */
    @Override
    public void hideIdentity(String publicKey)
            throws CantHideIdentityException,
            IdentityNotFoundException {
        try {
            this.jobSeekerIdentityDatabaseDao.changeExposureLevel(
                    publicKey,
                    ExposureLevel.HIDE);
        } catch (CantChangeExposureLevelException e) {
            throw new CantHideIdentityException(
                    e,
                    "Hiding an identity",
                    "Cannot change the exposure level");
        }
    }

    /**
     * This method exposes all the available identities.
     * @throws CantExposeIdentitiesException
     */
    public void exposeIdentities() throws CantExposeIdentitiesException {
        try{
            Location location = locationManager.getLocation();
            final List<JobActorExposingData> employerExposingDataArrayList = new ArrayList<>();
            List<Employer> jobSeekerList =
                    jobSeekerIdentityDatabaseDao.listIdentitiesFromDeviceUser(
                            deviceUserManager.getLoggedInDeviceUser());
            ExposureLevel exposureLevel;
            for (final Employer identity : jobSeekerList) {
                exposureLevel = identity.getExposureLevel();
                switch (exposureLevel){
                    case PUBLISH:
                        employerExposingDataArrayList.add(
                                new EmployerExposingData(
                                        identity.getPublicKey(),
                                        identity.getAlias(),
                                        identity.getProfileImage(),
                                        location,
                                        identity.getIndustry()
                                )
                        );
                        break;
                    default:
                        continue;
                }
            }
            jobSeekerManager.exposeIdentities(employerExposingDataArrayList);
        } catch (CantGetDeviceLocationException e) {
            throw new CantExposeIdentitiesException(
                    e,
                    "Exposing identities",
                    "Cannot get the device location");
        } catch (CantListJobsPlatformIdentitiesException e) {
            throw new CantExposeIdentitiesException(
                    e,
                    "Exposing identities",
                    "Cannot list identities");
        } catch (CantGetLoggedInDeviceUserException e) {
            throw new CantExposeIdentitiesException(
                    e,
                    "Exposing identities",
                    "Cannot get the logged device user");
        }
    }
}
