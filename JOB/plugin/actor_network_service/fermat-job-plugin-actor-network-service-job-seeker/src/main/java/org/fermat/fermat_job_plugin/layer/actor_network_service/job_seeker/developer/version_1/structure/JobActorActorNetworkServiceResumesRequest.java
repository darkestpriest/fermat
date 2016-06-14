package org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;

import org.fermat.fermat_job_api.all_definition.enums.MessageType;
import org.fermat.fermat_job_api.all_definition.enums.ProtocolState;
import org.fermat.fermat_job_api.all_definition.enums.RequestType;
import org.fermat.fermat_job_api.all_definition.interfaces.Resume;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces.JobSeekerExtraData;
import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.messages.NetworkServiceMessage;

import java.util.List;
import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 14/06/16.
 */
public class JobActorActorNetworkServiceResumesRequest extends NetworkServiceMessage implements JobSeekerExtraData<Resume>{

    private final UUID requestId;
    private final String requesterPublicKey;
    private final Actors requesterActorType;
    private final String cryptoBrokerPublicKey;
    private final long updateTime;
    private final RequestType type;
    private final ProtocolState state;
    private final List<Resume> resumeList;

    public JobActorActorNetworkServiceResumesRequest(
            UUID requestId,
            String requesterPublicKey,
            Actors requesterActorType,
            String cryptoBrokerPublicKey,
            long updateTime,
            RequestType type,
            ProtocolState state,
            List<Resume> resumeList) {
        super(MessageType.RESUME_REQUEST);
        this.requestId = requestId;
        this.requesterPublicKey = requesterPublicKey;
        this.requesterActorType = requesterActorType;
        this.cryptoBrokerPublicKey = cryptoBrokerPublicKey;
        this.updateTime = updateTime;
        this.type = type;
        this.state = state;
        this.resumeList = resumeList;
    }

    @Override
    public UUID getRequestId() {
        return requestId;
    }

    @Override
    public String getRequesterPublicKey() {
        return requesterPublicKey;
    }

    @Override
    public Actors getRequesterActorType() {
        return requesterActorType;
    }

    public String getCryptoBrokerPublicKey() {
        return cryptoBrokerPublicKey;
    }

    @Override
    public long getUpdateTime() {
        return updateTime;
    }

    public RequestType getType() {
        return type;
    }

    public ProtocolState getState() {
        return state;
    }

    public List<Resume> getResumeList() {
        return resumeList;
    }

    @Override
    public String toString() {
        return "JobActorActorNetworkServiceResumesRequest{" +
                "requestId=" + requestId +
                ", requesterPublicKey='" + requesterPublicKey + '\'' +
                ", requesterActorType=" + requesterActorType +
                ", cryptoBrokerPublicKey='" + cryptoBrokerPublicKey + '\'' +
                ", updateTime=" + updateTime +
                ", type=" + type +
                ", state=" + state +
                ", resumeList=" + resumeList +
                '}';
    }
}
