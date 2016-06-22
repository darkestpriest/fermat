package org.fermat.fermat_job_api.layer.sub_app_module.common;

import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;
import com.bitdubai.fermat_api.layer.modules.ModuleSettingsImpl;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.interfaces.ModuleManager;
import com.bitdubai.fermat_pip_api.all_definition.sub_app_module.settings.interfaces.SubAppSettings;
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

import org.fermat.fermat_job_api.all_definition.exceptions.ActorTypeNotSupportedException;
import org.fermat.fermat_job_api.all_definition.exceptions.CantListSelectableIdentitiesException;
import org.fermat.fermat_job_api.all_definition.exceptions.ActorConnectionAlreadyRequestedException;
import org.fermat.fermat_job_api.all_definition.exceptions.CantValidateConnectionStateException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantListJobActorsException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantRequestConnectionException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 22/06/16.
 */
public interface JobCommunitySubAppModuleManager<
                K extends SubAppSettings,
                T extends ActiveActorIdentityInformation,
                M extends JobActorCommunityInformation,
                Z extends JobActorCommunitySearch
        >
        extends ModuleManager<K , T>,
        ModuleSettingsImpl<K>, Serializable {

    /**
     * This method returns the job actor available.
     * @param selectedIdentity
     * @param max
     * @param offset
     * @return
     * @throws CantListJobActorsException
     */
    List<M> listWorldJobActors(T selectedIdentity, final int max, final int offset)
            throws CantListJobActorsException;

    /**
     * This method returns the selectable identities in the device.
     * @return
     * @throws CantListSelectableIdentitiesException
     */
    List<T> listSelectableIdentities() throws CantListSelectableIdentitiesException;

    /**
     * This method sets the selected actor identity.
     * @param selectedIdentity
     */
    void setSelectedActorIdentity(T selectedIdentity);

    /**
     * This method returns the job actor search
     * @return
     */
    Z getJobActorSearch();

    /**
     * This method returns the connected Job Actors by a given selected identity.
     * @param selectedIdentity
     * @return
     */
    Z searchConnectedJobActor(T selectedIdentity);

    /**
     * This method request connection with remote actor.
     * @param selectedIdentity
     * @param communityInformation
     * @throws CantRequestConnectionException
     * @throws ActorConnectionAlreadyRequestedException
     * @throws ActorTypeNotSupportedException
     */
    void requestConnectionToJobActor(T selectedIdentity, M communityInformation)
            throws CantRequestConnectionException,
            ActorConnectionAlreadyRequestedException,
            ActorTypeNotSupportedException;

    /**
     * This method returns all the connected job actors
     * @param selectedIdentity
     * @param max
     * @param offset
     * @return
     * @throws CantListJobActorsException
     */
    List<M> listAllConnectedJobActors(
            final T selectedIdentity,
            final int max,
            final int offset)
            throws CantListJobActorsException;

     /**
     * This method returns all the job actors with pending remotely action.
     * @param selectedIdentity
     * @param max
     * @param offset
     * @return
     * @throws CantListJobActorsException
     */
    List<M> listJobActorPendingRemoteAction(
            final T selectedIdentity,
            final int max,
            final int offset) throws CantListJobActorsException;

    /**
     * This method returns the actor connection state by a given actor public key
     * @param publicKey
     * @return
     * @throws CantValidateConnectionStateException
     */
    ConnectionState getActorConnectionState(String publicKey)
            throws CantValidateConnectionStateException;

    /**
     * Geolocation methods
     * Todo: after the android implementation we need to study what method we need
     */

    /**
     * This method returns tha country list.
     * @return
     * @throws CantConnectWithExternalAPIException
     * @throws CantCreateBackupFileException
     * @throws CantCreateCountriesListException
     */
    HashMap<String, Country> getCountryList() throws CantConnectWithExternalAPIException,
            CantCreateBackupFileException,
            CantCreateCountriesListException;

    /**
     * This method returns the country dependency, like states or counties, list by a given country code
     * @param countryCode
     * @return
     * @throws CantGetCountryDependenciesListException
     * @throws CantConnectWithExternalAPIException
     * @throws CantCreateBackupFileException
     */
    List<CountryDependency> getCountryDependencies(String countryCode)
            throws CantGetCountryDependenciesListException,
            CantConnectWithExternalAPIException,
            CantCreateBackupFileException;

    /**
     * This method returns the cities by a given country code
     * @param countryCode
     * @return
     * @throws CantGetCitiesListException
     */
    List<City> getCitiesByCountryCode(String countryCode)
            throws CantGetCitiesListException;

    /**
     * This method returns the cities list
     * @param countryName
     * @param dependencyName
     * @return
     * @throws CantGetCitiesListException
     * @throws CantCreateCountriesListException
     */
    List<City> getCitiesByCountryCodeAndDependencyName(
            String countryName,
            String dependencyName)
            throws CantGetCitiesListException,
            CantCreateCountriesListException;

    /**
     * This method returns the geoRectangle by a given location
     * @param location
     * @return
     * @throws CantCreateGeoRectangleException
     */
    GeoRectangle getGeoRectangleByLocation(String location)
            throws CantCreateGeoRectangleException;

    /**
     * This method returns an address by a given coordinate.
     * @param latitude
     * @param longitude
     * @return
     * @throws CantCreateAddressException
     */
    Address getAddressByCoordinate(float latitude, float longitude)
            throws CantCreateAddressException;

    /**
     * This method returns a random GeoRectangle.
     * @return
     * @throws CantCreateGeoRectangleException
     */
    GeoRectangle getRandomGeoLocation() throws CantCreateGeoRectangleException;
}
