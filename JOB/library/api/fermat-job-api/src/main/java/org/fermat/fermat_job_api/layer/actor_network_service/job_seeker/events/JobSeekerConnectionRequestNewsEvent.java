package org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.events;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEventEnum;
import com.bitdubai.fermat_api.layer.all_definition.events.common.AbstractEvent;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 18/06/16.
 */
public class JobSeekerConnectionRequestNewsEvent extends AbstractEvent {

    /**
     * Default Constructor with parameters
     * @param eventType
     */
    public JobSeekerConnectionRequestNewsEvent(final FermatEventEnum eventType) {
        super(eventType);
    }
}
