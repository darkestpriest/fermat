package org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;
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
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantRequestConnectionException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantRequestResumeException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.ResumeRequestNotFoundException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.utils.JobSeekerConnectionInformation;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.utils.JobSeekerExposingData;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * This version doesn't include method to allow request connections from Job seekers to Employers.
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/06/16.
 */
public interface JobSeekerManager extends FermatManager {

    /**
     * Through the method <code>exposeIdentity</code> we can expose the JOB identities created in
     * the device.
     * The information given will be shown to all the job seekers.
     *
     * @param jobSeekerExposingData  crypto broker exposing information.
     *
     * @throws CantExposeIdentityException   if something goes wrong.
     */
    void exposeIdentity(
            final JobSeekerExposingData jobSeekerExposingData) throws CantExposeIdentityException;

    /**
     * This method updates a identity in fermat network
     * @param jobSeekerExposingData
     * @throws CantExposeIdentityException
     */
    void updateIdentity(
            final JobSeekerExposingData jobSeekerExposingData) throws CantExposeIdentityException;

    /**
     * Through the method <code>exposeIdentities</code> we can expose the job identities created in the device.
     * The information given will be shown to all the Job Seekers.
     *
     * @param jobSeekerExposingDataList  list of crypto broker exposing information.
     *
     * @throws CantExposeIdentitiesException   if something goes wrong.
     */
    void exposeIdentities(
            final Collection<JobSeekerExposingData> jobSeekerExposingDataList)
            throws CantExposeIdentitiesException;

    /**
     * Through the method <code>getSearch</code> we can get a new instance of Job Seeker Search.
     * This Crypto Broker search provides all the necessary functionality to make a Job Seeker Search.
     *
     * @return a JobSeekerSearch instance.
     */
    JobSeekerSearch getSearch();

    /**
     * Through the method <code>acceptConnection</code> we can accept a received connection request.
     * In this version the Job seeker will accept all connections from Employer automatically. This
     * process will be executed by the Actor plugin
     *
     * @param requestId  id of the connection request to accept.
     *
     * @throws CantAcceptConnectionRequestException   if something goes wrong.
     * @throws ConnectionRequestNotFoundException     if the connection request cannot be found.
     */
    void acceptConnection(final UUID requestId)
            throws CantAcceptConnectionRequestException, ConnectionRequestNotFoundException;

    /**
     * Through the method <code>listPendingConnectionNews</code> we can list all the connection news
     * with a pending local action.
     *
     * @param actorType type of the actor whom wants to be new notifications
     *
     * @return a list of instance of JobActorConnectionRequest
     *
     * @throws CantListPendingConnectionRequestsException if something goes wrong.
     */
    List<JobActorConnectionRequest> listPendingConnectionNews(Actors actorType)
            throws CantListPendingConnectionRequestsException;

    /**
     * Through the method <code>listPendingConnectionUpdates</code> we can list all the connection news
     * with a pending local action.
     *
     * This method is exposed for all the actors that try to connect with a job seeker.
     * Here we'll return all the updates of the requests that arrive to them.
     *
     * @return a list of instance of JobActorConnectionRequest
     *
     * @throws CantListPendingConnectionRequestsException if something goes wrong.
     */
    List<JobActorConnectionRequest> listPendingConnectionUpdates()
            throws CantListPendingConnectionRequestsException;

    /**
     * Through the method <code>requestResume</code> we can request the quotes that manages a Job Seeker.
     *
     * @param requesterPublicKey     the public key of the actor that is requesting.
     * @param requesterActorType     the actor type of the actor that is requesting.
     * @param jobSeekerPublicKey  the public key of the crypto broker whom information i'm looking for.
     *
     * @return the created request.
     *
     * @throws CantRequestResumeException if something goes wrong.
     */
    JobSeekerExtraData<Resume> requestResume(
            String requesterPublicKey,
            Actors requesterActorType,
            String jobSeekerPublicKey) throws CantRequestResumeException;

    /**
     * Through the method <code>listPendingResumeRequests</code> we can list all the pending resume requests.
     * We have to set a request type indicating:
     * SENT    : All the Sent requests with a response.
     * RECEIVED: All the Received request without a response.
     *
     * @param requestType SENT or RECEIVED
     *
     * @return a list of the pending quotes requests
     *
     * @throws CantListPendingResumeRequestsException if something goes wrong.
     */
    List<JobSeekerExtraData<Resume>> listPendingResumeRequests(RequestType requestType)
            throws CantListPendingResumeRequestsException;

    /**
     * Through this method you can send the response of the quotes request to its requester.
     *
     * @param requestId   request id that we want to answer.
     * @param updateTime  update time of the sending information
     * @param quotes      list of quotes of the job seeker.
     *
     * @throws CantAnswerResumeRequestException  if something goes wrong.
     * @throws ResumeRequestNotFoundException    if i cant find the request.
     */
    void answerQuotesRequest(
            UUID requestId,
            long updateTime,
            List<Resume> quotes) throws CantAnswerResumeRequestException, ResumeRequestNotFoundException;

    /**
     * Through the method <code>confirmResumeRequest</code> we can mark as done and confirmed a pending quotes request.
     *
     * @param requestId  id of the quotes request to confirm.
     *
     * @throws CantConfirmException                if something goes wrong.
     * @throws ResumeRequestNotFoundException      if the quotes request cannot be found.
     */
    void confirmResumeRequest(final UUID requestId)
            throws CantConfirmException, ResumeRequestNotFoundException;

    /**
     * Through the method <code>confirm</code> we can mark as done and confirmed a pending connection new or update.
     *
     * @param requestId  id of the connection request to confirm.
     *
     * @throws CantConfirmException                   if something goes wrong.
     * @throws ConnectionRequestNotFoundException     if the connection request cannot be found.
     */
    void confirm(final UUID requestId) throws CantConfirmException, ConnectionRequestNotFoundException;

    /**
     * This method implements a connection request.
     * @param jobSeekerConnectionInformation
     * @throws CantRequestConnectionException
     */
    void requestConnection(final JobSeekerConnectionInformation jobSeekerConnectionInformation)
            throws CantRequestConnectionException;

}

