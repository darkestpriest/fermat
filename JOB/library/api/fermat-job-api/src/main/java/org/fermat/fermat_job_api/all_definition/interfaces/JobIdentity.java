package org.fermat.fermat_job_api.all_definition.interfaces;

/**
 * This interface represents the identity used in JOB platform.
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/06/16.
 */
public interface JobIdentity {

    /**
     * This method returns the identity alias.
     * @return
     */
    String getAlias();

    /**
     * This method returns the identity public key.
     * @return
     */
    String getPublicKey();

    /**
     * This method returns the profile image.
     * @return
     */
    byte[] getProfileImage();

    /**
     * This method saves a profile image
     * @param imageBytes
     */
    void setNewProfileImage(final byte[] imageBytes);

}
