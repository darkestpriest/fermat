package org.fermat.fermat_job_plugin.layer.sub_app_module.job_seeker_community.developer.version_1.structure;

import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;
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
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunityInformation;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunitySearch;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunitySubAppModuleManager;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.utils.JobSeekerCommunitySettings;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 23/06/16.
 */
public class JobSeekerCommunityModulePluginManager extends ModuleManagerImpl<JobSeekerCommunitySettings>
        implements JobSeekerCommunitySubAppModuleManager {

    public JobSeekerCommunityModulePluginManager(PluginFileSystem pluginFileSystem, UUID pluginId) {
        super(pluginFileSystem, pluginId);
    }

    @Override
    public List<JobSeekerCommunityInformation> listWorldJobActors(ActiveActorIdentityInformation selectedIdentity, int max, int offset) throws CantListJobActorsException {
        List<JobSeekerCommunityInformation> jobSeekerCommunityInformationList;
        return null;
    }

    @Override
    public List<ActiveActorIdentityInformation> listSelectableIdentities() throws CantListSelectableIdentitiesException {
        return null;
    }

    @Override
    public void setSelectedActorIdentity(ActiveActorIdentityInformation selectedIdentity) {

    }

    @Override
    public JobSeekerCommunitySearch getJobActorSearch() {
        return null;
    }

    @Override
    public JobSeekerCommunitySearch searchConnectedJobActor(ActiveActorIdentityInformation selectedIdentity) {
        return null;
    }

    @Override
    public void requestConnectionToJobActor(ActiveActorIdentityInformation selectedIdentity, JobSeekerCommunityInformation communityInformation) throws CantRequestConnectionException, ActorConnectionAlreadyRequestedException, ActorTypeNotSupportedException {

    }

    @Override
    public List<JobSeekerCommunityInformation> listAllConnectedJobActors(ActiveActorIdentityInformation selectedIdentity, int max, int offset) throws CantListJobActorsException {
        return null;
    }

    @Override
    public List<JobSeekerCommunityInformation> listJobActorPendingRemoteAction(ActiveActorIdentityInformation selectedIdentity, int max, int offset) throws CantListJobActorsException {
        return null;
    }

    @Override
    public ConnectionState getActorConnectionState(String publicKey) throws CantValidateConnectionStateException {
        return null;
    }

    @Override
    public HashMap<String, Country> getCountryList() throws CantConnectWithExternalAPIException, CantCreateBackupFileException, CantCreateCountriesListException {
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

    @Override
    public ActiveActorIdentityInformation getSelectedActorIdentity() throws CantGetSelectedActorIdentityException, ActorIdentityNotSelectedException {
        return null;
    }

    @Override
    public void createIdentity(String name, String phrase, byte[] profile_img) throws Exception {

    }

    @Override
    public void setAppPublicKey(String publicKey) {

    }

    @Override
    public int[] getMenuNotifications() {
        return new int[0];
    }
}
