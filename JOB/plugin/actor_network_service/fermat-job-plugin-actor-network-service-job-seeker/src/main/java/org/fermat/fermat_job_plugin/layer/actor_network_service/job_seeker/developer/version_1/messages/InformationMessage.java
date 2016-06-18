package org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.messages;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.fermat.fermat_job_api.all_definition.enums.ConnectionRequestAction;
import org.fermat.fermat_job_api.all_definition.enums.MessageType;

import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 18/06/16.
 */
public class InformationMessage extends NetworkServiceMessage {

    private final UUID requestId;
    private final ConnectionRequestAction action   ;

    /**
     * Default constructor
     * @param requestId
     * @param action
     */
    public InformationMessage(
            final UUID requestId,
            final ConnectionRequestAction action) {
        super(MessageType.CONNECTION_INFORMATION);
        this.requestId = requestId;
        this.action    = action   ;
    }

    private InformationMessage(JsonObject jsonObject, Gson gson) {
        super(MessageType.CONNECTION_INFORMATION);
        this.requestId= UUID.fromString(jsonObject.get("requestId").getAsString());
        this.action = gson.fromJson(
                jsonObject.get("action").getAsString(),
                ConnectionRequestAction.class);

    }

    /**
     * This method returns an InformationMessage from a Json String
     * @param jsonString
     * @return
     */
    public static InformationMessage fromJson(String jsonString){
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString);
        return new InformationMessage(jsonObject, gson);
    }

    /**
     * This method returns a Json String from this object
     * @return
     */
    @Override
    public String toJson() {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("messageType", getMessageType().toString());
        jsonObject.addProperty("requestId", requestId.toString());
        jsonObject.addProperty("action", action.toString());
        return gson.toJson(jsonObject);

    }

    /**
     * This method returns the request Id
     * @return
     */
    public UUID getRequestId() {
        return requestId;
    }

    /**
     * This method returns the ConnectionRequestAction
     * @return
     */
    public ConnectionRequestAction getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "InformationMessage{" +
                "requestId=" + requestId +
                ", action=" + action +
                '}';
    }
}
