package org.fermat.fermat_job_plugin.layer.sub_app_module.job_seeker_community.developer.version_1.util;

import com.bitdubai.fermat_api.layer.all_definition.location_system.DeviceLocation;

import org.fermat.fermat_job_api.all_definition.enums.JobTitle;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantListJobActorsException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces.JobSeekerManager;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces.JobSeekerSearch;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.utils.JobSeekerExposingData;
import org.fermat.fermat_job_api.layer.sub_app_module.exceptions.CantGetJobActorResultException;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunityInformation;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunitySearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 23/06/16.
 */
public class JobSeekerCommunityModuleSearch implements JobSeekerCommunitySearch {

    private final JobSeekerManager jobSeekerManager;

    /**
     * Default constructor with parameters.
     * @param jobSeekerManager
     */
    public JobSeekerCommunityModuleSearch(JobSeekerManager jobSeekerManager) {
        this.jobSeekerManager = jobSeekerManager;
    }

    @Override
    public List<JobSeekerCommunityInformation> getResultJobTitle(JobTitle jobTitle)
            throws CantGetJobActorResultException {
        return null;
    }

    @Override
    public void addAlias(String alias) {
        //TODO: to implement
    }

    /**
     * This method returns the JobSeekerCommunityInformation list.
     * @return
     * @throws CantGetJobActorResultException
     */
    @Override
    public List<JobSeekerCommunityInformation> getResult() throws CantGetJobActorResultException {
        try{
            JobSeekerSearch jobSeekerSearch = jobSeekerManager.getSearch();
            final List<JobSeekerExposingData> jobSeekerExposingDataList =
                    jobSeekerSearch.getResult();
            final List<JobSeekerCommunityInformation> jobSeekerCommunityInformationList =
                    new ArrayList<>();
            for(JobSeekerExposingData jobSeekerExposingData : jobSeekerExposingDataList){
                jobSeekerCommunityInformationList.add(
                        new JobSeekerCommunityPluginInformation(jobSeekerExposingData));
            }
            return jobSeekerCommunityInformationList;
        } catch (CantListJobActorsException e) {
            throw new CantGetJobActorResultException(
                    e,
                    "Getting search result",
                    "Cannot list connected actors");
        }
    }

    /**
     * This method returns the JobSeekerCommunityInformation with search criteria.
     * @param publicKey
     * @param deviceLocation
     * @param distance
     * @param alias
     * @param offSet
     * @param max
     * @return
     * @throws CantGetJobActorResultException
     */
    @Override
    public List<JobSeekerCommunityInformation> getResult(
            String publicKey,
            DeviceLocation deviceLocation,
            double distance,
            String alias,
            Integer offSet,
            Integer max) throws CantGetJobActorResultException {
        try{
            JobSeekerSearch jobSeekerSearch = jobSeekerManager.getSearch();
            List<JobSeekerExposingData> jobSeekerExposingDataList=
                    jobSeekerSearch.getResult(
                            publicKey,
                            deviceLocation,
                            distance,
                            alias,
                            offSet,
                            max);
            final List<JobSeekerCommunityInformation> jobSeekerCommunityInformationList
                    = new ArrayList<>();
            for(JobSeekerExposingData jobSeekerExposingData : jobSeekerExposingDataList)
                jobSeekerCommunityInformationList.add(
                        new JobSeekerCommunityPluginInformation(jobSeekerExposingData));
            return jobSeekerCommunityInformationList;
        } catch (CantListJobActorsException e) {
            throw new CantGetJobActorResultException(
                    e,
                    "Getting search result",
                    "Cannot list connected actors");
        }

    }

    @Override
    public List<JobSeekerCommunityInformation> getResultLocation(DeviceLocation deviceLocation) throws CantGetJobActorResultException {
        try{
            JobSeekerSearch jobSeekerSearch = jobSeekerManager.getSearch();
            List<JobSeekerExposingData> jobSeekerExposingDataList=
                    jobSeekerSearch.getResultLocation(
                            deviceLocation
                    );
            final List<JobSeekerCommunityInformation> jobSeekerCommunityInformationList
                    = new ArrayList<>();
            for(JobSeekerExposingData jobSeekerExposingData : jobSeekerExposingDataList)
                jobSeekerCommunityInformationList.add(
                        new JobSeekerCommunityPluginInformation(jobSeekerExposingData));
            return jobSeekerCommunityInformationList;
        } catch (CantListJobActorsException e) {
            throw new CantGetJobActorResultException(
                    e,
                    "Getting search result",
                    "Cannot list connected actors");
        }
    }

    @Override
    public List<JobSeekerCommunityInformation> getResultDistance(double distance) throws CantGetJobActorResultException {
        try{
            JobSeekerSearch jobSeekerSearch = jobSeekerManager.getSearch();
            List<JobSeekerExposingData> jobSeekerExposingDataList=
                    jobSeekerSearch.getResultDistance(
                            distance
                    );
            final List<JobSeekerCommunityInformation> jobSeekerCommunityInformationList
                    = new ArrayList<>();
            for(JobSeekerExposingData jobSeekerExposingData : jobSeekerExposingDataList)
                jobSeekerCommunityInformationList.add(
                        new JobSeekerCommunityPluginInformation(jobSeekerExposingData));
            return jobSeekerCommunityInformationList;
        } catch (CantListJobActorsException e) {
            throw new CantGetJobActorResultException(
                    e,
                    "Getting search result",
                    "Cannot list connected actors");
        }
    }

    @Override
    public List<JobSeekerCommunityInformation> getResultAlias(String alias) throws CantGetJobActorResultException {
        try{
            JobSeekerSearch jobSeekerSearch = jobSeekerManager.getSearch();
            List<JobSeekerExposingData> jobSeekerExposingDataList=
                    jobSeekerSearch.getResultAlias(
                            alias
                    );
            final List<JobSeekerCommunityInformation> jobSeekerCommunityInformationList
                    = new ArrayList<>();
            for(JobSeekerExposingData jobSeekerExposingData : jobSeekerExposingDataList)
                jobSeekerCommunityInformationList.add(
                        new JobSeekerCommunityPluginInformation(jobSeekerExposingData));
            return jobSeekerCommunityInformationList;
        } catch (CantListJobActorsException e) {
            throw new CantGetJobActorResultException(
                    e,
                    "Getting search result",
                    "Cannot list connected actors");
        }
    }

}
