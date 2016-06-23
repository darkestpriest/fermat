package org.fermat.fermat_job_plugin.layer.sub_app_module.job_seeker_community.developer.version_1.util;

import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;

import org.fermat.fermat_job_api.all_definition.enums.JobTitle;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.utils.JobSeekerExposingData;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunityInformation;

import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 23/06/16.
 */
public class JobSeekerCommunityPluginInformation implements JobSeekerCommunityInformation {

    private final JobTitle jobTitle;
    private final String publicKey;
    private final String alias;
    private final byte[] image;
    private final ConnectionState connectionState;
    private final UUID connectionId;

    /**
     * Default constructor with JobSeekerExposingData as parameter.
     * @param jobSeekerExposingData
     */
    public JobSeekerCommunityPluginInformation(JobSeekerExposingData jobSeekerExposingData) {
        this.jobTitle = jobSeekerExposingData.getExtraDataEnum();
        this.publicKey = jobSeekerExposingData.getPublicKey();
        this.alias = jobSeekerExposingData.getAlias();
        this.image = jobSeekerExposingData.getImage();
        this.connectionState = null;
        this.connectionId = null;
    }

    /**
     * This method returns the job title
     * @return
     */
    @Override
    public JobTitle getJobTitle() {
        return this.jobTitle;
    }

    /**
     * This method returns the public key.
     * @return
     */
    @Override
    public String getPublicKey() {
        return this.publicKey;
    }

    /**
     * This method returns alias.
     * @return
     */
    @Override
    public String getAlias() {
        return this.alias;
    }

    /**
     * This method returns the image profile.
     * @return
     */
    @Override
    public byte[] getImage() {
        return this.image;
    }

    /**
     * This method returns the connection state.
     * @return
     */
    @Override
    public ConnectionState getConnectionState() {
        return this.connectionState;
    }

    /**
     * This method returns the connection id.
     * @return
     */
    @Override
    public UUID getConnectionId() {
        return this.connectionId;
    }
}
