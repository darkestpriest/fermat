package org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.location_system.DeviceLocation;

import org.fermat.fermat_job_api.all_definition.enums.JobTitle;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantListJobActorsException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.utils.JobSeekerExposingData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/06/16.
 */
public abstract class JobSeekerSearch {

    protected List<String> aliasList;

    /**
     * Through the method <code>addAlias</code> we can add alias of the Job Seeker to search.
     *
     * @param alias of the Job seeker.
     */
    public final void addAlias(final String alias) {
        if(this.aliasList == null)
            this.aliasList = new ArrayList<>();
        this.aliasList.add(alias);
    }

    /**
     * Through the method <code>getResult</code> we can get the results of the search,
     * Like we're not setting max and offset we will return all the job seeker that match
     * with the parameters set.
     *
     * @return a list of job seeker with their information.
     *
     * @throws CantListJobActorsException  if something goes wrong.
     */
    public abstract List<JobSeekerExposingData> getResult() throws CantListJobActorsException;

    public abstract List<JobSeekerExposingData> getResult(
            String publicKey,
            DeviceLocation deviceLocation,
            double distance,
            String alias,
            Integer offSet,
            Integer max)
            throws CantListJobActorsException;

    /**
     * Through the method <code>getResult</code> we can get the results of the search,
     * Like we're not setting max and offset we will return all the job seeker that match
     * with the parameters set.
     * @param deviceLocation
     * @return
     * @throws CantListJobActorsException
     */
    public abstract List<JobSeekerExposingData> getResultLocation(
            DeviceLocation deviceLocation)
            throws CantListJobActorsException;

    /**
     * Through the method <code>getResult</code> we can get the results of the search,
     * Like we're not setting max and offset we will return all the job seeker that match
     * with the parameters set.
     * @param distance
     * @return
     * @throws CantListJobActorsException
     */
    public abstract List<JobSeekerExposingData> getResultDistance(
            double distance)
            throws CantListJobActorsException;

    /**
     * Through the method <code>getResult</code> we can get the results of the search,
     * Like we're not setting max and offset we will return all the job seeker that match
     * with the parameters set.
     * @param alias
     * @return
     * @throws CantListJobActorsException
     */
    public abstract List<JobSeekerExposingData> getResultAlias(
            String alias)
            throws CantListJobActorsException;

    /**
     * Through the method <code>getResult</code> we can get the results of the search,
     * Like we're not setting max and offset we will return all the job seeker that match
     * with the parameters set.
     * @param jobTitle
     * @return
     * @throws CantListJobActorsException
     */
    public abstract List<JobSeekerExposingData> getResultJobTitle(
            JobTitle jobTitle)
            throws CantListJobActorsException;

    /**
     * Through the method <code>getResult</code> we can get the results of the search,
     * filtered by the parameters set.
     * We'll receive at most the quantity of @max set. If null by default the max will be 100.
     *
     * @param max   maximum quantity of results expected.
     *
     * @return a list of crypto brokers with their information.
     *
     * @throws CantListJobActorsException  if something goes wrong.
     */
    public abstract List<JobSeekerExposingData> getResult(
            final Integer max) throws CantListJobActorsException;

    /**
     * Through the method <code>resetFilters</code> you can reset the filters set,
     */
    public final void resetFilters() {
        this.aliasList = null;
    }
}
