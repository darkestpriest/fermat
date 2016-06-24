package org.fermat.fermat_job_plugin.layer.sub_app_module.job_seeker_community.developer.version_1.structure;

import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.CantPersistSettingsException;
import com.bitdubai.fermat_api.layer.modules.ModuleManagerImpl;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.exceptions.ActorIdentityNotSelectedException;
import com.bitdubai.fermat_api.layer.modules.exceptions.CantGetSelectedActorIdentityException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_pip_api.layer.external_api.geolocation.exceptions.CantConnectWithExternalAPIException;
import com.bitdubai.fermat_pip_api.layer.external_api.geolocation.exceptions.CantCreateAddressException;
import com.bitdubai.fermat_pip_api.layer.external_api.geolocation.exceptions.CantCreateBackupFileException;
import com.bitdubai.fermat_pip_api.layer.external_api.geolocation.exceptions.CantCreateCountriesListException;
import com.bitdubai.fermat_pip_api.layer.external_api.geolocation.exceptions.CantCreateGeoRectangleException;
import com.bitdubai.fermat_pip_api.layer.external_api.geolocation.exceptions.CantGetCitiesListException;
import com.bitdubai.fermat_pip_api.layer.external_api.geolocation.exceptions.CantGetCountryDependenciesListException;
import com.bitdubai.fermat_pip_api.layer.external_api.geolocation.interfaces.Address;
import com.bitdubai.fermat_pip_api.layer.external_api.geolocation.interfaces.City;
import com.bitdubai.fermat_pip_api.layer.external_api.geolocation.interfaces.Country;
import com.bitdubai.fermat_pip_api.layer.external_api.geolocation.interfaces.CountryDependency;
import com.bitdubai.fermat_pip_api.layer.external_api.geolocation.interfaces.GeoRectangle;

import org.fermat.fermat_job_api.all_definition.exceptions.ActorConnectionAlreadyRequestedException;
import org.fermat.fermat_job_api.all_definition.exceptions.ActorTypeNotSupportedException;
import org.fermat.fermat_job_api.all_definition.exceptions.CantListSelectableIdentitiesException;
import org.fermat.fermat_job_api.all_definition.exceptions.CantValidateConnectionStateException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantListJobActorsException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantRequestConnectionException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces.JobSeekerManager;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantListJobsPlatformIdentitiesException;
import org.fermat.fermat_job_api.layer.identity.employer.interfaces.Employer;
import org.fermat.fermat_job_api.layer.identity.employer.interfaces.EmployerIdentityManager;
import org.fermat.fermat_job_api.layer.identity.job_seeker.interfaces.JobSeeker;
import org.fermat.fermat_job_api.layer.identity.job_seeker.interfaces.JobSeekerIdentityManager;
import org.fermat.fermat_job_api.layer.sub_app_module.common.JobActorCommunityInformation;
import org.fermat.fermat_job_api.layer.sub_app_module.exceptions.CantGetJobActorResultException;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.EmployerCommunityInformation;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunityInformation;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunitySearch;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunitySelectableIdentity;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunitySubAppModuleManager;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.utils.JobSeekerCommunitySettings;
import org.fermat.fermat_job_plugin.layer.sub_app_module.job_seeker_community.developer.version_1.JobSeekerCommunitySubAppModulePluginRoot;
import org.fermat.fermat_job_plugin.layer.sub_app_module.job_seeker_community.developer.version_1.util.JobSeekerCommunityModuleSearch;
import org.fermat.fermat_job_plugin.layer.sub_app_module.job_seeker_community.developer.version_1.util.JobSeekerCommunityModuleSelectableIdentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 23/06/16.
 */
