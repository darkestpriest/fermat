package com.bitdubai.fermat_cbp_plugin.layer.actor_connection.crypto_customer.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.ActorConnectionNotFoundException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantAcceptActorConnectionRequestException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantCancelActorConnectionRequestException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantChangeActorConnectionStateException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantDenyActorConnectionRequestException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantDisconnectFromActorException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantGetConnectionStateException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.UnexpectedConnectionStateException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.enums.RequestType;
import com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.exceptions.CantConfirmException;
import com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.exceptions.CantListPendingConnectionRequestsException;
import com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.exceptions.ConnectionRequestNotFoundException;
import com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.interfaces.CryptoBrokerManager;
import com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.utils.CryptoBrokerConnectionRequest;
import com.bitdubai.fermat_cbp_plugin.layer.actor_connection.crypto_customer.developer.bitdubai.version_1.database.CryptoCustomerActorConnectionDao;
import com.bitdubai.fermat_cbp_plugin.layer.actor_connection.crypto_customer.developer.bitdubai.version_1.exceptions.CantHandleNewsEventException;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import java.util.List;
import java.util.UUID;

/**
 * The class <code>com.bitdubai.fermat_cbp_plugin.layer.actor_connection.crypto_customer.developer.bitdubai.version_1.structure.ActorConnectionEventActions</code>
 * bla bla bla.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 03/02/2016.
 */
public class ActorConnectionEventActions {

    private final CryptoBrokerManager              cryptoBrokerNetworkService;
    private final CryptoCustomerActorConnectionDao dao                       ;
    private final ErrorManager                     errorManager              ;
    private final PluginVersionReference           pluginVersionReference    ;

    public ActorConnectionEventActions(final CryptoBrokerManager                     cryptoBrokerNetworkService,
                                       final CryptoCustomerActorConnectionDao        dao                       ,
                                       final ErrorManager                            errorManager              ,
                                       final PluginVersionReference                  pluginVersionReference    ) {

        this.cryptoBrokerNetworkService = cryptoBrokerNetworkService;
        this.dao                        = dao                       ;
        this.errorManager               = errorManager              ;
        this.pluginVersionReference     = pluginVersionReference    ;
    }

    public void handleCryptoBrokerUpdateEvent() throws CantHandleNewsEventException {

        try {

            final List<CryptoBrokerConnectionRequest> list = cryptoBrokerNetworkService.listPendingConnectionUpdates();


            for (final CryptoBrokerConnectionRequest request : list) {

                if (request.getRequestType() == RequestType.SENT  && request.getSenderActorType() == Actors.CBP_CRYPTO_CUSTOMER) {

                    switch (request.getRequestAction()) {

                        case ACCEPT:
                            this.handleAcceptConnection(request.getRequestId());
                            break;
                        case DENY:
                            this.handleDenyConnection(request.getRequestId());
                            break;
                        case DISCONNECT:
                            this.handleDisconnect(request.getRequestId());
                            break;

                    }
                }
            }

        } catch(CantListPendingConnectionRequestsException |
                ActorConnectionNotFoundException           |
                UnexpectedConnectionStateException         |
                CantAcceptActorConnectionRequestException  |
                CantDenyActorConnectionRequestException    |
                CantDisconnectFromActorException           e) {

            throw new CantHandleNewsEventException(e, "", "Error handling Crypto Addresses News Event.");
        }

    }

