package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.ActorCatalogToPropagateRequest;
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
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.nodes.request.ActorCatalogToPropagateRequestProcessor</code>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 24/07/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class ActorCatalogToPropagateRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(ActorCatalogToPropagateRequestProcessor.class));

    /**
     * Constructor
     */
    public ActorCatalogToPropagateRequestProcessor() {
        super(PackageType.ACTOR_CATALOG_TO_PROPAGATE_REQUEST);
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

        ActorCatalogToPropagateRequest messageContent = ActorCatalogToPropagateRequest.parseContent(packageReceived.getContent());

        List<ActorPropagationInformation> propagationInformationList = messageContent.getActorPropagationInformationList();

        try {

            LOG.info("ActorCatalogToPropagateRequestProcessor ->: propagationInformationList.size() -> " + (propagationInformationList != null ? propagationInformationList.size() : null));

            List<ActorPropagationInformation> propagationInformationResponseList = new ArrayList<>();

            Integer lateNotificationCounter = 0;

            for (ActorPropagationInformation propagationInformation : propagationInformationList) {

                if (JPADaoFactory.getActorCatalogDao().exist(propagationInformation.getId())) {

                    ActorCatalog actorsCatalog = JPADaoFactory.getActorCatalogDao().findById(propagationInformation.getId());

                    // if the version is minor than i have then i request for it
                    if (actorsCatalog.getVersion() < propagationInformation.getVersion())
                        propagationInformationResponseList.add(
                                new ActorPropagationInformation(
                                        propagationInformation.getId(),
                                        actorsCatalog.getVersion(),
                                        actorsCatalog.getLastUpdateType()
                                )
                        );
                    else
                        lateNotificationCounter++;

                } else {

                    propagationInformationResponseList.add(
                            new ActorPropagationInformation(
                                    propagationInformation.getId(),
                                    null,
                                    null
                            )
                    );
                }
            }

            if (!propagationInformationResponseList.isEmpty()) {

                ActorCatalogToPropagateResponse addNodeToCatalogResponse = new ActorCatalogToPropagateResponse(propagationInformationResponseList, lateNotificationCounter, ActorCatalogToPropagateResponse.STATUS.SUCCESS, null);
                Package packageRespond = Package.createInstance(addNodeToCatalogResponse.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.ACTOR_CATALOG_TO_PROPAGATE_RESPONSE, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getAsyncRemote().sendObject(packageRespond);
            } else {
                session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, "There's no more information to exchange."));
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
