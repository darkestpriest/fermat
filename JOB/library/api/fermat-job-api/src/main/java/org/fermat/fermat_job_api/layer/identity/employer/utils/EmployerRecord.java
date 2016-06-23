package org.fermat.fermat_job_api.layer.identity.employer.utils;

import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.AsymmetricCryptography;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.interfaces.KeyPair;

import org.fermat.fermat_job_api.all_definition.enums.ExposureLevel;
import org.fermat.fermat_job_api.all_definition.enums.Frequency;
import org.fermat.fermat_job_api.all_definition.enums.Industry;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantCreateMessageSignatureException;
import org.fermat.fermat_job_api.layer.identity.employer.interfaces.Employer;

import java.io.Serializable;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 22/06/16.
 */
public class EmployerRecord implements Employer, Serializable {

    private final String alias;
    private final KeyPair keyPair;
    private byte[] profileImage;
    private final ExposureLevel exposureLevel;
    private final long accuracy;
    private final Frequency frequency;
    private final Industry industry;

    /**
     * Constructor with parameters.
     * This can be used when we need to create an identity from the Database
     * @param alias
     * @param keyPair
     * @param exposureLevel
     * @param accuracy
     * @param frequency
     * @param industry
     * @param profileImage
     */
    public EmployerRecord(
            String alias,
            KeyPair keyPair,
            ExposureLevel exposureLevel,
            long accuracy,
            Frequency frequency,
            Industry industry,
            byte[] profileImage) {
        this.alias = alias;
        this.keyPair = keyPair;
        this.exposureLevel = exposureLevel;
        this.accuracy = accuracy;
        this.frequency = frequency;
        this.industry = industry;
        this.profileImage = profileImage;
    }

    /**
     * Default constructor with parameters.
     * This can be used to create an identity from GUI.
     * @param alias
     * @param exposureLevel
     * @param accuracy
     * @param frequency
     * @param industry
     * @param profileImage
     */
    public EmployerRecord(
            String alias,
            ExposureLevel exposureLevel,
            long accuracy,
            Frequency frequency,
            Industry industry,
            byte[] profileImage) {
        this.alias = alias;
        this.exposureLevel = exposureLevel;
        this.accuracy = accuracy;
        this.frequency = frequency;
        this.industry = industry;
        this.profileImage = profileImage;
        this.keyPair = new ECCKeyPair();
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
     * This method returns the profile image.
     * @return
     */
    @Override
    public byte[] getProfileImage() {
        return profileImage;
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
     * This method returns the identity industry.
     * @return
     */
    @Override
    public Industry getIndustry() {
        return industry;
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

    /**
     * This method saves a profile image
     * @param imageBytes
     */
    @Override
    public void setNewProfileImage(byte[] imageBytes) {
        this.profileImage = imageBytes;
    }
}
