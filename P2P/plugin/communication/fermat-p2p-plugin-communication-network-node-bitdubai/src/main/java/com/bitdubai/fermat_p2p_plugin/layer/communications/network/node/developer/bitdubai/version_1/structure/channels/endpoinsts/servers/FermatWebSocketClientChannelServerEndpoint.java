package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.servers;

import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ServerHandshakeRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileTypes;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.exception.PackageTypeNotSupportedException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.caches.ClientsSessionMemoryCache;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.conf.ClientChannelConfigurator;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessorFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ProfileRegistrationHistory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationResult;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.PackageDecoder;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.PackageEncoder;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.PongMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.servers.FermatWebSocketClientChannelServerEndpoint</code> this
 * is a especial channel to manage all the communication between the clients and the node
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 12/11/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@ServerEndpoint(
        value = "/client-channel",
        configurator = ClientChannelConfigurator.class,
        encoders = {PackageEncoder.class},
        decoders = {PackageDecoder.class}
)
public class FermatWebSocketClientChannelServerEndpoint extends FermatWebSocketChannelEndpoint {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(FermatWebSocketClientChannelServerEndpoint.class));

    /**
     * Represent the clientsSessionMemoryCache instance
     */
    private ClientsSessionMemoryCache clientsSessionMemoryCache;

    /**
     * Constructor
     */
    public FermatWebSocketClientChannelServerEndpoint(){
        super();
        this.clientsSessionMemoryCache = ClientsSessionMemoryCache.getInstance();
    }

    /**
     * (non-javadoc)
     *
     * @see FermatWebSocketChannelEndpoint#getPackageProcessors()
     */
    @Override
    protected Map<PackageType, List<PackageProcessor>> getPackageProcessors(){
        return PackageProcessorFactory.getPackagesProcessorsFermatWebSocketClientChannelServerEndpoint();
    }

    /**
     *  Method called to handle a new connection
     *
     * @param session connected
     * @param endpointConfig created
     * @throws IOException
     */
    @OnOpen
    public void onConnect(Session session, EndpointConfig endpointConfig) throws IOException {

        LOG.info(" New connection stablished: " + session.getId());

        try {

            /*
             * Get the node identity
             */
            setChannelIdentity((ECCKeyPair) endpointConfig.getUserProperties().get(HeadersAttName.REMOTE_NPKI_ATT_HEADER_NAME));

            /*
             * Get the client public key identity
             */
            String cpki = (String) endpointConfig.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);

            /*
             * Configure the session and mach the session with the client public key identity
             */
            session.setMaxTextMessageBufferSize(FermatWebSocketChannelEndpoint.MAX_MESSAGE_SIZE);

            if (clientsSessionMemoryCache.exist(cpki)) {
                Session previousSession = clientsSessionMemoryCache.get(cpki);
                if (previousSession.isOpen())
                        previousSession.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Closing a Previous Session"));
            }

            clientsSessionMemoryCache.add(cpki, session);

            /*
             * Construct packet SERVER_HANDSHAKE_RESPONSE
             */
            ServerHandshakeRespond serverHandshakeRespond = new ServerHandshakeRespond(ServerHandshakeRespond.STATUS.SUCCESS, ServerHandshakeRespond.STATUS.SUCCESS.toString(), cpki);
            Package packageRespond = Package.createInstance(serverHandshakeRespond.toJson(), NetworkServiceType.UNDEFINED, PackageType.SERVER_HANDSHAKE_RESPONSE, getChannelIdentity().getPrivateKey(), cpki);

            /*
             * Send the respond
             */
            session.getAsyncRemote().sendObject(packageRespond);


        }catch (Exception e){
            LOG.error(e);
            session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, e.getMessage()));
        }

    }

    /**
     * Method called to handle a new message received
     *
     * @param packageReceived new
     * @param session sender
     */
    @OnMessage
    public void newPackageReceived(Package packageReceived, Session session) {

        LOG.info("New package received (" + packageReceived.getPackageType().name() + ")");
        try {

            /*
             * Process the new package received
             */
            processMessage(packageReceived, session);

        }catch (PackageTypeNotSupportedException p){
            LOG.warn(p.getMessage());
        }

    }

    @OnMessage
    public void onPongMessage(PongMessage message) {
        LOG.debug("Pong message receive from server = " + message);
    }

    /**
     * Method called to handle a connection close
     *
     * @param closeReason message with the details.
     * @param session     closed session.
     */
    @OnClose
    public void onClose(final CloseReason closeReason,
                        final Session     session    ) {

        LOG.info("Closed session : " + session.getId() + " Code: (" + closeReason.getCloseCode() + ") - reason: " + closeReason.getReasonPhrase());

        try {

            /*
             * if the client is checked in, i will delete the record
             * if not, i will register the inconsistency
             */
            String clientPublicKey = clientsSessionMemoryCache.remove(session);

            if (JPADaoFactory.getClientSessionDao().exist(session.getId())) {

                JPADaoFactory.getClientSessionDao().checkOut(session);

            } else {

                ProfileRegistrationHistory profileRegistrationHistory = new ProfileRegistrationHistory(
                        clientPublicKey,
                        null,
                        ProfileTypes.CLIENT,
                        RegistrationType.CHECK_OUT,
                        RegistrationResult.IGNORED,
                        "There's no client registered with the given public key, indicated closed reason: " + closeReason.toString()
                );

                JPADaoFactory.getProfileRegistrationHistoryDao().save(profileRegistrationHistory);
            }

        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }

    /**
     * Create a new row into the table ProfileRegistrationHistory
     * Method  called to handle a error
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable){

        LOG.error("Unhandled exception catch");
        LOG.error(throwable);
        try {

            if (session.isOpen()){
                session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, throwable.getMessage()));
            }else {
                LOG.error("The session already close, no try to close");
            }

        } catch (Exception e) {
            //I'll try to print the stacktrace to determinate this exception
            System.out.println("ON CLOSE EXCEPTION: ");
            e.printStackTrace();
            LOG.error(e);
        }
    }
}
