package org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.database;

import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantPersistProfileImageException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.DeviceDirectory;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.util.XMLParser;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOperator;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilter;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilterGroup;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTransaction;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseTransactionFailedException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginBinaryFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantLoadFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantPersistFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;

import org.fermat.fermat_job_api.all_definition.enums.ConnectionRequestAction;
import org.fermat.fermat_job_api.all_definition.enums.ProtocolState;
import org.fermat.fermat_job_api.all_definition.enums.RequestType;
import org.fermat.fermat_job_api.all_definition.exceptions.CantAcceptConnectionRequestException;
import org.fermat.fermat_job_api.all_definition.exceptions.CantConfirmConnectionRequestException;
import org.fermat.fermat_job_api.all_definition.exceptions.CantDisconnectException;
import org.fermat.fermat_job_api.all_definition.exceptions.CantFindRequestException;
import org.fermat.fermat_job_api.all_definition.exceptions.CantListPendingConnectionRequestsException;
import org.fermat.fermat_job_api.all_definition.exceptions.ConnectionRequestNotFoundException;
import org.fermat.fermat_job_api.all_definition.interfaces.Resume;
import org.fermat.fermat_job_api.layer.actor_network_service.common.JobActorConnectionRequest;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantAnswerResumeRequestException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantListPendingResumeRequestsException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.CantRequestResumeException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.exceptions.ResumeRequestNotFoundException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces.JobSeekerExtraData;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.utils.JobSeekerConnectionInformation;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.exceptions.CantConfirmResumeRequestException;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.exceptions.CantDenyConnectionRequestException;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.exceptions.CantGetProfileImageException;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.exceptions.CantInitializeDatabaseException;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.exceptions.CantRequestConnectionException;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.structure.JobActorActorNetworkServiceResumesRequest;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 13/06/16.
 */
public class JobSeekerActorNetworkServiceDao {

    private static final String PROFILE_IMAGE_DIRECTORY_NAME   =
            DeviceDirectory.LOCAL_USERS.getName() + "/JOB/jobSeekerActorNS";
    private static final String PROFILE_IMAGE_FILE_NAME_PREFIX = "profileImage";

    private final PluginDatabaseSystem pluginDatabaseSystem;
    private final PluginFileSystem pluginFileSystem;
    private final UUID pluginId;

    private Database database;

