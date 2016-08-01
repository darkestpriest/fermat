package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MessageTransmitRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.database.entities.NetworkServiceMessage;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.JPANamedQuery;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.caches.ClientsSessionMemoryCache;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorSession;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.Map;

import javax.websocket.SendHandler;
import javax.websocket.SendResult;
import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.MessageTransmitProcessor</code>
 * process all packages received the type <code>PackageType.MESSAGE_TRANSMIT</code><p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 30/04/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class MessageTransmitProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(MessageTransmitProcessor.class));

    /**
     * Represent the clientsSessionMemoryCache instance
     */
    private ClientsSessionMemoryCache clientsSessionMemoryCache;

    /**
     * Constructor
     */
    public MessageTransmitProcessor() {
        super(PackageType.MESSAGE_TRANSMIT);
        this.clientsSessionMemoryCache = ClientsSessionMemoryCache.getInstance();
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public void processingPackage(final Session session, final Package packageReceived, final FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received "+packageReceived.getPackageType());
        String senderIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);
        MessageTransmitRespond messageTransmitRespond = null;
        final NetworkServiceMessage messageContent = NetworkServiceMessage.parseContent(packageReceived.getContent());

        final String destinationIdentityPublicKey = packageReceived.getDestinationPublicKey();
        LOG.info("Package destinationIdentityPublicKey =  "+destinationIdentityPublicKey);

        try {

            /*
             * Create the method call history
             */
            methodCallsHistory(packageReceived.getContent(), senderIdentityPublicKey);

             /*
             * Get the connection to the destination
             */
            Session clientDestination = null;

            ActorCatalog actor = JPADaoFactory.getActorCatalogDao().findById(destinationIdentityPublicKey);

            if (actor.getSession() != null){
                LOG.info("Actor session found = "+actor.getSession());
                clientDestination = clientsSessionMemoryCache.get(actor.getClient().getId());
            }

            if (clientDestination != null){

                clientDestination.getAsyncRemote().sendObject(packageReceived, new SendHandler() {
                    @Override
                    public void onResult(SendResult result) {

                        try {
                            if (result.isOK()) {

                                MessageTransmitRespond messageTransmitRespond = new MessageTransmitRespond(MsgRespond.STATUS.SUCCESS, MsgRespond.STATUS.SUCCESS.toString(), messageContent.getId());

                                channel.sendPackage(session, messageTransmitRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.MESSAGE_TRANSMIT_RESPONSE, destinationIdentityPublicKey);
                                LOG.info("Message transmit successfully");
                            } else {
                                MessageTransmitRespond messageTransmitRespond = new MessageTransmitRespond(
                                        MsgRespond.STATUS.FAIL,
                                        (result.getException() != null ? result.getException().getMessage() : "destination not available"),
                                        messageContent.getId());
                                channel.sendPackage(session, messageTransmitRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.MESSAGE_TRANSMIT_RESPONSE, destinationIdentityPublicKey);
                                LOG.info("Message cannot be transmitted", result.getException());
                            }
                        } catch (Exception ex) {
                            LOG.error("Cannot send message to counter part.", ex);
                        }
                    }
                });

            } else {

                /*
                 * Notify to de sender the message can not transmit
                 */
                messageTransmitRespond = new MessageTransmitRespond(MsgRespond.STATUS.FAIL, "The destination is not more available", messageContent.getId());
                channel.sendPackage(session, messageTransmitRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.MESSAGE_TRANSMIT_RESPONSE, destinationIdentityPublicKey);

                LOG.info("The destination is not more available, Message not transmitted");
            }

            LOG.info("------------------ Processing finish ------------------");

        } catch (Exception exception){

            try {
            
                LOG.error(exception);

                messageTransmitRespond = new MessageTransmitRespond(MsgRespond.STATUS.FAIL, exception.getMessage(), messageContent.getId());
                channel.sendPackage(session, messageTransmitRespond.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.MESSAGE_TRANSMIT_RESPONSE, destinationIdentityPublicKey);

            } catch (Exception e) {
                LOG.error(e);
            }
        }
    }

}
