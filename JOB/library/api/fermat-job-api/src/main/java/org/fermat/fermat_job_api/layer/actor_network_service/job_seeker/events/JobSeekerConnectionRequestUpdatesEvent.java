package org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.events;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEventEnum;
import com.bitdubai.fermat_api.layer.all_definition.events.common.AbstractEvent;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 18/06/16.
 */
public class JobSeekerConnectionRequestUpdatesEvent extends AbstractEvent {

    private Actors destinationActorType;

    /**
     * Default constructor with parameters
     * @param eventType
     */
    public JobSeekerConnectionRequestUpdatesEvent(final FermatEventEnum eventType) {
        super(eventType);
    }

    /**
     * This method returns the destination actor type
     * @return
     */
    public final Actors getDestinationActorType() {
        return destinationActorType;
    }

    /**
     * This method sets the destination actor type
     * @param destinationActorType
     */
    public final void setDestinationActorType(final Actors destinationActorType) {
        this.destinationActorType = destinationActorType;
    }
}
