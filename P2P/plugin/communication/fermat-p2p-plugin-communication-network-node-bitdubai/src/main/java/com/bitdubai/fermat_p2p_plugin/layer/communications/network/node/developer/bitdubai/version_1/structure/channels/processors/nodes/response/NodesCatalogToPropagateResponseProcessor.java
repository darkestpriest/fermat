package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.response;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.NodesCatalogToAddOrUpdateRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.response.NodesCatalogToPropagateResponse;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodePropagationInformation;

import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.NodesCatalogToPropagateRequestProcessor</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 23/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class NodesCatalogToPropagateResponseProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(NodesCatalogToPropagateResponseProcessor.class));

    /**
     * Constructor
     */
    public NodesCatalogToPropagateResponseProcessor() {
        super(PackageType.NODES_CATALOG_TO_PROPAGATE_RESPONSE);
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

        NodesCatalogToPropagateResponse messageContent = NodesCatalogToPropagateResponse.parseContent(packageReceived.getContent());

        List<NodePropagationInformation> nodePropagationInformationResponseListReceived = messageContent.getNodePropagationInformationResponseList();

        Integer lateNotificationCounter = messageContent.getLateNotificationCounter();

        try {

            LOG.info("NodesCatalogToPropagateResponseProcessor ->: nodePropagationInformationResponseListReceived.size() -> "+ nodePropagationInformationResponseListReceived.size());

            List<NodeCatalog> nodesCatalogList = new ArrayList<>();

            for (NodePropagationInformation nodePropagationInformation : nodePropagationInformationResponseListReceived) {

                try {

                    NodeCatalog nodesCatalog = JPADaoFactory.getNodeCatalogDao().findById(nodePropagationInformation.getId());

                    nodesCatalogList.add(nodesCatalog);

                    JPADaoFactory.getNodeCatalogDao().decreasePendingPropagationsCounter(nodePropagationInformation.getId());

                } catch (Exception e) {
                    // no action here
                }
            }

            if (lateNotificationCounter != 0) {
                try {
                    JPADaoFactory.getNodeCatalogDao().increaseLateNotificationCounter(destinationIdentityPublicKey, lateNotificationCounter);
                } catch (Exception e) {
                    LOG.info("NodesCatalogToPropagateResponseProcessor ->: Unexpected error trying to update the late notification counter -> "+e.getMessage());
                }
            }

            LOG.info("NodesCatalogToPropagateResponseProcessor ->: nodesCatalogList.size() -> " +nodesCatalogList.size());

            if (!nodePropagationInformationResponseListReceived.isEmpty()) {
                NodesCatalogToAddOrUpdateRequest addNodeToCatalogResponse = new NodesCatalogToAddOrUpdateRequest(nodesCatalogList);
                Package packageRespond = Package.createInstance(addNodeToCatalogResponse.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.NODES_CATALOG_TO_ADD_OR_UPDATE_REQUEST, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the response
                 */
                session.getAsyncRemote().sendObject(packageRespond);
            } else {

                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "There's no information requested."));
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
