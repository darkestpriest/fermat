package org.fermat.fermat_job_plugin.layer.sub_app_module.job_seeker_identity.developer.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.modules.ModuleManagerImpl;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.exceptions.ActorIdentityNotSelectedException;
import com.bitdubai.fermat_api.layer.modules.exceptions.CantGetSelectedActorIdentityException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_pip_api.layer.user.device_user.exceptions.CantGetLoggedInDeviceUserException;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUser;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUserManager;

import org.fermat.fermat_job_api.all_definition.enums.ExposureLevel;
import org.fermat.fermat_job_api.all_definition.enums.Frequency;
import org.fermat.fermat_job_api.all_definition.enums.JobTitle;
import org.fermat.fermat_job_api.all_definition.exceptions.CantExposeIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantCreateJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantHideIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantListJobsPlatformIdentitiesException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantPublishIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantUpdateJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.IdentityNotFoundException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.JobPlatformIdentityAlreadyExistsException;
import org.fermat.fermat_job_api.layer.identity.job_seeker.interfaces.JobSeeker;
import org.fermat.fermat_job_api.layer.identity.job_seeker.interfaces.JobSeekerIdentityManager;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_identity.JobSeekerPreferenceSettings;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_identity.interfaces.JobSeekerIdentityModuleManager;
import org.fermat.fermat_job_plugin.layer.sub_app_module.job_seeker_identity.developer.version_1.JobSeekerIdentitySubAppModulePluginRoot;

import java.util.List;
import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 21/06/16.
 */
public class JobSeekerIdentityModulePluginManager
        extends ModuleManagerImpl<JobSeekerPreferenceSettings>
        implements JobSeekerIdentityModuleManager {

    /**
     * Represents the job seeker identity manager
     */
    private JobSeekerIdentityManager jobSeekerIdentityManager;

    /**
     * Represents the plugin root class.
     */
    private JobSeekerIdentitySubAppModulePluginRoot jobSeekerIdentitySubAppModulePluginRoot;

    /**
     * Represents the device user manager.
     */
    private DeviceUserManager deviceUserManager;

    /**
     * Default constructor with parameters
     * @param pluginFileSystem
     * @param pluginId
     */
    public JobSeekerIdentityModulePluginManager(
            PluginFileSystem pluginFileSystem,
            UUID pluginId,
            JobSeekerIdentityManager jobSeekerIdentityManager,
            DeviceUserManager deviceUserManager,
            JobSeekerIdentitySubAppModulePluginRoot jobSeekerIdentitySubAppModulePluginRoot) {
        super(pluginFileSystem, pluginId);
        this.jobSeekerIdentityManager = jobSeekerIdentityManager;
        this.jobSeekerIdentitySubAppModulePluginRoot = jobSeekerIdentitySubAppModulePluginRoot;
        this.deviceUserManager = deviceUserManager;
    }

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
    @Override
    public JobSeeker createNewIdentity(
            String alias,
            byte[] imageProfile,
            ExposureLevel exposureLevel,
            long accuracy,
            Frequency frequency,
            JobTitle title)
            throws CantCreateJobPlatformIdentityException,
            JobPlatformIdentityAlreadyExistsException {
        try{
            //We get the logged device user from here
            DeviceUser deviceUser = deviceUserManager.getLoggedInDeviceUser();
            return this.jobSeekerIdentityManager.createNewIdentity(
                    alias,
                    deviceUser,
                    imageProfile,
                    exposureLevel,
                    accuracy,
                    frequency,
                    title);
        } catch (CantGetLoggedInDeviceUserException e) {
            jobSeekerIdentitySubAppModulePluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantCreateJobPlatformIdentityException(
                    e,
                    "Creating identity from module",
                    "There is a problem getting the logged device user");
        }

    }

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
    @Override
    public JobSeeker updateIdentity(
            String alias,
            String publicKey,
            byte[] imageProfile,
            long accuracy,
            Frequency frequency,
            ExposureLevel exposureLevel,
            JobTitle title)
            throws CantUpdateJobPlatformIdentityException,
            JobPlatformIdentityAlreadyExistsException {
        return this.jobSeekerIdentityManager.updateIdentity(
                alias,
                publicKey,
                imageProfile,
                accuracy,
                frequency,
                exposureLevel,
                title);
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
            IdentityNotFoundException,
            CantExposeIdentityException {
        this.jobSeekerIdentityManager.publishIdentity(publicKey);
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
        this.jobSeekerIdentityManager.hideIdentity(publicKey);
    }

    /**
     * This method returns an identities list from current device user
     * @return
     * @throws CantListJobsPlatformIdentitiesException
     */
    @Override
    public List<JobSeeker> getIdentitiesFromCurrentDeviceUser()
            throws CantListJobsPlatformIdentitiesException {
        return this.jobSeekerIdentityManager.getIdentitiesFromCurrentDeviceUser();
    }

    /**
     * This method returns true if exists any identity in the device
     * @return
     * @throws CantListJobsPlatformIdentitiesException
     */
    @Override
    public boolean hasIdentity() throws CantListJobsPlatformIdentitiesException {
        return this.jobSeekerIdentityManager.hasIdentity();
    }

    @Override
    public ActiveActorIdentityInformation getSelectedActorIdentity()
            throws CantGetSelectedActorIdentityException,
            ActorIdentityNotSelectedException {
        //Not implemented in this version
        return null;
    }

    @Override
    public void createIdentity(String name, String phrase, byte[] profile_img) throws Exception {
        //We got an specialized method for creation of this identity
    }

    @Override
    public void setAppPublicKey(String publicKey) {
        //Not implemented in this version
    }

    @Override
    public int[] getMenuNotifications() {
        //Returns a default value in this version
        return new int[0];
    }
}
