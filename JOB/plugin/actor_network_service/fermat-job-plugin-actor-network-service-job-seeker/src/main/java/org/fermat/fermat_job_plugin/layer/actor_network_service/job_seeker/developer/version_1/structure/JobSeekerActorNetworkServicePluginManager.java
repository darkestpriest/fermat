package org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.exceptions.ActorAlreadyRegisteredException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.exceptions.CantRegisterActorException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.exceptions.CantSendMessageException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;

import org.fermat.fermat_job_api.all_definition.enums.ConnectionRequestAction;
import org.fermat.fermat_job_api.all_definition.enums.ProtocolState;
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
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantRequestConnectionException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantRequestResumeException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.ResumeRequestNotFoundException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces.JobSeekerExtraData;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces.JobSeekerManager;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces.JobSeekerSearch;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.utils.JobSeekerConnectionInformation;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.utils.JobSeekerExposingData;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.JobSeekerActorNetworkServicePluginRoot;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.database.JobSeekerActorNetworkServiceDao;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.messages.InformationMessage;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.messages.RequestMessage;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 13/06/16.
 */
public class JobSeekerActorNetworkServicePluginManager implements JobSeekerManager {

    /**
     * This represents the plugin root.
     */
    JobSeekerActorNetworkServicePluginRoot pluginRoot;

    /**
     * Represents the plugin database dao.
     */
    JobSeekerActorNetworkServiceDao jobSeekerActorNetworkServiceDao;

    /**
     * This represents the executor service.
     */
    ExecutorService executorService;

    /**
     * Default constructor with parameters
     * @param pluginRoot
     * @param jobSeekerActorNetworkServiceDao
     */
    public JobSeekerActorNetworkServicePluginManager(
            JobSeekerActorNetworkServicePluginRoot pluginRoot,
            JobSeekerActorNetworkServiceDao jobSeekerActorNetworkServiceDao) {
        this.pluginRoot = pluginRoot;
        this.jobSeekerActorNetworkServiceDao = jobSeekerActorNetworkServiceDao;
        this.executorService = Executors.newFixedThreadPool(3);
    }

    /**
     * This method expose the given identity using the JobSeekerExposingData
     * @param jobSeekerExposingData  crypto broker exposing information.
     *
     * @throws CantExposeIdentityException
     */
    @Override
    public void exposeIdentity(
            JobSeekerExposingData jobSeekerExposingData)
            throws CantExposeIdentityException {
        try {
            pluginRoot.registerActor(
                    jobSeekerExposingData.getPublicKey(),
                    jobSeekerExposingData.getAlias(),
                    jobSeekerExposingData.getAlias(),
                    null,
                    null,
                    Actors.JOB_SEEKER,
                    jobSeekerExposingData.getImage(),
                    0,0
            );
        } catch (final ActorAlreadyRegisteredException | CantRegisterActorException e) {
            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantExposeIdentityException(
                    e,
                    "Exposing an Identity",
                    "Problem trying to register an identity component.");
        } catch (final Exception e){
            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantExposeIdentityException(
                    e,
                    "Exposing an Identity",
                    "Unhandled Exception.");
        }
    }

    /**
     * This method updates an identity by a given JobSeekerExposingData
     * @param jobSeekerExposingData
     * @throws CantExposeIdentityException
     */
    @Override
    public void updateIdentity(JobSeekerExposingData jobSeekerExposingData)
            throws CantExposeIdentityException {
        try {
            pluginRoot.updateRegisteredActor(
                    jobSeekerExposingData.getPublicKey(),
                    jobSeekerExposingData.getAlias(),
                    jobSeekerExposingData.getAlias(),
                    null,
                    null,
                    jobSeekerExposingData.getImage()
            );
        }catch (Exception e){
            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantExposeIdentityException(
                    e,
                    "Updating an Identity",
                    "Unhandled Exception.");
        }
    }

    /**
     * This method expose the identities contained in a JobSeekerExposingData list.
     * @param jobSeekerExposingDataList  list of crypto broker exposing information.
     *
     * @throws CantExposeIdentitiesException
     */
    @Override
    public void exposeIdentities(
            Collection<JobSeekerExposingData> jobSeekerExposingDataList)
            throws CantExposeIdentitiesException {
        try {
            for (final JobSeekerExposingData jobSeekerExposingData : jobSeekerExposingDataList)
                this.exposeIdentity(jobSeekerExposingData);
        } catch (final CantExposeIdentityException e){
            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantExposeIdentitiesException(
                    e,
                    "Exposing multiple Identities",
                    "Problem trying to expose an identity.");
        } catch (final Exception e){
            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantExposeIdentitiesException(
                    e,
                    "Exposing multiple Identities",
                    "Unhandled Exception.");
        }
    }

