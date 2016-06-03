package org.fermat.fermat_job_api.all_definition.interfaces;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/06/16.
 */
public abstract class JobActorExposingData implements Serializable{

    /**
     * Class objects
     */
    private final String publicKey;
    private final String alias;
    private final byte[] image;

    /**
     * Constructor with parameters
     * @param publicKey
     * @param alias
     * @param image
     */
    public JobActorExposingData(
            final String publicKey,
            final String alias,
            final byte[] image) {
        this.publicKey = publicKey;
        this.alias = alias;
        this.image = image;
    }

    /**
     * @return a string representing the public key.
     */
    public final String getPublicKey() {
        return publicKey;
    }

    /**
     * @return a string representing the alias of the crypto broker.
     */
    public final String getAlias() {
        return alias;
    }

    /**
     * @return an array of bytes with the image exposed by the Crypto Broker.
     */
    public final byte[] getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "CryptoBrokerExposingData{" +
                "publicKey='" + publicKey + '\'' +
                ", alias='" + alias + '\'' +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}
