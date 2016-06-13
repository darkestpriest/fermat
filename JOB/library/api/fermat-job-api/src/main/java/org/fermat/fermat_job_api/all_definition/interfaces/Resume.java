package org.fermat.fermat_job_api.all_definition.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;

import java.io.Serializable;
import java.util.UUID;

/**
 * This class represents a Job Seeker resume (curriculum vitae)
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/06/16.
 */
public interface Resume extends Serializable {

    /**
     * This method returns the Job Seeker identity.
     * @return
     */
    String getActorPublicKey();

    /**
     * This method returns the actor type
     * @return
     */
    Actors getActorType();

    /**
     * This method returns the resume Id
     * @return
     */
    UUID getResumeId();

}
