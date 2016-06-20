package org.fermat.fermat_job_api.layer.identity.job_seeker.util;

import org.fermat.fermat_job_api.layer.identity.job_seeker.interfaces.JobSeeker;

import java.io.Serializable;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 20/06/16.
 */
public class JobSeekerRecord implements JobSeeker, Serializable {

    private final String alias;
    private final String publicKey;
    private byte[] profileImage;

    /**
     * Default constructor with parameters
     * @param alias
     * @param publicKey
     * @param profileImage
     */
    public JobSeekerRecord(
            String alias,
            String publicKey,
            byte[] profileImage) {
        this.alias = alias;
        this.publicKey = publicKey;
        this.profileImage = profileImage;
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
     * This method returns the identity public key.
     * @return
     */
    @Override
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * This method returns the profile image.
     * @return
     */
    @Override
    public byte[] getProfileImage() {
        return profileImage;
    }

    /**
     * This method saves a profile image
     * @param imageBytes
     */
    @Override
    public void setNewProfileImage(byte[] imageBytes) {
        this.profileImage = imageBytes;
    }
}
