package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.nodes.NodesCatalogPropagationConfiguration;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.ActorCatalogToAddOrUpdateRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.response.ActorCatalogToPropagateResponse;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ActorPropagationInformation;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.ActorCatalogToAddOrUpdateRequestProcessor</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 24/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class ActorCatalogToAddOrUpdateRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(ActorCatalogToAddOrUpdateRequestProcessor.class));

    /**
     * Constructor
     */
    public ActorCatalogToAddOrUpdateRequestProcessor() {
        super(PackageType.ACTOR_CATALOG_TO_ADD_OR_UPDATE_REQUEST);
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package, FermatWebSocketChannelEndpoint)
     */
    @Override
    public synchronized void processingPackage(Session session, Package packageReceived, FermatWebSocketChannelEndpoint channel) {

        LOG.info("Processing new package received: " + packageReceived.getPackageType());

        String channelIdentityPrivateKey = channel.getChannelIdentity().getPrivateKey();

        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);

        ActorCatalogToAddOrUpdateRequest messageContent = ActorCatalogToAddOrUpdateRequest.parseContent(packageReceived.getContent());

        List<ActorCatalog> catalogList = messageContent.getActorCatalogList();

        List<ActorPropagationInformation> pendingItemList = messageContent.getPendingItemList();

        try {

            LOG.info("ActorCatalogToAddOrUpdateRequestProcessor ->: catalogList.size() -> " + (catalogList != null ? catalogList.size() : null));
            LOG.info("ActorCatalogToAddOrUpdateRequestProcessor ->: pendingItemList.size() -> " + (pendingItemList != null ? pendingItemList.size() : null));

            for (ActorCatalog actorCatalogToAddOrUpdate : catalogList) {

                if(JPADaoFactory.getActorCatalogDao().exist(actorCatalogToAddOrUpdate.getId())) {

                    ActorCatalog actorsCatalog = JPADaoFactory.getActorCatalogDao().findById(actorCatalogToAddOrUpdate.getId());

                    /*
                     * If version in our node catalog is minor to the version in the remote catalog then I will update it.
                     * else no action needed
                     */
                    if (actorsCatalog.getVersion() < actorCatalogToAddOrUpdate.getVersion()) {

                        actorCatalogToAddOrUpdate.setPendingPropagations(NodesCatalogPropagationConfiguration.DESIRED_PROPAGATIONS);
                        actorCatalogToAddOrUpdate.setTriedToPropagateTimes(0);

                        JPADaoFactory.getActorCatalogDao().update(actorCatalogToAddOrUpdate);
                    }

                } else {

                    actorCatalogToAddOrUpdate.setPendingPropagations(NodesCatalogPropagationConfiguration.DESIRED_PROPAGATIONS);
                    actorCatalogToAddOrUpdate.setTriedToPropagateTimes(0);

                    JPADaoFactory.getActorCatalogDao().persist(actorCatalogToAddOrUpdate);
                }
            }

            if (!pendingItemList.isEmpty()) {

                ActorCatalogToPropagateResponse addNodeToCatalogResponse = new ActorCatalogToPropagateResponse(pendingItemList, 0, ActorCatalogToPropagateResponse.STATUS.SUCCESS, null);
                Package packageRespond = Package.createInstance(addNodeToCatalogResponse.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.ACTOR_CATALOG_TO_PROPAGATE_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getAsyncRemote().sendObject(packageRespond);
            } else {
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "There's no more information to request."));
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
