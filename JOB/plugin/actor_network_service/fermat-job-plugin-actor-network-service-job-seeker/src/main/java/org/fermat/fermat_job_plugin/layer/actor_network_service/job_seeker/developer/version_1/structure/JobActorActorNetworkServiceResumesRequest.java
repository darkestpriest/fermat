package org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.world.interfaces.Currency;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    private final String jobSeekerPublicKey;
    private final long updateTime;
    private final RequestType type;
    private final ProtocolState state;
    private final List<Resume> resumeList;

    /**
     * Default constructor with parameters
     * @param requestId
     * @param requesterPublicKey
     * @param requesterActorType
     * @param jobSeekerPublicKey
     * @param updateTime
     * @param type
     * @param state
     * @param resumeList
     */
    public JobActorActorNetworkServiceResumesRequest(
            UUID requestId,
            String requesterPublicKey,
            Actors requesterActorType,
            String jobSeekerPublicKey,
            long updateTime,
            List<Resume> resumeList,
            RequestType type,
            ProtocolState state
            ) {
        super(MessageType.RESUME_REQUEST);
        this.requestId = requestId;
        this.requesterPublicKey = requesterPublicKey;
        this.requesterActorType = requesterActorType;
        this.jobSeekerPublicKey = jobSeekerPublicKey;
        this.updateTime = updateTime;
        this.type = type;
        this.state = state;
        this.resumeList = resumeList;
    }

    /**
     * This method converts this class to a json String
     * @return
     */
    @Override
    public String toJson() {

        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    /**
     * This method returns a JobActorActorNetworkServiceResumesRequest from a Json
     * @param jsonMessage
     * @return
     */
    public static JobActorActorNetworkServiceResumesRequest fromJson(String jsonMessage) {

        Gson gson = new GsonBuilder().create();

        return gson.fromJson(
                jsonMessage,
                JobActorActorNetworkServiceResumesRequest.class);
    }

    /**
     * This method returns the request id.
     * @return
     */
    @Override
    public UUID getRequestId() {
        return requestId;
    }

    /**
     * This method returns the requester public key.
     * @return
     */
    @Override
    public String getRequesterPublicKey() {
        return requesterPublicKey;
    }

    /**
     * This method returns the requester actor type
     * @return
     */
    @Override
    public Actors getRequesterActorType() {
        return requesterActorType;
    }

    /**
     * This method returns the job seeker public key
     * @return
     */
    @Override
    public String getJobSeekerPublicKey() {
        return jobSeekerPublicKey;
    }

    /**
     * This method returns the update time
     * @return
     */
    @Override
    public long getUpdateTime() {
        return updateTime;
    }

    /**
     * This method returns the resume list.
     * @return
     */
    @Override
    public List<Resume> listInformation() {
        return resumeList;
    }

    /**
     * This method returns the request type
     * @return
     */
    public RequestType getType() {
        return type;
    }

    /**
     * This method returns the protocol state
     * @return
     */
    public ProtocolState getState() {
        return state;
    }

    @Override
    public String toString() {
        return "JobActorActorNetworkServiceResumesRequest{" +
                "requestId=" + requestId +
                ", requesterPublicKey='" + requesterPublicKey + '\'' +
                ", requesterActorType=" + requesterActorType +
                ", cryptoBrokerPublicKey='" + jobSeekerPublicKey + '\'' +
                ", updateTime=" + updateTime +
                ", type=" + type +
                ", state=" + state +
                ", resumeList=" + resumeList +
                '}';
    }
}
