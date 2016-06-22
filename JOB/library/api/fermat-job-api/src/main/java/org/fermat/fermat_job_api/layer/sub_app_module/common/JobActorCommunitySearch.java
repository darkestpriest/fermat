package org.fermat.fermat_job_api.layer.sub_app_module.common;

import com.bitdubai.fermat_api.layer.all_definition.location_system.DeviceLocation;

import org.fermat.fermat_job_api.layer.sub_app_module.exceptions.CantGetJobActorResultException;

import java.util.List;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 22/06/16.
 */
public interface JobActorCommunitySearch<T extends JobActorCommunityInformation> {

    /**
     * This method sets the alias.
     * @param alias
     */
    void addAlias(String alias);

    /**
     * This method return the search result without filters.
     * @return
     * @throws CantGetJobActorResultException
     */
    List<T> getResult() throws CantGetJobActorResultException;

    /**
     * This method return the search result.
     * @param publicKey
     * @param deviceLocation
     * @param distance
     * @param alias
     * @param offSet
     * @param max
     * @return
     * @throws CantGetJobActorResultException
     */
    List<T> getResult(
            String publicKey,
            DeviceLocation deviceLocation,
            double distance,
            String alias,
            Integer offSet,
            Integer max) throws CantGetJobActorResultException;

    /**
     * This method returns the search result by a given device location.
     * @param deviceLocation
     * @return
     * @throws CantGetJobActorResultException
     */
    List<T> getResultLocation(DeviceLocation deviceLocation) throws CantGetJobActorResultException;

    /**
     * This method returns the search result by a given distance.
     * @param distance
     * @return
     * @throws CantGetJobActorResultException
     */
    List<T> getResultDistance(double distance) throws CantGetJobActorResultException;

    /**
     * This method returns the search by a given alias.
     * @param alias
     * @return
     * @throws CantGetJobActorResultException
     */
    List<T> getResultAlias(String alias) throws CantGetJobActorResultException;

}
