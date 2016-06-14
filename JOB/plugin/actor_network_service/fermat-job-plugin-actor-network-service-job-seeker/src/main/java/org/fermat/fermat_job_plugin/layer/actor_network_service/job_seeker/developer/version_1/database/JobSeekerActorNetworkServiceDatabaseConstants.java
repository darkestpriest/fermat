package org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.database;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 13/06/16.
 */
public class JobSeekerActorNetworkServiceDatabaseConstants {

    /**
     * Job Seeker Actor Network Service database global definition.
     */
    public static final String JOB_SEEKER_ACTOR_NETWORK_SERVICE_DATABASE_NAME  = "job_seeker_actor_network_service";

    /**
     * Connection News database table definition.
     */
    public static final String CONNECTION_NEWS_TABLE_NAME = "connection_news";

    public static final String CONNECTION_NEWS_REQUEST_ID_COLUMN_NAME = "request_id";
    public static final String CONNECTION_NEWS_SENDER_PUBLIC_KEY_COLUMN_NAME = "sender_public_key";
    public static final String CONNECTION_NEWS_SENDER_ACTOR_TYPE_COLUMN_NAME = "sender_actor_type";
    public static final String CONNECTION_NEWS_SENDER_ALIAS_COLUMN_NAME = "sender_alias";
    public static final String CONNECTION_NEWS_DESTINATION_PUBLIC_KEY_COLUMN_NAME = "destination_public_key";
    public static final String CONNECTION_NEWS_REQUEST_TYPE_COLUMN_NAME = "request_type";
    public static final String CONNECTION_NEWS_REQUEST_STATE_COLUMN_NAME = "request_state";
    public static final String CONNECTION_NEWS_REQUEST_ACTION_COLUMN_NAME = "request_action";
    public static final String CONNECTION_NEWS_SENT_TIME_COLUMN_NAME = "sent_time";

    public static final String CONNECTION_NEWS_FIRST_KEY_COLUMN = "request_id";

    /**
     * Resume Request database table definition.
     */
    public static final String RESUME_REQUEST_TABLE_NAME = "resume_request";

    public static final String RESUME_REQUEST_REQUEST_ID_COLUMN_NAME = "request_id";
    public static final String RESUME_REQUEST_REQUESTER_PUBLIC_KEY_COLUMN_NAME = "requester_public_key";
    public static final String RESUME_REQUEST_REQUESTER_ACTOR_TYPE_COLUMN_NAME = "requester_actor_type";
    public static final String RESUME_REQUEST_JOB_SEEKER_PUBLIC_KEY_COLUMN_NAME = "job_seeker_public_key";
    public static final String RESUME_REQUEST_UPDATE_TIME_COLUMN_NAME = "update_time";
    public static final String RESUME_REQUEST_TYPE_COLUMN_NAME = "type";
    public static final String RESUME_REQUEST_STATE_COLUMN_NAME = "state";

    public static final String RESUME_REQUEST_FIRST_KEY_COLUMN = "request_id";

    /**
     * Resume database table definition.
     */
    public static final String RESUME_TABLE_NAME = "resume";

    public static final String RESUME_REQUEST_ID_COLUMN_NAME = "request_id"; // RELATION WITH QUOTES REQUEST TABLE.
    public static final String RESUME_INNER_ID_COLUMN_NAME = "resume_id";
    public static final String RESUME_OWNER_PUBLIC_KEY_COLUMN_NAME = "owner_public_key";
    public static final String RESUME_XML_STRING_COLUMN_NAME = "resume_xml";

    public static final String QUOTES_FIRST_KEY_COLUMN = "request_id";
}
