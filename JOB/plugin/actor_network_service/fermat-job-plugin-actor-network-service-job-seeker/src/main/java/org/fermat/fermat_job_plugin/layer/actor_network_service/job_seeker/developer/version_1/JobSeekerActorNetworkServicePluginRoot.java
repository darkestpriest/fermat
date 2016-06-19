package org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.core.PluginInfo;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.abstract_classes.AbstractActorNetworkService;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.exceptions.CantUpdateRecordDataBaseException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.exceptions.RecordNotFoundException;

import org.fermat.fermat_job_api.all_definition.enums.ProtocolState;
import org.fermat.fermat_job_api.all_definition.enums.RequestType;
import org.fermat.fermat_job_api.all_definition.events.enums.EventType;
import org.fermat.fermat_job_api.all_definition.exceptions.CantAcceptConnectionRequestException;
import org.fermat.fermat_job_api.all_definition.exceptions.CantDisconnectException;
import org.fermat.fermat_job_api.all_definition.exceptions.CantFindRequestException;
import org.fermat.fermat_job_api.all_definition.exceptions.ConnectionRequestNotFoundException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantHandleNewMessagesException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantRequestConnectionException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantRequestResumeException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.ResumeRequestNotFoundException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.utils.JobSeekerConnectionInformation;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.database.JobSeekerActorNetworkServiceDao;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.database.JobSeekerActorNetworkServiceDeveloperDatabaseFactory;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.exceptions.CantDenyConnectionRequestException;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.exceptions.CantInitializeDatabaseException;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.messages.InformationMessage;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.messages.NetworkServiceMessage;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.messages.RequestMessage;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.structure.JobActorActorNetworkServiceResumesRequest;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.structure.JobSeekerActorNetworkServicePluginManager;

import java.util.List;

@PluginInfo(createdBy = "darkestpriest", maintainerMail = "darkpriestrelative@gmail.com", platform = Platforms.JOB_PLATFORM, layer = Layers.ACTOR_NETWORK_SERVICE, plugin = Plugins.JOB_SEEKER)
public class JobSeekerActorNetworkServicePluginRoot extends AbstractActorNetworkService implements DatabaseManagerForDevelopers{

    /**
     * Represents the plugin manager
     */
    JobSeekerActorNetworkServicePluginManager jobSeekerActorNetworkServicePluginManager;

    /**
     * Represents the plugin database dao.
     */
    JobSeekerActorNetworkServiceDao jobSeekerActorNetworkServiceDao;

    /**
     * Default constructor.
     */
    public JobSeekerActorNetworkServicePluginRoot(){

        super(
                new PluginVersionReference(new Version()),
                EventSource.JOB_SEEKER_ACTOR_NETWORK_SERVICE,
                NetworkServiceType.JOB_SEEKER
        );

    }

    /**
     * Service Interface implementation
     */
    @Override
    public void onActorNetworkServiceStart() throws CantStartPluginException {

        try {
            //Init plugin database DAO
            jobSeekerActorNetworkServiceDao = new JobSeekerActorNetworkServiceDao(
                    pluginDatabaseSystem,
                    pluginFileSystem,
                    pluginId);
            jobSeekerActorNetworkServiceDao.initialize();
            //Init plugin manager
            jobSeekerActorNetworkServicePluginManager = new JobSeekerActorNetworkServicePluginManager(
                    this,
                    jobSeekerActorNetworkServiceDao
            );
        } catch(final CantInitializeDatabaseException e) {
            errorManager.reportUnexpectedPluginException(
                    this.getPluginVersionReference(),
                    UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN,
                    e);
            throw new CantStartPluginException(
                    e,
                    "Starting the plugin",
                    "There's a problem initializing Job Seeker database dao.");
        }
    }

    @Override
    public FermatManager getManager() {
        return jobSeekerActorNetworkServicePluginManager;
    }

