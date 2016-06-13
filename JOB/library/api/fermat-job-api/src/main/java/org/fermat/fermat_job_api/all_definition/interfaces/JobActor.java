package org.fermat.fermat_job_api.all_definition.interfaces;

import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;

import java.io.Serializable;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 13/06/16.
 */
public interface JobActor extends Serializable {

    String getAlias();

    String getPublicKey();

    byte[] getProfileImage();

    ConnectionState getContactState();

}
