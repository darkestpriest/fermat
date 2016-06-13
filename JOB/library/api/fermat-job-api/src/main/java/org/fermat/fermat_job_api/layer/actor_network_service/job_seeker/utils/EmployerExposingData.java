package org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.utils;

import org.fermat.fermat_job_api.all_definition.interfaces.JobActorExposingData;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 13/06/16.
 */
public class EmployerExposingData extends JobActorExposingData {

    /**
     * Constructor with parameters
     *
     * @param publicKey
     * @param alias
     * @param image
     */
    public EmployerExposingData(
            String publicKey,
            String alias,
            byte[] image) {
        super(publicKey, alias, image);
    }
}
