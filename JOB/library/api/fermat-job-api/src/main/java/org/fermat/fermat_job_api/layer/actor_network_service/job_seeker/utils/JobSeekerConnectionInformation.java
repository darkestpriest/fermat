package org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.utils;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 13/06/16.
 */
public class JobSeekerConnectionInformation {

    private final UUID connectionId;
    private final String senderPublicKey;
    private final Actors senderActorType;
    private final String senderAlias;
    private final byte[] senderImage;
    private final String destinationPublicKey;
    private final long sendingTime;

    /**
     * Default constructor with parameters.
     * @param connectionId
     * @param senderPublicKey
     * @param senderActorType
     * @param senderAlias
     * @param senderImage
     * @param destinationPublicKey
     * @param sendingTime
     */
    public JobSeekerConnectionInformation(
            UUID connectionId,
            String senderPublicKey,
            Actors senderActorType,
            String senderAlias,
            byte[] senderImage,
            String destinationPublicKey,
            long sendingTime) {
        this.connectionId = connectionId;
        this.senderPublicKey = senderPublicKey;
        this.senderActorType = senderActorType;
        this.senderAlias = senderAlias;
        this.senderImage = senderImage;
        this.destinationPublicKey = destinationPublicKey;
        this.sendingTime = sendingTime;
    }

    /**
     * This method returns the connection Id
     * @return
     */
    public UUID getConnectionId() {
        return connectionId;
    }

    /**
     * This method returns the sender public key
     * @return
     */
    public String getSenderPublicKey() {
        return senderPublicKey;
    }

    /**
     * This method returns the sender actor type.
     * @return
     */
    public Actors getSenderActorType() {
        return senderActorType;
    }

    /**
     * This method returns the sender alias
     * @return
     */
    public String getSenderAlias() {
        return senderAlias;
    }

    /**
     * This method returns the sender image
     * @return
     */
    public byte[] getSenderImage() {
        return senderImage;
    }

    /**
     * This method returns the destination public key
     * @return
     */
    public String getDestinationPublicKey() {
        return destinationPublicKey;
    }

    /**
     * This method returns the sending time.
     * @return
     */
    public long getSendingTime() {
        return sendingTime;
    }

    @Override
    public String toString() {
        return "JobSeekerConnectionInformation{" +
                "connectionId=" + connectionId +
                ", senderPublicKey='" + senderPublicKey + '\'' +
                ", senderActorType=" + senderActorType +
                ", senderAlias='" + senderAlias + '\'' +
                ", senderImage=" + Arrays.toString(senderImage) +
                ", destinationPublicKey='" + destinationPublicKey + '\'' +
                ", sendingTime=" + sendingTime +
                '}';
    }
}
