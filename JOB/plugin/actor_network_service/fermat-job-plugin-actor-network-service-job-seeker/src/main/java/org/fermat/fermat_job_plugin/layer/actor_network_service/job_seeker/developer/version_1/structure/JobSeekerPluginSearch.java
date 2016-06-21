package org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.exceptions.CantRequestProfileListException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.DiscoveryQueryParameters;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;

import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantListJobSeekersException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces.JobSeekerSearch;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.utils.JobSeekerExposingData;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.JobSeekerActorNetworkServicePluginRoot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 15/06/16.
 */
public class JobSeekerPluginSearch extends JobSeekerSearch {

    /**
     * This represents the plugin root.
     */
    JobSeekerActorNetworkServicePluginRoot pluginRoot;

    /**
     * Default constructor with parameters
     * @param pluginRoot
     */
    public JobSeekerPluginSearch(JobSeekerActorNetworkServicePluginRoot pluginRoot) {
        this.pluginRoot = pluginRoot;
    }

    /**
     * This method returns the result of a searching process.
     * @return
     * @throws CantListJobSeekersException
     */
    @Override
    public List<JobSeekerExposingData> getResult() throws CantListJobSeekersException {
        try {
            DiscoveryQueryParameters discoveryQueryParameters = new DiscoveryQueryParameters(
                    Actors.JOB_SEEKER.getCode(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    NetworkServiceType.UNDEFINED,
                    null,
                    NetworkServiceType.JOB_SEEKER
            );
            final List<ActorProfile> list = pluginRoot.getConnection().listRegisteredActorProfiles(
                    discoveryQueryParameters);
            final List<JobSeekerExposingData> cryptoBrokerExposingDataList = new ArrayList<>();
            for (final ActorProfile actorProfile : list) {
                cryptoBrokerExposingDataList.add(
                        new JobSeekerExposingData(
                                actorProfile.getIdentityPublicKey(),
                                actorProfile.getAlias(),
                                actorProfile.getPhoto(),
                                actorProfile.getLocation(),
                                actorProfile.getExtraData()
                                ));
            }
            return cryptoBrokerExposingDataList;
        } catch (final CantRequestProfileListException e) {
            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantListJobSeekersException(
                    e,
                    "Listing Job Seeker Actors",
                    "Problem trying to request list of registered components in communication layer.");
        } catch (final Exception e) {
            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantListJobSeekersException(
                    e,
                    "Listing Job Seeker Actors",
                    "Unhandled error.");
        }
    }

    @Override
    public List<JobSeekerExposingData> getResult(Integer max) throws CantListJobSeekersException {
        //TODO: to implement
        return null;
    }
}
