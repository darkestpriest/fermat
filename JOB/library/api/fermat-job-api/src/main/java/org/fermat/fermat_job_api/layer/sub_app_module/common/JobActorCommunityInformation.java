package org.fermat.fermat_job_api.layer.sub_app_module.common;

import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 22/06/16.
 */
public interface JobActorCommunityInformation extends Serializable {

    /**
     * The method <code>getPublicKey</code> returns the public key of the represented job actor
     * @return the public key of the job actor
     */
    String getPublicKey();

    /**
     * The method <code>getAlias</code> returns the name of the represented job actor
     *
     * @return the name of the job actor
     */
    String getAlias();

    /**
     * The method <code>getProfileImage</code> returns the profile image of the represented job actor
     *
     * @return the profile image
     */
    byte[] getImage();

    /**
     * The method <code>getConnectionState</code> returns the Connection State Status
     * @return ConnectionState object
     */
    ConnectionState getConnectionState();

    /**
     * The method <code>getConnectionId</code> returns the Connection UUID this actor has with the
     * selected actor
     * @return UUID object
     */
    UUID getConnectionId();

}
