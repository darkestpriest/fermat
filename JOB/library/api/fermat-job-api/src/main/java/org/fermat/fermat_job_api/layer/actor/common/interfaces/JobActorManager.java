package org.fermat.fermat_job_api.layer.actor.common.interfaces;

import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;

import org.fermat.fermat_job_api.all_definition.interfaces.JobActor;

import java.util.List;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 13/06/16.
 */
public interface JobActorManager<T extends JobActor> {

    /**
     * This method returns the connected actors
     * @param linkedIdentityPublicKey
     * @return
     */
    List<T> getConnectedActors(String linkedIdentityPublicKey);

    /**
     * This method returns true if the actor, by the given actorPublicKey, is connected
     * @param actorPublicKey
     * @return
     */
    boolean isActorConnected(String actorPublicKey);

    /**
     * This method returns the connection state by a given actor public key
     * @param actorPublicKey
     * @return
     */
    ConnectionState getActorConnectionStatus(String actorPublicKey);

    /**
     * This method returns the Actors list by a given connection state
     * @param linkedIdentityPublicKey
     * @param connectionState
     * @return
     */
    List<T> getActorsByConnectionState(
            String linkedIdentityPublicKey,
            ConnectionState connectionState);

}
