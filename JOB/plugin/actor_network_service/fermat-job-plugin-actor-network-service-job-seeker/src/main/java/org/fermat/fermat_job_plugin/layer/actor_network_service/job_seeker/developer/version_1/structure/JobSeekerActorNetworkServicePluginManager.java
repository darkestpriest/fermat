package org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;

import org.fermat.fermat_job_api.all_definition.enums.RequestType;
import org.fermat.fermat_job_api.all_definition.exceptions.CantAcceptConnectionRequestException;
import org.fermat.fermat_job_api.all_definition.exceptions.CantConfirmException;
import org.fermat.fermat_job_api.all_definition.exceptions.CantExposeIdentitiesException;
import org.fermat.fermat_job_api.all_definition.exceptions.CantExposeIdentityException;
import org.fermat.fermat_job_api.all_definition.exceptions.CantListPendingConnectionRequestsException;
import org.fermat.fermat_job_api.all_definition.exceptions.ConnectionRequestNotFoundException;
import org.fermat.fermat_job_api.all_definition.interfaces.Resume;
import org.fermat.fermat_job_api.layer.actor_network_service.common.JobActorConnectionRequest;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantAnswerResumeRequestException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantListPendingResumeRequestsException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantRequestResumeException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.ResumeRequestNotFoundException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces.JobSeekerExtraData;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces.JobSeekerManager;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces.JobSeekerSearch;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.utils.JobSeekerExposingData;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 13/06/16.
 */
public class JobSeekerActorNetworkServicePluginManager implements JobSeekerManager {
    @Override
    public void exposeIdentity(JobSeekerExposingData jobSeekerExposingData) throws CantExposeIdentityException {

    }

    @Override
    public void updateIdentity(JobSeekerExposingData jobSeekerExposingData) throws CantExposeIdentityException {

    }

    @Override
    public void exposeIdentities(Collection<JobSeekerExposingData> jobSeekerExposingDataList) throws CantExposeIdentitiesException {

    }

    @Override
    public JobSeekerSearch getSearch() {
        return null;
    }

    @Override
    public void acceptConnection(UUID requestId) throws CantAcceptConnectionRequestException, ConnectionRequestNotFoundException {

    }

    @Override
    public List<JobActorConnectionRequest> listPendingConnectionNews(Actors actorType) throws CantListPendingConnectionRequestsException {
        return null;
    }

    @Override
    public List<JobActorConnectionRequest> listPendingConnectionUpdates() throws CantListPendingConnectionRequestsException {
        return null;
    }

    @Override
    public JobSeekerExtraData<Resume> requestResume(String requesterPublicKey, Actors requesterActorType, String jobSeekerPublicKey) throws CantRequestResumeException {
        return null;
    }

    @Override
    public List<JobSeekerExtraData<Resume>> listPendingResumeRequests(RequestType requestType) throws CantListPendingResumeRequestsException {
        return null;
    }

    @Override
    public void answerQuotesRequest(UUID requestId, long updateTime, List<Resume> quotes) throws CantAnswerResumeRequestException, ResumeRequestNotFoundException {

    }

    @Override
    public void confirmResumeRequest(UUID requestId) throws CantConfirmException, ResumeRequestNotFoundException {

    }

    @Override
    public void confirm(UUID requestId) throws CantConfirmException, ConnectionRequestNotFoundException {

    }
}