    @Override
    public synchronized void onSentMessage(
            com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.entities.NetworkServiceMessage networkServiceMessage2) {
        System.out.println("************ Message sent, in theory, by Job Seeker actor network service");

        try {
            String jsonMessage = networkServiceMessage2.getContent();
            NetworkServiceMessage networkServiceMessage = NetworkServiceMessage.fromJson(
                    jsonMessage);
            System.out.println("********************* Message Sent Type:  " + networkServiceMessage.getMessageType());
            switch (networkServiceMessage.getMessageType()) {
                case CONNECTION_INFORMATION:
                    InformationMessage informationMessage = InformationMessage.fromJson(
                            jsonMessage);
                    jobSeekerActorNetworkServiceDao.confirmActorConnectionRequest(
                            informationMessage.getRequestId());
                    break;
                case CONNECTION_REQUEST:
                    // update the request to processing receive state with the given action.
                    RequestMessage requestMessage = RequestMessage.fromJson(jsonMessage);
                    jobSeekerActorNetworkServiceDao.confirmActorConnectionRequest(
                            requestMessage.getRequestId());
                    break;
                case RESUME_REQUEST:
                    JobActorActorNetworkServiceResumesRequest quotesRequestMessage =
                            JobActorActorNetworkServiceResumesRequest.fromJson(jsonMessage);
                    jobSeekerActorNetworkServiceDao.confirmResumeRequest(
                            quotesRequestMessage.getRequestId());
                    break;
                default:
                    throw new CantHandleNewMessagesException(
                            "message type: " +networkServiceMessage.getMessageType().name()+" Message type not handled."
                    );
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            errorManager.reportUnexpectedPluginException(
                    this.getPluginVersionReference(),
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
        }
    }

    @Override
    public void onNewMessageReceived(
            com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.entities.NetworkServiceMessage fermatMessage) {

        System.out.println("****** CRYPTO BROKER ACTOR NETWORK SERVICE NEW MESSAGE RECEIVED: " + fermatMessage);
        try {
            String jsonMessage = fermatMessage.getContent();
            NetworkServiceMessage networkServiceMessage = NetworkServiceMessage.fromJson(
                    jsonMessage);
            System.out.println("********************* Message Type:  " + networkServiceMessage.getMessageType());
            switch (networkServiceMessage.getMessageType()) {
                case CONNECTION_INFORMATION:
                    InformationMessage informationMessage = InformationMessage.fromJson(
                            jsonMessage);
                    System.out.println("********************* Content:  " + informationMessage);
                    receiveConnectionInformation(informationMessage);
                    break;
                case CONNECTION_REQUEST:
                    // update the request to processing receive state with the given action.
                    RequestMessage requestMessage = RequestMessage.fromJson(jsonMessage);
                    System.out.println("********************* Content:  " + requestMessage);
                    receiveRequest(requestMessage);
                    break;
                case RESUME_REQUEST:
                    JobActorActorNetworkServiceResumesRequest quotesRequestMessage =
                            JobActorActorNetworkServiceResumesRequest.fromJson(jsonMessage);
                    System.out.println("********************* Content:  " + quotesRequestMessage);
                    receiveResumeRequest(quotesRequestMessage);
                    break;
                default:
                    throw new CantHandleNewMessagesException(
                            "message type: " +networkServiceMessage.getMessageType().name()+" Message type not handled."
                    );
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            errorManager.reportUnexpectedPluginException(
                    this.getPluginVersionReference(),
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
        }
        try {
            getNetworkServiceConnectionManager().getIncomingMessagesDao().markAsRead(fermatMessage);
        } catch (CantUpdateRecordDataBaseException | RecordNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        return new JobSeekerActorNetworkServiceDeveloperDatabaseFactory(
                pluginDatabaseSystem,
                pluginId
        ).getDatabaseList(
                developerObjectFactory
        );
    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(
            DeveloperObjectFactory developerObjectFactory,
            DeveloperDatabase developerDatabase) {
        return new JobSeekerActorNetworkServiceDeveloperDatabaseFactory(
                pluginDatabaseSystem,
                pluginId
        ).getDatabaseTableList(
                developerObjectFactory,
                developerDatabase
        );
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(
            DeveloperObjectFactory developerObjectFactory,
            DeveloperDatabase developerDatabase,
            DeveloperDatabaseTable developerDatabaseTable) {
        return new JobSeekerActorNetworkServiceDeveloperDatabaseFactory(
                pluginDatabaseSystem,
                pluginId
        ).getDatabaseTableContent(
                developerObjectFactory,
                developerDatabase     ,
                developerDatabaseTable
        );
    }

    /**
     * I indicate to the Agent the action that it must take:
     * - Protocol State: PROCESSING_RECEIVE.    .
     */
    private void receiveConnectionInformation(
            final InformationMessage informationMessage)
            throws CantHandleNewMessagesException {

        try {
            ProtocolState state = ProtocolState.PENDING_LOCAL_ACTION;
            switch (informationMessage.getAction()) {
                case ACCEPT:
                    jobSeekerActorNetworkServiceDao.acceptConnection(
                            informationMessage.getRequestId(),
                            state
                    );
                    break;
                case DENY:
                    jobSeekerActorNetworkServiceDao.denyConnection(
                            informationMessage.getRequestId(),
                            state
                    );
                    break;
                case DISCONNECT:
                    jobSeekerActorNetworkServiceDao.disconnectConnection(
                            informationMessage.getRequestId(),
                            state
                    );
                    break;
                default:
                    throw new CantHandleNewMessagesException(
                            "action not supported: " +informationMessage.getAction()+" action not handled."
                    );
            }
            FermatEvent eventToRaise = eventManager.getNewEvent(
                    EventType.JOB_SEEKER_CONNECTION_REQUEST_UPDATES);
            eventToRaise.setSource(eventSource);
            eventManager.raiseEvent(eventToRaise);

        } catch(CantAcceptConnectionRequestException | CantDenyConnectionRequestException | ConnectionRequestNotFoundException | CantDisconnectException e) {
            //I'll inform to error manager the error.
            errorManager.reportUnexpectedPluginException(
                    this.getPluginVersionReference(),
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantHandleNewMessagesException(
                    e,
                    "Executing a connection action",
                    "Error in Job Seeker ANS Dao.");
        } catch(Exception e) {
            errorManager.reportUnexpectedPluginException(
                    this.getPluginVersionReference(),
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantHandleNewMessagesException(
                    e,
                    "Executing a connection action",
                    "Unhandled Exception.");
        }
    }

    /**
     * I indicate to the Agent the action that it must take:
     * - Protocol State: PROCESSING_RECEIVE.
     * - Type          : RECEIVED.
     */
    private void receiveRequest(final RequestMessage requestMessage)
            throws CantHandleNewMessagesException {

        try {
            if (jobSeekerActorNetworkServiceDao.existsConnectionRequest(
                    requestMessage.getRequestId()))
                return;
            final ProtocolState state  = ProtocolState.PENDING_LOCAL_ACTION;
            final RequestType type = RequestType.RECEIVED;
            final JobSeekerConnectionInformation connectionInformation =
                    new JobSeekerConnectionInformation(
                            requestMessage.getRequestId(),
                            requestMessage.getSenderPublicKey(),
                            requestMessage.getSenderActorType(),
                            requestMessage.getSenderAlias(),
                            requestMessage.getSenderImage(),
                            requestMessage.getDestinationPublicKey(),
                            requestMessage.getSentTime()
            );
            jobSeekerActorNetworkServiceDao.createConnectionRequest(
                    connectionInformation,
                    state,
                    type,
                    requestMessage.getRequestAction()
            );
            FermatEvent eventToRaise = eventManager.getNewEvent(
                    EventType.JOB_SEEKER_CONNECTION_REQUEST_NEWS);
            eventToRaise.setSource(eventSource);
            eventManager.raiseEvent(eventToRaise);

        } catch(CantRequestConnectionException e) {
            //I'll inform to error manager the error.
            errorManager.reportUnexpectedPluginException(
                    this.getPluginVersionReference(),
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantHandleNewMessagesException(
                    e,
                    "Executing a receive action",
                    "Error in Job Seeker ANS Dao.");
        } catch(Exception e) {
            errorManager.reportUnexpectedPluginException(
                    this.getPluginVersionReference(),
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantHandleNewMessagesException(
                    e,
                    "Executing a receive action",
                    "Unhandled Exception.");
        }
    }

    /**
     * This method contains all the logic to receive a resume request
     * @param quotesRequestReceived
     * @throws CantHandleNewMessagesException
     */
    private void receiveResumeRequest(
            final JobActorActorNetworkServiceResumesRequest quotesRequestReceived)
            throws CantHandleNewMessagesException {

        try {
            JobActorActorNetworkServiceResumesRequest resumeRequest =
                    jobSeekerActorNetworkServiceDao.getResumeRequest(quotesRequestReceived.getRequestId());

            if (resumeRequest.getType() == RequestType.SENT) {
                jobSeekerActorNetworkServiceDao.answerResumeRequest(
                        quotesRequestReceived.getRequestId(),
                        quotesRequestReceived.getUpdateTime(),
                        quotesRequestReceived.listInformation(),
                        ProtocolState.PENDING_LOCAL_ACTION
                );
                FermatEvent eventToRaise = eventManager.getNewEvent(EventType.JOB_SEEKER_RESUME_REQUEST_UPDATES);
                eventToRaise.setSource(eventSource);
                eventManager.raiseEvent(eventToRaise);
            }
        } catch (ResumeRequestNotFoundException e) {
            try {
                final ProtocolState state = ProtocolState.PENDING_LOCAL_ACTION;
                final RequestType type = RequestType.RECEIVED;
                jobSeekerActorNetworkServiceDao.createResumeRequest(
                        quotesRequestReceived.getRequestId(),
                        quotesRequestReceived.getRequesterPublicKey(),
                        quotesRequestReceived.getRequesterActorType(),
                        quotesRequestReceived.getJobSeekerPublicKey(),
                        state,
                        type
                );
                FermatEvent eventToRaise = eventManager.getNewEvent(EventType.JOB_SEEKER_RESUME_REQUEST_NEWS);
                eventToRaise.setSource(eventSource);
                eventManager.raiseEvent(eventToRaise);
            } catch (CantRequestResumeException cantRequestQuotesException) {
                errorManager.reportUnexpectedPluginException(
                        this.getPluginVersionReference(),
                        UnexpectedPluginExceptionSeverity
                                .DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                        cantRequestQuotesException);
                throw new CantHandleNewMessagesException(
                        cantRequestQuotesException,
                        "Handling a received resume request",
                        "Error in Job Seeker ANS Dao.");
            }
        } catch (CantFindRequestException e) {
            //I'll inform to error manager the error.
            errorManager.reportUnexpectedPluginException(
                    this.getPluginVersionReference(),
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantHandleNewMessagesException(e,
                    "Handling a received resume request",
                    "Error in Job Seeker ANS Dao.");
        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(
                    this.getPluginVersionReference(),
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
            throw new CantHandleNewMessagesException(
                    e,
                    "Handling a received resume request",
                    "Unhandled Exception.");
        }
    }

}