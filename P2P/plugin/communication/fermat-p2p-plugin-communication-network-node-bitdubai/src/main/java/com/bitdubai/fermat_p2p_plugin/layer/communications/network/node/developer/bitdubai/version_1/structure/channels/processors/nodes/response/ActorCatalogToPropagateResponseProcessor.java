package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.response;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.actors.ActorsCatalogPropagationConfiguration;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.ActorCatalogToAddOrUpdateRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.response.ActorCatalogToPropagateResponse;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ActorPropagationInformation;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.ActorCatalogToPropagateResponseProcessor</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 24/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class ActorCatalogToPropagateResponseProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(ActorCatalogToPropagateResponseProcessor.class));

    /**
     * Constructor
     */
    public ActorCatalogToPropagateResponseProcessor() {
        super(PackageType.ACTOR_CATALOG_TO_PROPAGATE_RESPONSE);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public synchronized void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: "+packageReceived.getPackageType());

        String channelIdentityPrivateKey = channel.getChannelIdentity().getPrivateKey();

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);

        ActorCatalogToPropagateResponse messageContent = ActorCatalogToPropagateResponse.parseContent(packageReceived.getContent());

        List<ActorPropagationInformation> propagationInformationResponseList = messageContent.getActorPropagationInformationList();

        Integer lateNotificationCounter = messageContent.getLateNotificationCounter();

        try {

            LOG.info("ActorCatalogToPropagateResponseProcessor ->: propagationInformationResponseList.size() -> " + propagationInformationResponseList.size());

            List<ActorCatalog> itemList = new ArrayList<>();

            for (ActorPropagationInformation propagationInformation : propagationInformationResponseList) {

                // if the count of items to share is greater or equal to the max requestable items i will stop looking for items.
                if (itemList.size() >= ActorsCatalogPropagationConfiguration.MAX_REQUESTABLE_ITEMS)
                    break;

                if (JPADaoFactory.getActorCatalogDao().exist(propagationInformation.getId())) {

                    ActorCatalog actorsCatalog = JPADaoFactory.getActorCatalogDao().findById(propagationInformation.getId());

                    itemList.add(actorsCatalog);

                    JPADaoFactory.getActorCatalogDao().decreasePendingPropagationsCounter(propagationInformation.getId());
                }

                propagationInformationResponseList.remove(propagationInformation);

            }

            if (lateNotificationCounter != 0) {
                try {
                    JPADaoFactory.getNodeCatalogDao().increaseLateNotificationCounter(destinationIdentityPublicKey, lateNotificationCounter);
                } catch (Exception e) {
                    LOG.info("ActorCatalogToPropagateResponseProcessor ->: Unexpected error trying to update the late notification counter -> "+e.getMessage());
                }
            }

            LOG.info("ActorCatalogToPropagateResponseProcessor ->: itemList.size() -> " + itemList.size());
            LOG.info("ActorCatalogToPropagateResponseProcessor ->: propagationInformationResponseList.size() -> " + propagationInformationResponseList.size());

            if (!itemList.isEmpty()) {
                ActorCatalogToAddOrUpdateRequest response = new ActorCatalogToAddOrUpdateRequest(itemList, propagationInformationResponseList);
                Package packageRespond = Package.createInstance(response.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.ACTOR_CATALOG_TO_ADD_OR_UPDATE_REQUEST, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the response
                 */
                session.getAsyncRemote().sendObject(packageRespond);
            } else {

                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "There's no information to send."));
            }

        } catch (Exception exception){

            try {

                LOG.error(FermatException.wrapException(exception).toString());

                if (session.isOpen()) {
                    session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, "Can't process ACTOR_CATALOG_TO_PROPAGATE_RESPONSE. ||| "+ exception.getMessage()));
                }else {
                    LOG.error("The session already close, no try to close");
                }

            } catch (Exception e) {
                LOG.error(FermatException.wrapException(e).toString());
            }

        }
    }

}
