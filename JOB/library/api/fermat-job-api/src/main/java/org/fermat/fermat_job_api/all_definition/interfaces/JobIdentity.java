package org.fermat.fermat_job_api.all_definition.interfaces;

import org.fermat.fermat_job_api.all_definition.enums.ExposureLevel;
import org.fermat.fermat_job_api.all_definition.enums.Frequency;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantCreateMessageSignatureException;

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

    /**
     * This method creates a message signature.
     * @param message
     * @return
     * @throws CantCreateMessageSignatureException
     */
    String createMessageSignature(String message) throws CantCreateMessageSignatureException;

    /**
     * This method returns the accuracy
     * @return
     */
    long getAccuracy();

    /**
     * This method returns the frequency.
     * @return
     */
    Frequency getFrequency();

    /**
     * This method returns the exposure level from this identity.
     * @return
     */
    ExposureLevel getExposureLevel();


}