public class JobSeekerCommunityModulePluginManager extends
        ModuleManagerImpl<JobSeekerCommunitySettings>
        implements JobSeekerCommunitySubAppModuleManager, Serializable {

    /**
     * Represents the JobSeeker Actor Network Service Manager
     */
    private final JobSeekerManager jobSeekerManager;

    /**
     * Represents the plugin root
     */
    private final JobSeekerCommunitySubAppModulePluginRoot jobSeekerCommunitySubAppModulePluginRoot;

    /**
     * Represents the Employer identity manager.
     */
    private final EmployerIdentityManager employerIdentityManager;

    /**
     * Represents the Job Seeker identity manager.
     */
    private final JobSeekerIdentityManager jobSeekerIdentityManager;

    private String subAppPublicKey;

    public JobSeekerCommunityModulePluginManager(
            PluginFileSystem pluginFileSystem,
            UUID pluginId,
            JobSeekerManager jobSeekerManager,
            JobSeekerCommunitySubAppModulePluginRoot jobSeekerCommunitySubAppModulePluginRoot,
            EmployerIdentityManager employerIdentityManager,
            JobSeekerIdentityManager jobSeekerIdentityManager) {
        super(pluginFileSystem, pluginId);
        this.jobSeekerManager = jobSeekerManager;
        this.jobSeekerCommunitySubAppModulePluginRoot = jobSeekerCommunitySubAppModulePluginRoot;
        this.employerIdentityManager = employerIdentityManager;
        this.jobSeekerIdentityManager = jobSeekerIdentityManager;
    }

    /**
     * This method returns all the JobSeekers that can be online in Fermat network.
     * @param selectedIdentity
     * @param max
     * @param offset
     * @return
     * @throws CantListJobActorsException
     */
    @Override
    public List<JobSeekerCommunityInformation> listWorldJobActors(
            JobSeekerCommunitySelectableIdentity selectedIdentity,
            int max,
            int offset) throws CantListJobActorsException {
        try{
            JobSeekerCommunitySearch jobSeekerCommunitySearch = getJobActorSearch();
            //Job Seekers online
            List<JobSeekerCommunityInformation> jobSeekerCommunityInformationList =
                    jobSeekerCommunitySearch.getResult();
            /**
             * TODO: When the actor layer is done, we must implement to get the actor connected list and
             * include this actors in this list.
             */
            return jobSeekerCommunityInformationList;
        } catch (CantGetJobActorResultException e) {
            jobSeekerCommunitySubAppModulePluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantListJobActorsException(
                    e,
                    "Listing the job seekers online",
                    "Cannot list job actors");
        } catch (Exception e){
            //Unexpected error, I'll report and return an empty list.
            jobSeekerCommunitySubAppModulePluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            return new ArrayList<JobSeekerCommunityInformation>();
        }

    }

    /**
     * This method returns all the selectable identities created in the device.
     * @return
     * @throws CantListSelectableIdentitiesException
     */
    @Override
    public List<JobSeekerCommunitySelectableIdentity> listSelectableIdentities()
            throws CantListSelectableIdentitiesException {
        try{
            List<JobSeekerCommunitySelectableIdentity> jobSeekerCommunitySelectableIdentityList
                    = new ArrayList<>();
            JobSeekerCommunitySelectableIdentity jobSeekerCommunitySelectableIdentity;
            //Employer selectable list
            try{
                List<Employer> employerList =
                        employerIdentityManager.getIdentitiesFromCurrentDeviceUser();
                for(Employer employer : employerList){
                    jobSeekerCommunitySelectableIdentity =
                            new JobSeekerCommunityModuleSelectableIdentity(employer);
                    jobSeekerCommunitySelectableIdentityList.add(jobSeekerCommunitySelectableIdentity);
                }
            } catch (CantListJobsPlatformIdentitiesException e) {
                //I'll report this exception, but, I'll try to get the job seeker identity list
                jobSeekerCommunitySubAppModulePluginRoot.reportError(
                        UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                        e);
            }
            //Job seeker selectable list
            try{
                List<JobSeeker> jobSeekerList =
                        jobSeekerIdentityManager.getIdentitiesFromCurrentDeviceUser();
                for(JobSeeker jobSeeker : jobSeekerList){
                    jobSeekerCommunitySelectableIdentity =
                            new JobSeekerCommunityModuleSelectableIdentity(jobSeeker);
                    jobSeekerCommunitySelectableIdentityList.add(jobSeekerCommunitySelectableIdentity);
                }
            } catch (CantListJobsPlatformIdentitiesException e) {
                //I'll report this exception, but, I'll try to return the previous Employer identity list.
                jobSeekerCommunitySubAppModulePluginRoot.reportError(
                        UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                        e);
            }
            return jobSeekerCommunitySelectableIdentityList;
        } catch (Exception e){
            //Fatal error, I cannot get the list.
            jobSeekerCommunitySubAppModulePluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantListSelectableIdentitiesException(
                    e,
                    "Getting the selectable identity list",
                    "Unexpected exception");
        }

    }

    /**
     * This method sets the selected identity.
     * @param selectedIdentity
     */
    @Override
    public void setSelectedActorIdentity(JobSeekerCommunitySelectableIdentity selectedIdentity) {
        //Check if the sub app public key is set.
        if(this.subAppPublicKey==null|| this.subAppPublicKey.isEmpty()){
            //In theory, it cannot happens, but, I'll report and return void.
            jobSeekerCommunitySubAppModulePluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    new Exception("The sub app is null or empty"));
            return;
        }
        //Try to get appSettings
        JobSeekerCommunitySettings appSettings;
        try {
            appSettings = loadAndGetSettings(this.subAppPublicKey);
        }catch (Exception e){
            appSettings = null;
        }
        //If appSettings exist, save identity
        if(appSettings != null){
            if(selectedIdentity.getPublicKey() != null)
                appSettings.setLastSelectedIdentityPublicKey(selectedIdentity.getPublicKey());
            if(selectedIdentity.getActorType() != null)
                appSettings.setLastSelectedActorType(selectedIdentity.getActorType());
            try {
                persistSettings(this.subAppPublicKey, appSettings);
            }catch (CantPersistSettingsException e){
                jobSeekerCommunitySubAppModulePluginRoot.reportError(
                        UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                        e);
            }
        }
    }

    @Override
    public JobSeekerCommunitySearch getJobActorSearch() {
        return new JobSeekerCommunityModuleSearch(jobSeekerManager);
    }

    @Override
    public JobSeekerCommunitySearch searchConnectedJobActor(JobSeekerCommunitySelectableIdentity selectedIdentity) {
        //TODO: To implement with Actor layer
        return null;
    }

    @Override
    public void requestConnectionToJobActor(JobSeekerCommunitySelectableIdentity selectedIdentity, JobSeekerCommunityInformation communityInformation) throws CantRequestConnectionException, ActorConnectionAlreadyRequestedException, ActorTypeNotSupportedException {
        //TODO: To implement with Actor layer
    }

    @Override
    public List<JobActorCommunityInformation> listAllConnectedJobActors(JobSeekerCommunitySelectableIdentity selectedIdentity, int max, int offset) throws CantListJobActorsException {
        //TODO: To implement with Actor layer
        return null;
    }

    @Override
    public List<JobSeekerCommunityInformation> listAllConnectedJobSeekers(JobSeekerCommunitySelectableIdentity selectedIdentity, int max, int offset) throws CantListJobActorsException {
        //TODO: To implement with Actor layer
        return null;
    }

    @Override
    public List<EmployerCommunityInformation> listAllConnectedEmployers(JobSeekerCommunitySelectableIdentity selectedIdentity, int max, int offset) throws CantListJobActorsException {
        //TODO: To implement with Actor layer
        return null;
    }

    @Override
    public List<JobSeekerCommunityInformation> listJobActorPendingRemoteAction(JobSeekerCommunitySelectableIdentity selectedIdentity, int max, int offset) throws CantListJobActorsException {
        //TODO: To implement with Actor layer
        return null;
    }

    @Override
    public ConnectionState getActorConnectionState(String publicKey) throws CantValidateConnectionStateException {
        //TODO: To implement with Actor layer
        return null;
    }

    @Override
    public HashMap<String, Country> getCountryList() throws CantConnectWithExternalAPIException, CantCreateBackupFileException, CantCreateCountriesListException {
        //TODO: To implement with Actor layer
        return null;
    }

    @Override
    public List<CountryDependency> getCountryDependencies(String countryCode) throws CantGetCountryDependenciesListException, CantConnectWithExternalAPIException, CantCreateBackupFileException {
        return null;
    }

    @Override
    public List<City> getCitiesByCountryCode(String countryCode) throws CantGetCitiesListException {
        return null;
    }

    @Override
    public List<City> getCitiesByCountryCodeAndDependencyName(String countryName, String dependencyName) throws CantGetCitiesListException, CantCreateCountriesListException {
        return null;
    }

    @Override
    public GeoRectangle getGeoRectangleByLocation(String location) throws CantCreateGeoRectangleException {
        return null;
    }

    @Override
    public Address getAddressByCoordinate(float latitude, float longitude) throws CantCreateAddressException {
        return null;
    }

    @Override
    public GeoRectangle getRandomGeoLocation() throws CantCreateGeoRectangleException {
        return null;
    }

    /**
     * This method returns the selected actor identity.
     * @return
     * @throws CantGetSelectedActorIdentityException
     * @throws ActorIdentityNotSelectedException
     */
    @Override
    public JobSeekerCommunitySelectableIdentity getSelectedActorIdentity()
            throws CantGetSelectedActorIdentityException,
            ActorIdentityNotSelectedException {
        //Check if the sub app public key is set.
        if(this.subAppPublicKey==null|| this.subAppPublicKey.isEmpty()){
            //In theory, it cannot happens, but, I'll report and thrown an exception.
            String message = "The sub app is null or empty";
            jobSeekerCommunitySubAppModulePluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    new Exception(message));
            throw new CantGetSelectedActorIdentityException(
                    null,
                    "Getting the selected actor identity",
                    message);
        }
        //Try to get appSettings
        JobSeekerCommunitySettings appSettings;
        try {
            appSettings = loadAndGetSettings(this.subAppPublicKey);
        }catch (Exception e){
            return null;
        }
        //Get all job seeker identities on local device
        List<JobSeeker> jobSeekerList = new ArrayList<>();
        try{
            jobSeekerList = jobSeekerIdentityManager.getIdentitiesFromCurrentDeviceUser();
        } catch (CantListJobsPlatformIdentitiesException e) {
            jobSeekerCommunitySubAppModulePluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
        }
        //Get all employer identities on local device
        List<Employer> employerList = new ArrayList<>();
        try{
            employerList = employerIdentityManager.getIdentitiesFromCurrentDeviceUser();
        } catch(CantListJobsPlatformIdentitiesException e) {
            jobSeekerCommunitySubAppModulePluginRoot.reportError(
                UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                e); }

        //No registered users in device
        if(jobSeekerList.size() + employerList.size() == 0)
            throw new CantGetSelectedActorIdentityException(
                    "CAN'T GET SELECTED ACTOR IDENTITY EXCEPTION",
                    null,
                    "Getting the Selected Actor",
                    "There's no identities created");

        //If appSettings exists, get its selectedActorIdentityPublicKey property
        if(appSettings != null){
            String lastSelectedIdentityPublicKey = appSettings.getLastSelectedIdentityPublicKey();
            Actors lastSelectedActorType = appSettings.getLastSelectedActorType();
            if (lastSelectedIdentityPublicKey != null && lastSelectedActorType != null) {
                JobSeekerCommunityModuleSelectableIdentity selectedIdentity = null;
                if(lastSelectedActorType == Actors.EMPLOYER){
                    for(Employer i : employerList) {
                        if(i.getPublicKey().equals(lastSelectedIdentityPublicKey))
                            selectedIdentity = new JobSeekerCommunityModuleSelectableIdentity(
                                    i.getPublicKey(),
                                    Actors.EMPLOYER,
                                    i.getAlias(),
                                    i.getProfileImage());
                    }
                }
                else if( lastSelectedActorType == Actors.JOB_SEEKER){
                    for(JobSeeker i : jobSeekerList) {
                        if(i.getPublicKey().equals(lastSelectedIdentityPublicKey))
                            selectedIdentity = new JobSeekerCommunityModuleSelectableIdentity(
                                    i.getPublicKey(),
                                    Actors.JOB_SEEKER,
                                    i.getAlias(),
                                    i.getProfileImage());
                    }
                }

                if(selectedIdentity == null)
                    throw new ActorIdentityNotSelectedException(
                            "CAN'T GET SELECTED ACTOR IDENTITY EXCEPTION",
                            null,
                            "Getting the Selected Actor",
                            "The selected identity is null");

                return selectedIdentity;
            } else
                throw new ActorIdentityNotSelectedException(
                        "CAN'T GET SELECTED ACTOR IDENTITY EXCEPTION",
                        null,
                        "Getting the Selected Actor",
                        "The last identity selected is null");
        }

        return null;
    }

    @Override
    public void createIdentity(String name, String phrase, byte[] profile_img) throws Exception {
        //TODO: to implement
    }

    @Override
    public void setAppPublicKey(String publicKey) {
        this.subAppPublicKey = publicKey;
    }

    @Override
    public int[] getMenuNotifications() {
        //TODO: to improve
        return new int[0];
    }
}
