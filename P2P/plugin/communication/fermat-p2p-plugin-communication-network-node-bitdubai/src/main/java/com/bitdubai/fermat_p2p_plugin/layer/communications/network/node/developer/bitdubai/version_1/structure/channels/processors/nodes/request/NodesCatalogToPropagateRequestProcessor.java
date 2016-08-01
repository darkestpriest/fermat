package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.NodesCatalogToPropagateRequest;
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
public class NodesCatalogToPropagateRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(NodesCatalogToPropagateRequestProcessor.class));

    /**
     * Constructor
     */
    public NodesCatalogToPropagateRequestProcessor() {
        super(PackageType.NODES_CATALOG_TO_PROPAGATE_REQUEST);
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

        NodesCatalogToPropagateRequest messageContent = NodesCatalogToPropagateRequest.parseContent(packageReceived.getContent());

        List<NodePropagationInformation> nodePropagationInformationList = messageContent.getNodePropagationInformationList();

        try {

            LOG.info("NodesCatalogToPropagateRequestProcessor ->: nodePropagationInformationList.size() -> "+(nodePropagationInformationList != null ? nodePropagationInformationList.size() : null));

            List<NodePropagationInformation> nodePropagationInformationResponseList = new ArrayList<>();

            Integer lateNotificationCounter = 0;

            for (NodePropagationInformation nodePropagationInformation : nodePropagationInformationList) {

                try {

                    NodeCatalog nodesCatalog = JPADaoFactory.getNodeCatalogDao().findById(nodePropagationInformation.getId());

                    // if the version is minor than i have then i request for it
                    // else i increase the counter of late notification
                    if (nodesCatalog.getVersion() < nodePropagationInformation.getVersion())
                        nodePropagationInformationResponseList.add(
                                new NodePropagationInformation(
                                        nodePropagationInformation.getId(),
                                        nodesCatalog.getVersion()
                                )
                        );
                    else
                        lateNotificationCounter++;

                } catch (Exception recordNotFoundException) {

                    nodePropagationInformationResponseList.add(
                            new NodePropagationInformation(
                                    nodePropagationInformation.getId(),
                                    null
                            )
                    );
                }
            }

            NodesCatalogToPropagateResponse addNodeToCatalogResponse = new NodesCatalogToPropagateResponse(nodePropagationInformationResponseList, lateNotificationCounter, NodesCatalogToPropagateResponse.STATUS.SUCCESS, null);
            Package packageRespond = Package.createInstance(addNodeToCatalogResponse.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.NODES_CATALOG_TO_PROPAGATE_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

            /*
             * Send the respond
             */
            session.getAsyncRemote().sendObject(packageRespond);

        } catch (Exception exception){

            try {

                LOG.error(FermatException.wrapException(exception).toString());
                if (session.isOpen()) {
                    session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, "Can't process NODES_CATALOG_TO_PROPAGATE_REQUEST. ||| "+ exception.getMessage()));
                }else {
                    LOG.error("The session already close, no try to close");
                }

            } catch (Exception e) {
                LOG.error(FermatException.wrapException(e).toString());
            }

        }

    }

}
