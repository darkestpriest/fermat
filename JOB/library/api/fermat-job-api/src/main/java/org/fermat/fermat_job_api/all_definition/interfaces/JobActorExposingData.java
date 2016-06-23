package org.fermat.fermat_job_api.all_definition.interfaces;

import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/06/16.
 */
public abstract class JobActorExposingData<T extends JobEnum> implements Serializable{

    /**
     * Class objects
     */
    private final String publicKey;
    private final String alias;
    private final byte[] image;
    private final Location location;
    public T jobEnum;

    /**
     * Constructor with parameters
     * @param publicKey
     * @param alias
     * @param image
     */
    public JobActorExposingData(
            final String publicKey,
            final String alias,
            final byte[] image,
            Location location) {
        this.publicKey = publicKey;
        this.alias = alias;
        this.image = image;
        this.location = location;
    }

    public JobActorExposingData(
            final String publicKey,
            final String alias,
            final byte[] image,
            final Location location,
            final T jobEnum){
        this.publicKey = publicKey;
        this.alias = alias;
        this.image = image;
        this.location = location;
        this.jobEnum = jobEnum;
    }

    /**
     * @return a string representing the public key.
     */
    public final String getPublicKey() {
        return publicKey;
    }

    /**
     * @return a string representing the alias of the job seeker.
     */
    public final String getAlias() {
        return alias;
    }

    /**
     * @return an array of bytes with the image exposed by the job seeker.
     */
    public final byte[] getImage() {
        return image;
    }

    /**
     * This method returns the location
     * @return
     */
    public Location getLocation() {
        return location;
    }

    /**
     * This method returns the extra data enum
     * @return
     */
    public abstract T getExtraDataEnum();

    @Override
    public String toString() {
        return "CryptoBrokerExposingData{" +
                "publicKey='" + publicKey + '\'' +
                ", alias='" + alias + '\'' +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}
