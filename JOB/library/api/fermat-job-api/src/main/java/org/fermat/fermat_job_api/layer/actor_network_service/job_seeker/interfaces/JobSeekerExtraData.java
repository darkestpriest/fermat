package org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;

import java.util.List;
import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/06/16.
 */
public interface JobSeekerExtraData<T>{

    /**
     * This method returns the request Id.
     * @return
     */
    UUID getRequestId();

    /**
     * This method returns the requester Public Key
     * @return
     */
    String getRequesterPublicKey();

    /**
     * This method returns the requester Actor Type
     * @return
     */
    Actors getRequesterActorType();

    /**
     * This method returns the Job Seeker public key that respond this request
     * @return
     */
    String getJobSeekerPublicKey();

    /**
     * This method returns the update time
     * @return
     */
    long getUpdateTime();

    /**
     * This method returns the information List
     * @return
     */
    List<T> listInformation();

}
