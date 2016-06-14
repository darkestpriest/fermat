package org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.messages;

import com.bitdubai.fermat_api.layer.all_definition.util.Base64;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.fermat.fermat_job_api.all_definition.enums.MessageType;

import java.lang.reflect.Type;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 14/06/16.
 */
public class NetworkServiceMessage {

    private MessageType messageType;

    public static final Gson customGson = new GsonBuilder()
            .registerTypeHierarchyAdapter(
                    byte[].class,
                    new ByteArrayToBase64TypeAdapter()).create();

    private static class ByteArrayToBase64TypeAdapter
            implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
        public byte[] deserialize(
                JsonElement json,
                Type typeOfT,
                JsonDeserializationContext context)
                throws JsonParseException {
            return Base64.decode(json.getAsString(), Base64.NO_WRAP);
        }

        public JsonElement serialize(
                byte[] src,
                Type typeOfSrc,
                JsonSerializationContext context) {
            return new JsonPrimitive(Base64.encodeToString(src, Base64.NO_WRAP));
        }
    }

    /**
     * Default constructor
     */
    public NetworkServiceMessage() {
        //Not implemented in this version
    }

    /**
     * Constructor with parameters
     * @param messageType
     */
    public NetworkServiceMessage(final MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * This method returns this message in Json format.
     * @return
     */
    public String toJson() {

        return NetworkServiceMessage.customGson.toJson(this);
    }

    /**
     * This method returns the MessageType
     * @return
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * This method creates a NetworkServiceMessage
     * @param jsonObject
     * @param gson
     */
    private NetworkServiceMessage(JsonObject jsonObject, Gson gson) {

        this.messageType = gson.fromJson(
                jsonObject.get("messageType").getAsString(),
                MessageType.class);

    }

    /**
     * This method returns a NetworkServiceMessage from a Json
     * @param jsonString
     * @return
     */
    public static NetworkServiceMessage fromJson(String jsonString){

        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString);
        return new NetworkServiceMessage(jsonObject, gson);
    }

    @Override
    public String toString() {
        return "NetworkServiceMessage{" +
                "messageType=" + messageType +
                '}';
    }

}
