package org.fermat.fermat_job_api.layer.actor_network_service.common;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;

import org.fermat.fermat_job_api.all_definition.enums.ConnectionRequestAction;
import org.fermat.fermat_job_api.all_definition.enums.ProtocolState;
import org.fermat.fermat_job_api.all_definition.enums.RequestType;

import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/06/16.
 */
public class JobActorConnectionRequest {

    /**
     * Class objects
     */
    private final UUID requestId;
    private final String senderPublicKey;
    private final Actors senderActorType;
    private final String senderAlias;
    private final byte[] senderImage;
    private final String destinationPublicKey;
    private final RequestType requestType;
    private final ProtocolState protocolState;
    private final ConnectionRequestAction requestAction;
    private final long sentTime;

    /**
     * Constructor with parameters
     * @param requestId
     * @param senderPublicKey
     * @param senderActorType
     * @param senderAlias
     * @param senderImage
     * @param destinationPublicKey
     * @param requestType
     * @param protocolState
     * @param requestAction
     * @param sentTime
     */
    public JobActorConnectionRequest(
            final UUID requestId,
            final String senderPublicKey,
            final Actors senderActorType,
            final String senderAlias,
            final byte[] senderImage,
            final String destinationPublicKey,
            final RequestType requestType,
            final ProtocolState protocolState,
            final ConnectionRequestAction requestAction,
            final long sentTime) {

        this.requestId = requestId;
        this.senderPublicKey = senderPublicKey;
        this.senderActorType = senderActorType;
        this.senderAlias = senderAlias;
        this.senderImage = senderImage;
        this.destinationPublicKey = destinationPublicKey;
        this.requestType = requestType;
        this.protocolState = protocolState;
        this.requestAction = requestAction;
        this.sentTime = sentTime;
    }

    /**
     * @return an uuid representing the request id.
     */
    public final UUID getRequestId() {
        return requestId;
    }

    /**
     * @return a string representing the sender public key.
     */
    public final String getSenderPublicKey() {
        return senderPublicKey;
    }

    /**
     * @return an element of actors enum representing the actor type of the sender.
     */
    public final Actors getSenderActorType() {
        return senderActorType;
    }

    /**
     * @return a string representing the alias of the Job Actor.
     */
    public final String getSenderAlias() {
        return senderAlias;
    }

    /**
     * @return an array of bytes with the image exposed by the Job Actor.
     */
    public final byte[] getSenderImage() {
        return senderImage;
    }

    /**
     * @return a string representing the destination public key.
     */
    public final String getDestinationPublicKey() {
        return destinationPublicKey;
    }

    /**
     * @return an element of RequestType indicating the type of the Job Actor connection request.
     */
    public final RequestType getRequestType() {
        return requestType;
    }

    /**
     * @return an element of ProtocolState indicating the state of the Job Actor connection request.
     */
    public ProtocolState getProtocolState() {
        return protocolState;
    }

    /**
     * @return an element of ConnectionRequestAction enum indicating the action that must to be done.
     */
    public final ConnectionRequestAction getRequestAction() {
        return requestAction;
    }

    /**
     * @return the time when the action was performed.
     */
    public final long getSentTime() {
        return sentTime;
    }

    @Override
    public String toString() {
        return "CryptoBrokerConnectionRequest{" +
                "requestId=" + requestId +
                ", senderPublicKey='" + senderPublicKey + '\'' +
                ", senderActorType=" + senderActorType +
                ", senderAlias='" + senderAlias + '\'' +
                ", senderImage=" + (senderImage != null )+
                ", destinationPublicKey='" + destinationPublicKey + '\'' +
                ", requestType=" + requestType +
                ", protocolState=" + protocolState +
                ", requestAction=" + requestAction +
                ", sentTime=" + sentTime +
                '}';
    }
}
