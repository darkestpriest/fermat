package org.fermat.fermat_job_api.all_definition.events.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEventEnum;
import com.bitdubai.fermat_api.layer.all_definition.events.common.GenericEventListener;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventListener;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventMonitor;

import org.fermat.fermat_job_api.all_definition.events.GenericJOBFermatEvent;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.events.JobSeekerConnectionRequestNewsEvent;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.events.JobSeekerConnectionRequestUpdatesEvent;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 18/06/16.
 */
public enum EventType implements FermatEventEnum {
    /**
     * Please for doing the code more readable, keep the elements of the enum ordered.
     */
    JOB_SEEKER_CONNECTION_REQUEST_UPDATES("JSCRU"){
        public final FermatEvent getNewEvent() {
            return new JobSeekerConnectionRequestUpdatesEvent(this); }
    },
    JOB_SEEKER_CONNECTION_REQUEST_NEWS("JSCRN"){
        public final FermatEvent getNewEvent() {
            return new JobSeekerConnectionRequestNewsEvent(this); }
    },
    JOB_SEEKER_RESUME_REQUEST_NEWS("JSRRN"){
        public final FermatEvent getNewEvent() {
            return new GenericJOBFermatEvent(this); }
    },
    JOB_SEEKER_RESUME_REQUEST_UPDATES("JSRRU"){
        public final FermatEvent getNewEvent() {
            return new GenericJOBFermatEvent(this); }
    }
    ;

    private final String code;

    EventType(String code) {
        this.code = code;
    }

    @Override // by default
    public FermatEventListener getNewListener(FermatEventMonitor fermatEventMonitor) {
        return new GenericEventListener(
                this,
                fermatEventMonitor); }

    @Override
    public final String getCode() {
        return this.code;
    }

    @Override
    public final Platforms getPlatform() {
        return Platforms.JOB_PLATFORM;
    }
}