    public void handleDisconnect(final UUID connectionId) throws CantDisconnectFromActorException   ,
                                                                 ActorConnectionNotFoundException   ,
                                                                 UnexpectedConnectionStateException {

        try {

            ConnectionState currentConnectionState = dao.getConnectionState(connectionId);

            switch (currentConnectionState) {

                case DISCONNECTED_REMOTELY:
                    // no action needed
                    break;

                case CONNECTED:

                    dao.changeConnectionState(
                            connectionId,
                            ConnectionState.DISCONNECTED_REMOTELY
                    );

                    cryptoBrokerNetworkService.confirm(connectionId);

                    break;

                default:
                    throw new UnexpectedConnectionStateException("connectionId: "+connectionId + " - currentConnectionState: "+currentConnectionState, "Unexpected contact state for denying.");
            }

        } catch (final ActorConnectionNotFoundException | UnexpectedConnectionStateException innerException) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, innerException);
            throw innerException;
        } catch (final CantGetConnectionStateException cantGetConnectionStateException) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantGetConnectionStateException);
            throw new CantDisconnectFromActorException(cantGetConnectionStateException, "connectionId: "+connectionId, "Error trying to get the connection state.");
        } catch (final CantConfirmException | ConnectionRequestNotFoundException networkServiceException ) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, networkServiceException);
            throw new CantDisconnectFromActorException(networkServiceException, "connectionId: "+connectionId, "Error trying to disconnect from an actor through the network service.");
        } catch (final CantChangeActorConnectionStateException cantChangeActorConnectionStateException ) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantChangeActorConnectionStateException);
            throw new CantDisconnectFromActorException(cantChangeActorConnectionStateException, "connectionId: "+connectionId, "Error trying to change the actor connection state.");
        } catch (final Exception exception) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, exception);
            throw new CantDisconnectFromActorException(exception, "connectionId: "+connectionId, "Unhandled error.");
        }
    }

    public void handleDenyConnection(final UUID connectionId) throws CantDenyActorConnectionRequestException,
                                                               ActorConnectionNotFoundException       ,
                                                               UnexpectedConnectionStateException     {

        try {

            ConnectionState currentConnectionState = dao.getConnectionState(connectionId);

            switch (currentConnectionState) {

                case DENIED_REMOTELY:
                    // no action needed
                    break;

                case PENDING_REMOTELY_ACCEPTANCE:

                    dao.changeConnectionState(
                            connectionId,
                            ConnectionState.DENIED_REMOTELY
                    );

                    cryptoBrokerNetworkService.confirm(connectionId);

                    break;

                default:
                    throw new UnexpectedConnectionStateException("connectionId: "+connectionId + " - currentConnectionState: "+currentConnectionState, "Unexpected contact state for denying.");
            }

        } catch (final ActorConnectionNotFoundException | UnexpectedConnectionStateException innerException) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, innerException);
            throw innerException;
        } catch (final CantGetConnectionStateException cantGetConnectionStateException) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantGetConnectionStateException);
            throw new CantDenyActorConnectionRequestException(cantGetConnectionStateException, "connectionId: "+connectionId, "Error trying to get the connection state.");
        } catch (final CantConfirmException | ConnectionRequestNotFoundException networkServiceException ) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, networkServiceException);
            throw new CantDenyActorConnectionRequestException(networkServiceException, "connectionId: "+connectionId, "Error trying to deny the connection through the network service.");
        } catch (final CantChangeActorConnectionStateException cantChangeActorConnectionStateException ) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantChangeActorConnectionStateException);
            throw new CantDenyActorConnectionRequestException(cantChangeActorConnectionStateException, "connectionId: "+connectionId, "Error trying to change the actor connection state.");
        } catch (final Exception exception) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, exception);
            throw new CantDenyActorConnectionRequestException(exception, "connectionId: "+connectionId, "Unhandled error.");
        }

    }

    public void handleCancelConnection(final UUID connectionId) throws CantCancelActorConnectionRequestException,
                                                                 ActorConnectionNotFoundException         ,
                                                                 UnexpectedConnectionStateException       {

        try {

            ConnectionState currentConnectionState = dao.getConnectionState(connectionId);

            switch (currentConnectionState) {

                case CANCELLED_REMOTELY:
                    // no action needed
                    break;

                case PENDING_LOCALLY_ACCEPTANCE:

                    dao.changeConnectionState(
                            connectionId,
                            ConnectionState.CANCELLED_REMOTELY
                    );

                    cryptoBrokerNetworkService.confirm(connectionId);

                    break;

                default:
                    throw new UnexpectedConnectionStateException(
                            "connectionId: "+connectionId + " - currentConnectionState: "+currentConnectionState,
                            "Unexpected contact state for cancelling."
                    );
            }

        } catch (final ActorConnectionNotFoundException | UnexpectedConnectionStateException innerException) {

            errorManager.reportUnexpectedPluginException(
                    pluginVersionReference,
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    innerException
            );
            throw innerException;
        } catch (final CantGetConnectionStateException cantGetConnectionStateException) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantGetConnectionStateException);
            throw new CantCancelActorConnectionRequestException(cantGetConnectionStateException, "connectionId: "+connectionId, "Error trying to get the connection state.");
        } catch (final CantConfirmException | ConnectionRequestNotFoundException networkServiceException ) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, networkServiceException);
            throw new CantCancelActorConnectionRequestException(networkServiceException, "connectionId: "+connectionId, "Error trying to cancel the connection through the network service.");
        } catch (final CantChangeActorConnectionStateException cantChangeActorConnectionStateException ) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantChangeActorConnectionStateException);
            throw new CantCancelActorConnectionRequestException(cantChangeActorConnectionStateException, "connectionId: "+connectionId, "Error trying to change the actor connection state.");
        } catch (final Exception exception) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, exception);
            throw new CantCancelActorConnectionRequestException(exception, "connectionId: "+connectionId, "Unhandled error.");
        }
    }

    public void handleAcceptConnection(final UUID connectionId) throws CantAcceptActorConnectionRequestException,
                                                                 ActorConnectionNotFoundException         ,
                                                                 UnexpectedConnectionStateException       {

        try {

            ConnectionState currentConnectionState = dao.getConnectionState(connectionId);

            switch (currentConnectionState) {

                case CONNECTED:
                    // no action needed
                    break;

                case PENDING_REMOTELY_ACCEPTANCE:

                    dao.changeConnectionState(
                            connectionId,
                            ConnectionState.CONNECTED
                    );

                    cryptoBrokerNetworkService.confirm(connectionId);

                    break;

                default:
                    throw new UnexpectedConnectionStateException("connectionId: "+connectionId + " - currentConnectionState: "+currentConnectionState, "Unexpected contact state for cancelling.");
            }

        } catch (final ActorConnectionNotFoundException | UnexpectedConnectionStateException innerException) {

            errorManager.reportUnexpectedPluginException(
                    pluginVersionReference,
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    innerException
            );
            throw innerException;
        } catch (final CantGetConnectionStateException cantGetConnectionStateException) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantGetConnectionStateException);
            throw new CantAcceptActorConnectionRequestException(cantGetConnectionStateException, "connectionId: "+connectionId, "Error trying to get the connection state.");
        } catch (final CantConfirmException | ConnectionRequestNotFoundException networkServiceException ) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, networkServiceException);
            throw new CantAcceptActorConnectionRequestException(networkServiceException, "connectionId: "+connectionId, "Error trying to accept the connection through the network service.");
        } catch (final CantChangeActorConnectionStateException cantChangeActorConnectionStateException ) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantChangeActorConnectionStateException);
            throw new CantAcceptActorConnectionRequestException(cantChangeActorConnectionStateException, "connectionId: "+connectionId, "Error trying to change the actor connection state.");
        } catch (final Exception exception) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, exception);
            throw new CantAcceptActorConnectionRequestException(exception, "connectionId: "+connectionId, "Unhandled error.");
        }
    }
}