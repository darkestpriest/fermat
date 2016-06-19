package org.fermat.fermat_job_api.all_definition.events;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEventEnum;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;

import org.fermat.fermat_job_api.all_definition.events.enums.EventType;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 18/06/16.
 */
public class GenericJOBFermatEvent implements FermatEvent {

    private EventType eventType;

    private EventSource eventSource;

    /**
     * Default constructor with parameters
     * @param eventType
     */
    public GenericJOBFermatEvent(EventType eventType){
        this.eventType=eventType;
    }

    /**
     * This method returns returns the event type
     * @return
     */
    @Override
    public FermatEventEnum getEventType() {
        return this.eventType;
    }

    /**
     * This method sets the event type
     * @param eventSource an element of event source enum in reference to the source of the event
     */
    @Override
    public void setSource(EventSource eventSource) {
        this.eventSource=eventSource;
    }

    /**
     * This method returns the event source
     * @return
     */
    @Override
    public EventSource getSource() {
        return this.eventSource;
    }

    @Override
    public String toString() {
        return "GenericCBPFermatEvent{" +
                "eventType=" + eventType +
                ", eventSource=" + eventSource +
                '}';
    }
}
