package org.fermat.fermat_job_api.layer.identity.job_seeker.util;

import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.AsymmetricCryptography;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.interfaces.KeyPair;

import org.fermat.fermat_job_api.all_definition.enums.ExposureLevel;
import org.fermat.fermat_job_api.all_definition.enums.Frequency;
import org.fermat.fermat_job_api.all_definition.enums.JobTitle;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantCreateMessageSignatureException;
import org.fermat.fermat_job_api.layer.identity.job_seeker.interfaces.JobSeeker;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 20/06/16.
 */
public class JobSeekerRecord implements JobSeeker, Serializable {

    private final String alias;
    private final KeyPair keyPair;
    private byte[] profileImage;
    private final ExposureLevel exposureLevel;
    private final long accuracy;
    private final Frequency frequency;
    private final JobTitle jobTitle;

    /**
     * Default constructor with parameters.
     * This can be used to create an identity from GUI.
     * @param alias
     * @param profileImage
     * @param exposureLevel
     * @param accuracy
     * @param frequency
     */
    public JobSeekerRecord(
            String alias,
            byte[] profileImage,
            ExposureLevel exposureLevel,
            long accuracy,
            Frequency frequency,
            JobTitle jobTitle) {
        this.alias = alias;
        this.profileImage = profileImage;
        this.exposureLevel = exposureLevel;
        this.accuracy = accuracy;
        this.frequency = frequency;
        this.keyPair = new ECCKeyPair();
        this.jobTitle = jobTitle;
    }

    /**
     * Constructor with parameters.
     * This can be used when we need to create an identity from the Database
     * @param alias
     * @param keyPair
     * @param exposureLevel
     * @param accuracy
     * @param frequency
     * @param profileImage
     */
    public JobSeekerRecord(
            String alias,
            KeyPair keyPair,
            ExposureLevel exposureLevel,
            long accuracy,
            Frequency frequency,
            byte[] profileImage,
            JobTitle jobTitle) {
        this.alias = alias;
        this.keyPair = keyPair;
        this.exposureLevel = exposureLevel;
        this.accuracy = accuracy;
        this.frequency = frequency;
        this.profileImage = profileImage;
        this.jobTitle = jobTitle;
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
        return keyPair.getPublicKey();
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

    /**
     * This method returns the exposure level from this identity.
     * @return
     */
    @Override
    public ExposureLevel getExposureLevel() {
        return exposureLevel;
    }

    /**
     * This method returns the accuracy
     * @return
     */
    @Override
    public long getAccuracy() {
        return accuracy;
    }

    /**
     * This method returns the frequency.
     * @return
     */
    @Override
    public Frequency getFrequency() {
        return frequency;
    }

    /**
     * This method creates a message signature.
     * @param message
     * @return
     * @throws CantCreateMessageSignatureException
     */
    @Override
    public String createMessageSignature(String message)
            throws CantCreateMessageSignatureException {
        try{
            return AsymmetricCryptography.createMessageSignature(
                    message,
                    this.keyPair.getPrivateKey());
        } catch(Exception ex){
            throw new CantCreateMessageSignatureException(
                    CantCreateMessageSignatureException.DEFAULT_MESSAGE,
                    ex,
                    "Message: "+ message,
                    "The message could be invalid");
        }
    }

    @Override
    public JobTitle getJobTitle() {
        return jobTitle;
    }

    @Override
    public String toString() {
        return "JobSeekerRecord{" +
                "alias='" + alias + '\'' +
                ", keyPair=" + keyPair +
                ", profileImage=" + Arrays.toString(profileImage) +
                ", exposureLevel=" + exposureLevel +
                ", accuracy=" + accuracy +
                ", frequency=" + frequency +
                '}';
    }
}
