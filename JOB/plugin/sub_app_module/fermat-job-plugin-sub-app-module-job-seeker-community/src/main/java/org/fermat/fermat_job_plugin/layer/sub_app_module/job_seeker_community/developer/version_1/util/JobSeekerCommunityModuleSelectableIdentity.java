package org.fermat.fermat_job_plugin.layer.sub_app_module.job_seeker_community.developer.version_1.util;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;

import org.fermat.fermat_job_api.layer.identity.employer.interfaces.Employer;
import org.fermat.fermat_job_api.layer.identity.job_seeker.interfaces.JobSeeker;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunitySelectableIdentity;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 24/06/16.
 */
public class JobSeekerCommunityModuleSelectableIdentity
        implements JobSeekerCommunitySelectableIdentity, Serializable {

    private final String publicKey;
    private final Actors actorType;
    private final String alias;
    private final byte[] image;

    /**
     * Default constructor with parameters.
     * @param publicKey
     * @param actorType
     * @param alias
     * @param image
     */
    public JobSeekerCommunityModuleSelectableIdentity(
            String publicKey,
            Actors actorType,
            String alias,
            byte[] image) {
        this.publicKey = publicKey;
        this.actorType = actorType;
        this.alias = alias;
        this.image = image;
    }

    /**
     * Constructor with JobSeeker interface as parameter
     * @param jobSeeker
     */
    public JobSeekerCommunityModuleSelectableIdentity(JobSeeker jobSeeker){
        this.publicKey = jobSeeker.getPublicKey();
        this.actorType = Actors.JOB_SEEKER;
        this.alias = jobSeeker.getAlias();
        this.image = jobSeeker.getProfileImage();
    }

    /**
     * Constructor with Employer interface as parameter.
     * @param employer
     */
    public JobSeekerCommunityModuleSelectableIdentity(Employer employer){
        this.publicKey = employer.getPublicKey();
        this.actorType = Actors.EMPLOYER;
        this.alias = employer.getAlias();
        this.image = employer.getProfileImage();
    }

    /**
     * This method returns the identity public Key
     * @return
     */
    @Override
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * This method returns the identity actor type
     * @return
     */
    @Override
    public Actors getActorType() {
        return actorType;
    }

    /**
     * This method returns the identity alias.
     * @return
     */
    @Override
    public String getAlias() {
        return alias;
    }

    /**
     * This method returns tha image profile
     * @return
     */
    @Override
    public byte[] getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "JobSeekerCommunityModuleSelectableIdentity{" +
                "publicKey='" + publicKey + '\'' +
                ", actorType=" + actorType +
                ", alias='" + alias + '\'' +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}