    /**
     * This method return a P2P network search
     * @return
     */
    @Override
    public JobSeekerSearch getSearch() {
        return new JobSeekerPluginSearch(
                pluginRoot
        );
    }

    /**
     * This method contains all the logic to accept a connection request
     * @param requestId  id of the connection request to accept.
     *
     * @throws CantAcceptConnectionRequestException
     * @throws ConnectionRequestNotFoundException
     */
    @Override
    public void acceptConnection(UUID requestId)
            throws CantAcceptConnectionRequestException, ConnectionRequestNotFoundException {

        try {

            final ProtocolState protocolState = ProtocolState.PROCESSING_SEND;

            jobSeekerActorNetworkServiceDao.acceptConnection(
                    requestId,
                    protocolState
            );

            JobActorConnectionRequest jobSeekerConnectionRequest = jobSeekerActorNetworkServiceDao.getConnectionRequest(requestId);

            sendMessage(
                    buildJsonInformationMessage(jobSeekerConnectionRequest),
                    jobSeekerConnectionRequest.getDestinationPublicKey(),
                    Actors.JOB_SEEKER,
                    jobSeekerConnectionRequest.getSenderPublicKey(),
                    jobSeekerConnectionRequest.getSenderActorType()
            );

        } catch (final CantAcceptConnectionRequestException | ConnectionRequestNotFoundException e){
            // ConnectionRequestNotFoundException - THIS SHOULD NOT HAPPEN.
            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw e;
        } catch (final Exception e){
            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantAcceptConnectionRequestException(
                    e,
                    "Accepting connection request",
                    "Unhandled Exception.");
        }

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

    /**
     * This method implements a connection request.
     * @param jobSeekerConnectionInformation
     * @throws CantRequestConnectionException
     */
    @Override
    public final void requestConnection(
            final JobSeekerConnectionInformation jobSeekerConnectionInformation)
            throws CantRequestConnectionException {
        try {
            final ProtocolState state = ProtocolState.PROCESSING_SEND;
            final RequestType type = RequestType.SENT;
            final ConnectionRequestAction action = ConnectionRequestAction.REQUEST;
            jobSeekerActorNetworkServiceDao.createConnectionRequest(
                    jobSeekerConnectionInformation,
                    state,
                    type,
                    action
            );
            sendMessage(
                    buildJsonRequestMessage(jobSeekerConnectionInformation),
                    jobSeekerConnectionInformation.getSenderPublicKey(),
                    jobSeekerConnectionInformation.getSenderActorType(),
                    jobSeekerConnectionInformation.getDestinationPublicKey(),
                    Actors.JOB_SEEKER
            );
        } catch (final CantRequestConnectionException e){
            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw e;
        } catch (final Exception e){
            pluginRoot.reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantRequestConnectionException(
                    e,
                    "Requesting a connection",
                    "Unhandled Exception.");
        }
    }

    /**
     * This method sends a message through Fermat network
     * @param jsonMessage
     * @param identityPublicKey
     * @param identityType
     * @param actorPublicKey
     * @param actorType
     */
    private void sendMessage(
            final String jsonMessage,
            final String identityPublicKey,
            final Actors identityType,
            final String actorPublicKey,
            final Actors actorType) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    ActorProfile sender = new ActorProfile();
                    sender.setActorType(identityType.getCode());
                    sender.setIdentityPublicKey(identityPublicKey);

                    ActorProfile receiver = new ActorProfile();
                    receiver.setActorType(actorType.getCode());
                    receiver.setIdentityPublicKey(actorPublicKey);

                    pluginRoot.sendNewMessage(
                            sender,
                            receiver,
                            jsonMessage
                    );
                } catch (CantSendMessageException e) {
                    pluginRoot.reportError(
                            UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                            e);
                }
            }
        });
    }

    /**
     * This method build a Json String to be sent
     * @param jobSeekerConnectionInformation
     * @return
     */
    private String buildJsonRequestMessage(
            final JobSeekerConnectionInformation jobSeekerConnectionInformation) {
        return new RequestMessage(
                jobSeekerConnectionInformation.getConnectionId(),
                jobSeekerConnectionInformation.getSenderPublicKey(),
                jobSeekerConnectionInformation.getSenderActorType(),
                jobSeekerConnectionInformation.getSenderAlias(),
                jobSeekerConnectionInformation.getSenderImage(),
                jobSeekerConnectionInformation.getDestinationPublicKey(),
                ConnectionRequestAction.REQUEST,
                jobSeekerConnectionInformation.getSendingTime()
        ).toJson();
    }

    private String buildJsonInformationMessage(final JobActorConnectionRequest jobSeekerConnectionRequest) {

        return new InformationMessage(
                jobSeekerConnectionRequest.getRequestId(),
                jobSeekerConnectionRequest.getRequestAction()
        ).toJson();
    }

}