    /**
     * Default constructor with parameters
     * @param pluginDatabaseSystem
     * @param pluginFileSystem
     * @param pluginId
     */
    public JobSeekerActorNetworkServiceDao(
            final PluginDatabaseSystem pluginDatabaseSystem,
            final PluginFileSystem pluginFileSystem,
            final UUID pluginId) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginFileSystem = pluginFileSystem;
        this.pluginId = pluginId;
    }

    public void initialize() throws CantInitializeDatabaseException {
        try {
            database = this.pluginDatabaseSystem.openDatabase(
                    this.pluginId,
                    JobSeekerActorNetworkServiceDatabaseConstants.JOB_SEEKER_ACTOR_NETWORK_SERVICE_DATABASE_NAME
            );
        } catch (final DatabaseNotFoundException e) {
            try {
                JobSeekerActorNetworkServiceDatabaseFactory jobSeekerActorNetworkServiceDatabaseFactory
                        = new JobSeekerActorNetworkServiceDatabaseFactory(pluginDatabaseSystem);
                database = jobSeekerActorNetworkServiceDatabaseFactory.createDatabase(
                        pluginId,
                        JobSeekerActorNetworkServiceDatabaseConstants.JOB_SEEKER_ACTOR_NETWORK_SERVICE_DATABASE_NAME
                );
            } catch (final CantCreateDatabaseException f) {
                throw new CantInitializeDatabaseException(
                        f,
                        "",
                        "There was a problem and we cannot create the database.");
            } catch (final Exception z) {
                throw new CantInitializeDatabaseException(
                        z,
                        "",
                        "Unhandled Exception.");
            }
        } catch (final CantOpenDatabaseException e) {
            throw new CantInitializeDatabaseException(
                    e,
                    "",
                    "Exception not handled by the plugin, there was a problem and we cannot open the database.");
        } catch (final Exception e) {
            throw new CantInitializeDatabaseException(
                    e,
                    "",
                    "Unhandled Exception.");
        }
    }

    /**
     * This method returns a list with JobActorConnectionRequest on pending state
     * @return
     * @throws CantListPendingConnectionRequestsException
     */
    public final List<JobActorConnectionRequest> listPendingConnectionUpdates()
            throws CantListPendingConnectionRequestsException {
        try {
            final ProtocolState protocolState = ProtocolState.PENDING_LOCAL_ACTION;
            final DatabaseTable connectionNewsTable = database.getTable(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_TABLE_NAME);
            connectionNewsTable.addFermatEnumFilter(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_STATE_COLUMN_NAME,
                    protocolState,
                    DatabaseFilterType.EQUAL);
            List<ConnectionRequestAction> actions = new ArrayList<>();
            actions.add(ConnectionRequestAction.ACCEPT);
            actions.add(ConnectionRequestAction.DENY);
            final List<DatabaseTableFilter> tableFilters = new ArrayList<>();
            for(final ConnectionRequestAction action : actions)
                tableFilters.add(connectionNewsTable.getNewFilter(
                        JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ACTION_COLUMN_NAME,
                        DatabaseFilterType.EQUAL,
                        action.getCode()));
            final DatabaseTableFilterGroup filterGroup = connectionNewsTable.getNewFilterGroup(
                    tableFilters,
                    null,
                    DatabaseFilterOperator.OR);
            connectionNewsTable.setFilterGroup(filterGroup);
            connectionNewsTable.loadToMemory();
            final List<DatabaseTableRecord> records = connectionNewsTable.getRecords();
            final List<JobActorConnectionRequest> jobActorConnectionRequests = new ArrayList<>();
            for (final DatabaseTableRecord record : records)
                jobActorConnectionRequests.add(buildConnectionNewRecord(record));
            return jobActorConnectionRequests;
        } catch (final CantLoadTableToMemoryException e) {
            throw new CantListPendingConnectionRequestsException(
                    e,
                    "Getting pending connection updates",
                    "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        } catch (final InvalidParameterException e) {
            throw new CantListPendingConnectionRequestsException(
                    e,
                    "Getting pending connection updates",
                    "There is a problem with some enum code."                                                                                );
        }
    }

    /**
     * This method returns a JobActorConnectionRequest within a DatabaseRecord
     * @param record
     * @return
     * @throws InvalidParameterException
     */
    private JobActorConnectionRequest buildConnectionNewRecord(final DatabaseTableRecord record) throws InvalidParameterException {
        try {
            //ID
            UUID requestId = record.getUUIDValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ID_COLUMN_NAME);
            //Sender public key
            String senderPublicKey = record.getStringValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_SENDER_PUBLIC_KEY_COLUMN_NAME);
            //Sender actor type String
            String senderActorTypeString = record.getStringValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_SENDER_ACTOR_TYPE_COLUMN_NAME);
            //Sender Alias
            String senderAlias = record.getStringValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_SENDER_ALIAS_COLUMN_NAME);
            //Destination public key
            String destinationPublicKey = record.getStringValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_DESTINATION_PUBLIC_KEY_COLUMN_NAME);
            //Request type string
            String requestTypeString = record.getStringValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_TYPE_COLUMN_NAME);
            //Protocol state string
            String protocolStateString = record.getStringValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_STATE_COLUMN_NAME);
            //Request action
            String requestActionString = record.getStringValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ACTION_COLUMN_NAME);
            //Sent time
            Long sentTime = record.getLongValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_SENT_TIME_COLUMN_NAME);

            Actors senderActorType = Actors.getByCode(senderActorTypeString);
            RequestType requestType = RequestType.getByCode(requestTypeString);
            ProtocolState state = ProtocolState.getByCode(protocolStateString);
            ConnectionRequestAction action = ConnectionRequestAction.getByCode(requestActionString);

            byte[] profileImage;

            try {
                profileImage = getProfileImage(senderPublicKey);
            } catch (FileNotFoundException e) {
                profileImage = new byte[0];
            }

            return new JobActorConnectionRequest(
                    requestId,
                    senderPublicKey,
                    senderActorType,
                    senderAlias,
                    profileImage,
                    destinationPublicKey,
                    requestType,
                    state,
                    action,
                    sentTime
            );
        } catch (final CantGetProfileImageException e) {
            throw new InvalidParameterException(
                    e,
                    "Getting JobActorConnectionRequest from DatabaseTableRecord",
                    "Problem trying to get the profile image."
            );
        }
    }

    /**
     * This method returns a profile stored in the device.
     * @param publicKey
     * @return
     * @throws CantGetProfileImageException
     * @throws FileNotFoundException
     */
    private byte[] getProfileImage(final String publicKey)
            throws CantGetProfileImageException,
            FileNotFoundException{
        try {
            PluginBinaryFile file = this.pluginFileSystem.getBinaryFile(
                    pluginId,
                    PROFILE_IMAGE_DIRECTORY_NAME,
                    buildProfileImageFileName(publicKey),
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );
            file.loadFromMedia();
            return file.getContent();
        } catch (final CantLoadFileException e) {
            throw new CantGetProfileImageException(
                    e,
                    "Getting image profile from local storage.",
                    "Cannot load image"
            );
        } catch (final FileNotFoundException | CantCreateFileException e) {
            throw new FileNotFoundException(e, "", null);
        } catch (Exception e) {
            throw new CantGetProfileImageException(
                    e,
                    "Getting image profile from local storage",
                    "Unhandled Exception"
            );
        }
    }

    /**
     * This method returns the profile image stored name.
     * @param publicKey
     * @return
     */
    private String buildProfileImageFileName(final String publicKey) {
        return PROFILE_IMAGE_FILE_NAME_PREFIX + "_" + publicKey;
    }

    /**
     * This method returns a List with pending connections news
     * @param actorType
     * @return
     * @throws CantListPendingConnectionRequestsException
     */
    public final List<JobActorConnectionRequest> listPendingConnectionNews(
            final Actors actorType)
            throws CantListPendingConnectionRequestsException {
        try {
            final ProtocolState protocolState = ProtocolState.PENDING_LOCAL_ACTION;
            final ConnectionRequestAction action = ConnectionRequestAction.REQUEST;
            final DatabaseTable connectionNewsTable = database.getTable(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_TABLE_NAME);
            connectionNewsTable.addFermatEnumFilter(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_STATE_COLUMN_NAME,
                    protocolState,
                    DatabaseFilterType.EQUAL);
            if (actorType != null)
                connectionNewsTable.addFermatEnumFilter(
                        JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_SENDER_ACTOR_TYPE_COLUMN_NAME,
                        actorType,
                        DatabaseFilterType.EQUAL);
            connectionNewsTable.addFermatEnumFilter(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ACTION_COLUMN_NAME,
                    action,
                    DatabaseFilterType.EQUAL);
            connectionNewsTable.loadToMemory();
            final List<DatabaseTableRecord> records = connectionNewsTable.getRecords();
            final List<JobActorConnectionRequest> jobActorConnectionRequests = new ArrayList<>();
            for (final DatabaseTableRecord record : records)
                jobActorConnectionRequests.add(buildConnectionNewRecord(record));
            return jobActorConnectionRequests;
        } catch (final CantLoadTableToMemoryException e) {
            throw new CantListPendingConnectionRequestsException(
                    e,
                    "Getting pending connection news",
                    "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        } catch (final InvalidParameterException e) {
            throw new CantListPendingConnectionRequestsException(
                    e,
                    "Getting pending connection news",
                    "There is a problem with some enum code.");
        }
    }

    /**
     * This method returns the request list by protocol state
     * @param protocolState
     * @return
     * @throws CantListPendingConnectionRequestsException
     */
    public final List<JobActorConnectionRequest> listAllRequestByProtocolState(
            final ProtocolState protocolState)
            throws CantListPendingConnectionRequestsException {
        try {
            final DatabaseTable connectionNewsTable = database.getTable(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_TABLE_NAME);
            connectionNewsTable.addFermatEnumFilter(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_STATE_COLUMN_NAME,
                    protocolState,
                    DatabaseFilterType.EQUAL);
            connectionNewsTable.loadToMemory();
            final List<DatabaseTableRecord> records = connectionNewsTable.getRecords();
            final List<JobActorConnectionRequest> jobActorConnectionRequests = new ArrayList<>();
            for (final DatabaseTableRecord record : records)
                jobActorConnectionRequests.add(buildConnectionNewRecord(record));
            return jobActorConnectionRequests;
        } catch (final CantLoadTableToMemoryException e) {
            throw new CantListPendingConnectionRequestsException(
                    e,
                    "Getting a list of all request by protocol state",
                    "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        } catch (final InvalidParameterException e) {
            throw new CantListPendingConnectionRequestsException(
                    e,
                    "Getting a list of all request by protocol state", "There is a problem with some enum code."                                                                                );
        }
    }

    public final void createConnectionRequest(
            final JobSeekerConnectionInformation jobSeekerConnectionInformation,
            final ProtocolState state,
            final RequestType type,
            final ConnectionRequestAction action) throws CantRequestConnectionException {

        try {

            final JobActorConnectionRequest connectionNew = new JobActorConnectionRequest(
                    jobSeekerConnectionInformation.getConnectionId(),
                    jobSeekerConnectionInformation.getSenderPublicKey(),
                    jobSeekerConnectionInformation.getSenderActorType(),
                    jobSeekerConnectionInformation.getSenderAlias(),
                    jobSeekerConnectionInformation.getSenderImage(),
                    jobSeekerConnectionInformation.getDestinationPublicKey(),
                    type,
                    state,
                    action,
                    jobSeekerConnectionInformation.getSendingTime()
            );
            final DatabaseTable addressExchangeRequestTable = database.getTable(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_TABLE_NAME);
            DatabaseTableRecord entityRecord = addressExchangeRequestTable.getEmptyRecord();
            entityRecord = buildConnectionNewDatabaseRecord(entityRecord, connectionNew);
            addressExchangeRequestTable.insertRecord(entityRecord);
        } catch (final CantInsertRecordException e) {
            throw new CantRequestConnectionException(
                    e,
                    "Creating a new connection request",
                    "Exception not handled by the plugin, there is a problem in database and i cannot insert the record.");
        }
    }

    /**
     * This method returns a DatabaseTableRecord with modifications given at a JobActorConnectionRequest
     * @param record
     * @param connectionNew
     * @return
     */
    private DatabaseTableRecord buildConnectionNewDatabaseRecord(
            final DatabaseTableRecord record,
            final JobActorConnectionRequest connectionNew) {
        try {
            record.setUUIDValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ID_COLUMN_NAME,
                    connectionNew.getRequestId());
            record.setStringValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_SENDER_PUBLIC_KEY_COLUMN_NAME,
                    connectionNew.getSenderPublicKey());
            record.setFermatEnum(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_SENDER_ACTOR_TYPE_COLUMN_NAME,
                    connectionNew.getSenderActorType());
            record.setStringValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_SENDER_ALIAS_COLUMN_NAME,
                    connectionNew.getSenderAlias());
            record.setStringValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_DESTINATION_PUBLIC_KEY_COLUMN_NAME,
                    connectionNew.getDestinationPublicKey());
            record.setFermatEnum(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_TYPE_COLUMN_NAME,
                    connectionNew.getRequestType());
            record.setFermatEnum(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_STATE_COLUMN_NAME,
                    connectionNew.getProtocolState());
            record.setFermatEnum(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ACTION_COLUMN_NAME,
                    connectionNew.getRequestAction());
            record.setLongValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_SENT_TIME_COLUMN_NAME,
                    connectionNew.getSentTime());
            if (connectionNew.getSenderImage() != null && connectionNew.getSenderImage().length > 0)
                persistNewUserProfileImage(
                        connectionNew.getSenderPublicKey(),
                        connectionNew.getSenderImage());
            return record;
        } catch (final Exception e) {
            System.err.println("error trying to persist image:"+e.getMessage());
            return record;
        }
    }

    /**
     * This method returns persists a new profile image
     * @param publicKey
     * @param profileImage
     * @throws CantPersistProfileImageException
     */
    private void persistNewUserProfileImage(
            final String publicKey,
            final byte[] profileImage) throws CantPersistProfileImageException {
        try {
            PluginBinaryFile file = this.pluginFileSystem.createBinaryFile(pluginId,
                    PROFILE_IMAGE_DIRECTORY_NAME,
                    buildProfileImageFileName(publicKey),
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );
            file.setContent(profileImage);
            file.persistToMedia();
        } catch (final CantPersistFileException e) {
            throw new CantPersistProfileImageException(
                    e,
                    "Persisting a profile image",
                    "Error persist file."
            );
        } catch (final CantCreateFileException e) {
            throw new CantPersistProfileImageException(
                    e,
                    "Persisting a profile image",
                    "Cannot create persist file."
            );
        } catch (final Exception e) {
            throw new CantPersistProfileImageException(
                    e,
                    "Persisting a profile image",
                    "Unhandled Exception."
            );
        }
    }

    /**
     * This method denies a connection request.
     * @param requestId
     * @param state
     * @throws CantDenyConnectionRequestException
     * @throws ConnectionRequestNotFoundException
     */
    public void denyConnection(
            final UUID requestId,
            final ProtocolState state)
            throws CantDenyConnectionRequestException,
            ConnectionRequestNotFoundException {
        if (requestId == null)
            throw new CantDenyConnectionRequestException("The requestId is required, can not be null");
        if (state == null)
            throw new CantDenyConnectionRequestException("The state is required, can not be null");
        try {
            final ConnectionRequestAction action = ConnectionRequestAction.DENY;
            final DatabaseTable connectionNewsTable = database.getTable(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_TABLE_NAME);
            connectionNewsTable.addUUIDFilter(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ID_COLUMN_NAME,
                    requestId,
                    DatabaseFilterType.EQUAL);
            connectionNewsTable.loadToMemory();
            final List<DatabaseTableRecord> records = connectionNewsTable.getRecords();
            if (!records.isEmpty()) {
                final DatabaseTableRecord record = records.get(0);
                record.setFermatEnum(
                        JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_STATE_COLUMN_NAME,
                        state);
                record.setFermatEnum(
                        JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ACTION_COLUMN_NAME,
                        action);
                connectionNewsTable.updateRecord(record);
            } else
                throw new ConnectionRequestNotFoundException(
                        null,
                        "requestId: "+requestId,
                        "Cannot find an actor connection request with that requestId.");
        } catch (final CantUpdateRecordException e) {
            throw new CantDenyConnectionRequestException(
                    e,
                    "Denying a connection request",
                    "Exception not handled by the plugin, there is a problem in database and I cannot update the record.");
        } catch (final CantLoadTableToMemoryException e) {
            throw new CantDenyConnectionRequestException(
                    e,
                    "Denying a connection request",
                    "Exception not handled by the plugin, there is a problem in database and I cannot load the table.");
        }
    }

    /**
     * This method disconnects a connection
     * @param requestId
     * @param state
     * @throws CantDisconnectException
     * @throws ConnectionRequestNotFoundException
     */
    public void disconnectConnection(
            final UUID requestId,
            final ProtocolState state)
            throws CantDisconnectException,
            ConnectionRequestNotFoundException {
        if (requestId == null)
            throw new CantDisconnectException("The requestId is required, can not be null");
        if (state == null)
            throw new CantDisconnectException("The state is required, can not be null");
        try {
            final ConnectionRequestAction action = ConnectionRequestAction.DISCONNECT;
            final DatabaseTable connectionNewsTable = database.getTable(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_TABLE_NAME);
            connectionNewsTable.addUUIDFilter(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ID_COLUMN_NAME,
                    requestId,
                    DatabaseFilterType.EQUAL);
            connectionNewsTable.loadToMemory();
            final List<DatabaseTableRecord> records = connectionNewsTable.getRecords();
            if (!records.isEmpty()) {
                final DatabaseTableRecord record = records.get(0);
                record.setFermatEnum(
                        JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_STATE_COLUMN_NAME,
                        state );
                record.setFermatEnum(
                        JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ACTION_COLUMN_NAME,
                        action);
                connectionNewsTable.updateRecord(record);
            } else
                throw new ConnectionRequestNotFoundException(
                        null,
                        "RequestId: "+requestId,
                        "Cannot find an actor connection request with that requestId.");
        } catch (final CantUpdateRecordException e) {
            throw new CantDisconnectException(
                    e,
                    "Disconnect a connection",
                    "Exception not handled by the plugin, there is a problem in database and I cannot update the record.");
        } catch (final CantLoadTableToMemoryException e) {
            throw new CantDisconnectException(
                    e,
                    "Disconnect a connection",
                    "Exception not handled by the plugin, there is a problem in database and I cannot load the table.");
        }
    }

    /**
     * This method returns a JobActorConnectionRequest by a given request Id.
     * @param requestId
     * @return
     * @throws CantFindRequestException
     * @throws ConnectionRequestNotFoundException
     */
    public JobActorConnectionRequest getConnectionRequest(
            final UUID requestId)
            throws CantFindRequestException,
            ConnectionRequestNotFoundException {
        if (requestId == null)
            throw new CantFindRequestException("The requestId is required, can not be null");
        try {
            final DatabaseTable connectionRequestTable = database.getTable(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_TABLE_NAME);
            connectionRequestTable.addUUIDFilter(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ID_COLUMN_NAME,
                    requestId,
                    DatabaseFilterType.EQUAL);
            connectionRequestTable.loadToMemory();
            final List<DatabaseTableRecord> records = connectionRequestTable.getRecords();
            if (!records.isEmpty())
                return buildConnectionNewRecord(records.get(0));
            else
                throw new ConnectionRequestNotFoundException(
                        null,
                        "requestId: "+requestId,
                        "Cannot find an actor Connection request with that requestId.");
        } catch (final CantLoadTableToMemoryException e) {
            throw new CantFindRequestException(
                    e,
                    "Getting a connection request by Id:"+requestId,
                    "Exception not handled by the plugin, there is a problem in database and I cannot load the table.");
        } catch (final InvalidParameterException e) {
            throw new CantFindRequestException(
                    e,
                    "Getting a connection request by Id:"+requestId,
                    "Exception reading records of the table Cannot recognize the codes of the currencies.");
        }
    }

    /**
     * This method confirms a connection request
     * @param requestId
     * @throws CantConfirmConnectionRequestException
     * @throws ConnectionRequestNotFoundException
     */
    public void confirmActorConnectionRequest(final UUID requestId)
            throws CantConfirmConnectionRequestException,
            ConnectionRequestNotFoundException{
        if (requestId == null) {
            throw new CantConfirmConnectionRequestException(
                    "The requestId is required, can not be null");
        }
        try {
            ProtocolState state = ProtocolState.DONE;
            ConnectionRequestAction action = ConnectionRequestAction.NONE;
            DatabaseTable actorConnectionRequestTable = database.getTable(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_TABLE_NAME);
            actorConnectionRequestTable.addUUIDFilter(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ID_COLUMN_NAME,
                    requestId,
                    DatabaseFilterType.EQUAL);
            actorConnectionRequestTable.loadToMemory();
            List<DatabaseTableRecord> records = actorConnectionRequestTable.getRecords();
            if (!records.isEmpty()) {
                DatabaseTableRecord record = records.get(0);
                record.setStringValue(
                        JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_STATE_COLUMN_NAME,
                        state.getCode());
                record.setStringValue(
                        JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ACTION_COLUMN_NAME,
                        action.getCode());
                actorConnectionRequestTable.updateRecord(record);
            } else
                throw new ConnectionRequestNotFoundException(
                        null,
                        "requestId: "+requestId,
                        "Cannot find an address exchange request with that requestId.");
        } catch (CantUpdateRecordException e) {
            throw new CantConfirmConnectionRequestException(
                    e,
                    "Confirming a connection request",
                    "Exception not handled by the plugin, there is a problem in database and I cannot update the record.");
        } catch (CantLoadTableToMemoryException e) {
            throw new CantConfirmConnectionRequestException(
                    e,
                    "Confirming a connection request",
                    "Exception not handled by the plugin, there is a problem in database and I cannot load the table.");
        }
    }

    /**
     * This method contains the logic to accept connections.
     * @param requestId
     * @param state
     * @throws CantAcceptConnectionRequestException
     * @throws ConnectionRequestNotFoundException
     */
    public void acceptConnection(
            final UUID requestId,
            final ProtocolState state)
            throws CantAcceptConnectionRequestException,
            ConnectionRequestNotFoundException{
        if (requestId == null)
            throw new CantAcceptConnectionRequestException(
                    "The requestId is required, can not be null");
        if (state == null)
            throw new CantAcceptConnectionRequestException(
                    "The state is required, can not be null");
        try {
            final ConnectionRequestAction action = ConnectionRequestAction.ACCEPT;
            final DatabaseTable connectionNewsTable = database.getTable(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_TABLE_NAME);
            connectionNewsTable.addUUIDFilter(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ID_COLUMN_NAME,
                    requestId,
                    DatabaseFilterType.EQUAL);
            connectionNewsTable.loadToMemory();
            final List<DatabaseTableRecord> records = connectionNewsTable.getRecords();
            if (!records.isEmpty()) {
                final DatabaseTableRecord record = records.get(0);
                record.setFermatEnum(
                        JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_STATE_COLUMN_NAME,
                        state);
                record.setFermatEnum(
                        JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ACTION_COLUMN_NAME,
                        action);
                connectionNewsTable.updateRecord(record);
            } else
                throw new ConnectionRequestNotFoundException(
                        null,
                        "requestId: "+requestId,
                        "Cannot find an actor connection request with that requestId.");
        } catch (final CantUpdateRecordException e) {
            throw new CantAcceptConnectionRequestException(
                    e,
                    "Accepting a connection",
                    "Exception not handled by the plugin, there is a problem in database and I cannot update the record.");
        } catch (final CantLoadTableToMemoryException e) {
            throw new CantAcceptConnectionRequestException(
                    e,
                    "Accepting a connection",
                    "Exception not handled by the plugin, there is a problem in database and I cannot load the table.");
        }
    }

    /**
     * This method returns the destination public key from a given connection Id.
     * @param connectionId
     * @return
     * @throws CantListPendingConnectionRequestsException
     * @throws ConnectionRequestNotFoundException
     */
    public String getDestinationPublicKey(
            final UUID connectionId)
            throws CantListPendingConnectionRequestsException,
            ConnectionRequestNotFoundException {
        if (connectionId == null)
            throw new CantListPendingConnectionRequestsException(
                    "The connectionId is required, can not be null");
        try {
            final DatabaseTable actorConnectionsTable = database.getTable(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_TABLE_NAME);
            actorConnectionsTable.addUUIDFilter(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ID_COLUMN_NAME,
                    connectionId,
                    DatabaseFilterType.EQUAL);
            actorConnectionsTable.loadToMemory();
            final List<DatabaseTableRecord> records = actorConnectionsTable.getRecords();
            if (!records.isEmpty()) {
                final DatabaseTableRecord record = records.get(0);
                return record.getStringValue(
                        JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_DESTINATION_PUBLIC_KEY_COLUMN_NAME);
            } else
                throw new ConnectionRequestNotFoundException(
                        null,
                        "connectionId: "+connectionId,
                        "Cannot find an actor connection request with that requestId."
                );
        } catch (final CantLoadTableToMemoryException cantLoadTableToMemoryException) {
            throw new CantListPendingConnectionRequestsException(
                    cantLoadTableToMemoryException,
                    "ConnectionId: "+connectionId,
                    "Exception not handled by the plugin, there is a problem in database and I cannot load the table.");
        }
    }

    /**
     * This method returns if exists a connection request.
     * @param requestId
     * @return
     * @throws CantFindRequestException
     */
    public boolean existsConnectionRequest(final UUID requestId) throws CantFindRequestException {
        if (requestId == null)
            throw new CantFindRequestException("The requestId is required, can not be null");
        try {
            final DatabaseTable connectionNewsTable = database.getTable(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_TABLE_NAME);
            connectionNewsTable.addUUIDFilter(
                    JobSeekerActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ID_COLUMN_NAME,
                    requestId,
                    DatabaseFilterType.EQUAL);
            connectionNewsTable.loadToMemory();
            final List<DatabaseTableRecord> records = connectionNewsTable.getRecords();
            return !records.isEmpty();
        } catch (final CantLoadTableToMemoryException e) {
            throw new CantFindRequestException(
                    e,
                    "Asking if exists a connection with Id: "+requestId,
                    "Exception not handled by the plugin, there is a problem in database and I cannot load the table.");
        }
    }

    /**
     * This method returns a JobActorActorNetworkServiceResumesRequest by a given request Id
     * @param requestId
     * @return
     * @throws CantFindRequestException
     * @throws ResumeRequestNotFoundException
     */
    public JobActorActorNetworkServiceResumesRequest getResumeRequest(final UUID requestId)
            throws CantFindRequestException, ResumeRequestNotFoundException {
        if (requestId == null)
            throw new CantFindRequestException("The requestId is required, can not be null");
        try {
            final DatabaseTable quotesRequestTable = database.getTable(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_TABLE_NAME);
            quotesRequestTable.addUUIDFilter(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_REQUEST_ID_COLUMN_NAME,
                    requestId,
                    DatabaseFilterType.EQUAL);
            quotesRequestTable.loadToMemory();
            final List<DatabaseTableRecord> records = quotesRequestTable.getRecords();
            if (!records.isEmpty())
                return buildResumeRequestObject(records.get(0));
            else
                throw new ResumeRequestNotFoundException("Cannot find a quotes request with that id.");
        } catch (final CantLoadTableToMemoryException e) {
            throw new CantFindRequestException(
                    e,
                    "Getting the resume request",
                    "Exception not handled by the plugin, there is a problem in database and I cannot load the table.");
        } catch (CantListPendingResumeRequestsException e) {
            throw new CantFindRequestException(
                    e,
                    "Getting the resume request",
                    "Exception in dao trying to list the pending request.");
        } catch (final InvalidParameterException e) {
            throw new CantFindRequestException(
                    e,
                    "Getting the resume request",
                    "Exception reading records of the table Cannot recognize the codes of the currencies.");
        }
    }

    /**
     * This method builds a JobActorActorNetworkServiceResumesRequest by a given DatabaseTableRecord
     * @param record
     * @return
     * @throws CantListPendingResumeRequestsException
     * @throws InvalidParameterException
     */
    private JobActorActorNetworkServiceResumesRequest buildResumeRequestObject(
            final DatabaseTableRecord record)
            throws CantListPendingResumeRequestsException, InvalidParameterException {
        UUID requestId = record.getUUIDValue(
                JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_REQUEST_ID_COLUMN_NAME);
        String requesterPublicKey = record.getStringValue(
                JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_REQUESTER_PUBLIC_KEY_COLUMN_NAME);
        String requesterActorTypeString = record.getStringValue(
                JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_REQUESTER_ACTOR_TYPE_COLUMN_NAME);
        String cryptoBrokerPublicKey = record.getStringValue(
                JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_JOB_SEEKER_PUBLIC_KEY_COLUMN_NAME);
        Long updateTime = record.getLongValue(
                JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_UPDATE_TIME_COLUMN_NAME);
        String typeString = record.getStringValue(
                JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_TYPE_COLUMN_NAME);
        String stateString = record.getStringValue(
                JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_STATE_COLUMN_NAME);
        Actors requesterActorType = Actors.getByCode(requesterActorTypeString);
        RequestType type = RequestType.getByCode(typeString);
        ProtocolState state = ProtocolState.getByCode(stateString);
        return new JobActorActorNetworkServiceResumesRequest(
                requestId            ,
                requesterPublicKey   ,
                requesterActorType   ,
                cryptoBrokerPublicKey,
                updateTime           ,
                listResume(requestId),
                type                 ,
                state
        );
    }

    /**
     * This method returns a resume list
     * @param requestId
     * @return
     * @throws CantListPendingResumeRequestsException
     */
    private ArrayList<Resume> listResume(UUID requestId)
            throws CantListPendingResumeRequestsException {
        try {
            final DatabaseTable quotesTable = database.getTable(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_TABLE_NAME);
            quotesTable.addUUIDFilter(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_ID_COLUMN_NAME,
                    requestId,
                    DatabaseFilterType.EQUAL);
            quotesTable.loadToMemory();
            final List<DatabaseTableRecord> records = quotesTable.getRecords();

            ArrayList<Resume> resumeList = new ArrayList<>();

            for(DatabaseTableRecord record : records) {
                String resumeString = record.getStringValue(
                        JobSeekerActorNetworkServiceDatabaseConstants.RESUME_XML_STRING_COLUMN_NAME);
                Resume resume = null;
                Object xmlObject = XMLParser.parseXML(resumeString,resume);
                resume = (Resume) xmlObject;
                resumeList.add(
                        resume
                );
            }
            return resumeList;
        } catch (final CantLoadTableToMemoryException e) {
            throw new CantListPendingResumeRequestsException(
                    e,
                    "Getting the resume list",
                    "Exception not handled by the plugin, there is a problem in database and I cannot load the table.");
        }
    }

    /**
     * This method creates a resume request
     * @param requestId
     * @param requesterPublicKey
     * @param requesterActorType
     * @param cryptoBrokerPublicKey
     * @param state
     * @param type
     * @return
     * @throws CantRequestResumeException
     */
    public final JobActorActorNetworkServiceResumesRequest createResumeRequest(
            final UUID requestId,
            final String requesterPublicKey,
            final Actors requesterActorType,
            final String cryptoBrokerPublicKey,
            final ProtocolState state,
            final RequestType type) throws CantRequestResumeException {
        try {
            final DatabaseTable quotesRequestTable = database.getTable(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_TABLE_NAME);
            final DatabaseTableRecord quotesRequestRecord = quotesRequestTable.getEmptyRecord();
            final JobActorActorNetworkServiceResumesRequest resumeRequest =
                    new JobActorActorNetworkServiceResumesRequest(
                            requestId,
                            requesterPublicKey,
                            requesterActorType,
                            cryptoBrokerPublicKey,
                            0,
                            new ArrayList<Resume>(),
                            type,
                            state
                    );

            quotesRequestRecord.setUUIDValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_REQUEST_ID_COLUMN_NAME,
                    resumeRequest.getRequestId());
            quotesRequestRecord.setStringValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_REQUESTER_PUBLIC_KEY_COLUMN_NAME,
                    resumeRequest.getRequesterPublicKey());
            quotesRequestRecord.setFermatEnum(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_REQUESTER_ACTOR_TYPE_COLUMN_NAME,
                    resumeRequest.getRequesterActorType());
            quotesRequestRecord.setStringValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_JOB_SEEKER_PUBLIC_KEY_COLUMN_NAME,
                    resumeRequest.getJobSeekerPublicKey());
            quotesRequestRecord.setLongValue(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_UPDATE_TIME_COLUMN_NAME,
                    resumeRequest.getUpdateTime());
            quotesRequestRecord.setFermatEnum(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_TYPE_COLUMN_NAME,
                    resumeRequest.getType());
            quotesRequestRecord.setFermatEnum(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_STATE_COLUMN_NAME,
                    resumeRequest.getState());
            quotesRequestTable.insertRecord(quotesRequestRecord);
            return resumeRequest;
        } catch (final CantInsertRecordException e) {
            throw new CantRequestResumeException(
                    e,
                    "Creating a resume request",
                    "Exception not handled by the plugin, there is a problem in database and I cannot insert all the records.");
        }
    }

    /**
     * This method answers a Resume request
     * @param requestId
     * @param updateTime
     * @param resumes
     * @param state
     * @throws CantAnswerResumeRequestException
     * @throws ResumeRequestNotFoundException
     */
    public final void answerResumeRequest(
            final UUID requestId,
            final long updateTime,
            final List<Resume> resumes,
            final ProtocolState state)
            throws CantAnswerResumeRequestException, ResumeRequestNotFoundException {
        try {
            final DatabaseTable quotesRequestTable = database.getTable(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_TABLE_NAME);
            quotesRequestTable.addUUIDFilter(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_ID_COLUMN_NAME,
                    requestId,
                    DatabaseFilterType.EQUAL);
            quotesRequestTable.loadToMemory();
            final List<DatabaseTableRecord> records = quotesRequestTable.getRecords();
            DatabaseTableRecord quotesRequestRecord;
            if (!records.isEmpty()) {
                quotesRequestRecord = records.get(0);
                quotesRequestRecord.setFermatEnum(
                        JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_STATE_COLUMN_NAME,
                        state);
                quotesRequestRecord.setLongValue(
                        JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_UPDATE_TIME_COLUMN_NAME,
                        updateTime);
            } else
                throw new ResumeRequestNotFoundException("Cannot find a quotes request with that id:"+requestId);
            DatabaseTransaction databaseTransaction = database.newTransaction();
            databaseTransaction.addRecordToUpdate(quotesRequestTable, quotesRequestRecord);
            for (final Resume resume : resumes) {

                final DatabaseTable quotesTable = database.getTable(
                        JobSeekerActorNetworkServiceDatabaseConstants.RESUME_TABLE_NAME);
                final DatabaseTableRecord quotesRecord = quotesTable.getEmptyRecord();
                quotesRecord.setUUIDValue(
                        JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_ID_COLUMN_NAME,
                        requestId);
                quotesRecord.setUUIDValue(
                        JobSeekerActorNetworkServiceDatabaseConstants.RESUME_INNER_ID_COLUMN_NAME,
                        resume.getResumeId());
                quotesRecord.setStringValue(
                        JobSeekerActorNetworkServiceDatabaseConstants.RESUME_OWNER_PUBLIC_KEY_COLUMN_NAME,
                        resume.getActorPublicKey());
                quotesRecord.setStringValue(
                        JobSeekerActorNetworkServiceDatabaseConstants.RESUME_XML_STRING_COLUMN_NAME, XMLParser.parseObject(resume));
                databaseTransaction.addRecordToInsert(quotesTable, quotesRecord);
            }
            databaseTransaction.execute();

        } catch (final CantLoadTableToMemoryException e) {
            throw new CantAnswerResumeRequestException(
                    e,
                    "Answering a resume request",
                    "Exception not handled by the plugin, there is a problem in database and I cannot load the table.");
        } catch (final DatabaseTransactionFailedException e) {
            throw new CantAnswerResumeRequestException(
                    e,
                    "Answering a resume request",
                    "Exception not handled by the plugin, there is a problem in database and I cannot insert all the records.");
        }
    }

    /**
     * This method returns a JobSeekerExtraData list
     * @param protocolState
     * @param requestType
     * @return
     * @throws CantListPendingResumeRequestsException
     */
    public final List<JobSeekerExtraData<Resume>> listPendingQuotesRequests(
            final ProtocolState protocolState,
            final RequestType requestType)
            throws CantListPendingResumeRequestsException {
        try {
            final DatabaseTable connectionNewsTable = database.getTable(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_TABLE_NAME);
            connectionNewsTable.addFermatEnumFilter(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_TYPE_COLUMN_NAME,
                    requestType,
                    DatabaseFilterType.EQUAL);
            connectionNewsTable.addFermatEnumFilter(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_STATE_COLUMN_NAME,
                    protocolState,
                    DatabaseFilterType.EQUAL);
            connectionNewsTable.loadToMemory();
            final List<DatabaseTableRecord> records = connectionNewsTable.getRecords();
            final List<JobSeekerExtraData<Resume>> quotesRequestsList = new ArrayList<>();
            for (final DatabaseTableRecord record : records)
                quotesRequestsList.add(buildResumeRequestObject(record));
            return quotesRequestsList;
        } catch (final CantLoadTableToMemoryException e) {
            throw new CantListPendingResumeRequestsException(
                    e,
                    "Listing pending resume request",
                    "Exception not handled by the plugin, there is a problem in database and I cannot load the table.");
        } catch (final InvalidParameterException e) {
            throw new CantListPendingResumeRequestsException(
                    e,
                    "Listing pending resume request",
                    "Exception reading records of the table Cannot recognize the codes.");
        }
    }

    public void confirmResumeRequest(final UUID requestId)
            throws CantConfirmResumeRequestException,
            ResumeRequestNotFoundException   {
        if (requestId == null) {
            throw new CantConfirmResumeRequestException("The requestId is required, can not be null");
        }
        try {
            ProtocolState state  = ProtocolState.DONE;
            DatabaseTable actorConnectionRequestTable = database.getTable(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_TABLE_NAME);
            actorConnectionRequestTable.addUUIDFilter(
                    JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_REQUEST_ID_COLUMN_NAME,
                    requestId,
                    DatabaseFilterType.EQUAL);
            actorConnectionRequestTable.loadToMemory();
            List<DatabaseTableRecord> records = actorConnectionRequestTable.getRecords();
            if (!records.isEmpty()) {
                DatabaseTableRecord record = records.get(0);
                record.setFermatEnum(
                        JobSeekerActorNetworkServiceDatabaseConstants.RESUME_REQUEST_STATE_COLUMN_NAME,
                        state);
                actorConnectionRequestTable.updateRecord(record);
            } else
                throw new ResumeRequestNotFoundException("Cannot find a quotes request with that requestId."+requestId);
        } catch (CantUpdateRecordException e) {
            throw new CantConfirmResumeRequestException(
                    e,
                    "Confirming resume request",
                    "Exception not handled by the plugin, there is a problem in database and I cannot update the record.");
        } catch (CantLoadTableToMemoryException e) {
            throw new CantConfirmResumeRequestException(
                    e,
                    "Confirming resume request",
                    "Exception not handled by the plugin, there is a problem in database and I cannot load the table.");

        }
    }

}
