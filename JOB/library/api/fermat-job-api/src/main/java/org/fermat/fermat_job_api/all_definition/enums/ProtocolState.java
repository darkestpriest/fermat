package org.fermat.fermat_job_api.all_definition.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/06/16.
 */
public enum ProtocolState implements FermatEnum {

    /**
     * Please for doing the code more readable, keep the elements of the enum ordered.
     */
    DONE                         ("DON"), // final state of request.
    PENDING_LOCAL_ACTION         ("PLA"), // pending local action
    PENDING_REMOTE_ACTION        ("PRA"), // waiting response from the counterpart
    PROCESSING_RECEIVE           ("PCR"), // when an action from the network service is needed receiving.
    PROCESSING_SEND              ("PCS"), // when an action from the network service is needed sending.
    WAITING_RECEIPT_CONFIRMATION ("WRC"), // the ns waits for the receipt confirmation of the counterpart.
    ;

    private final String code;

    ProtocolState(final String code){
        this.code = code;
    }


    //PUBLIC METHODS

    public static ProtocolState getByCode(final String code) throws InvalidParameterException {
        for (ProtocolState value : values()) {
            if (value.getCode().equals(code)) return value;
        }
        throw new InvalidParameterException(
                InvalidParameterException.DEFAULT_MESSAGE,
                null, "Code Received: " + code,
                "This Code Is Not Valid for the ProtocolState enum.");
    }

    //GETTERS AND SETTERS

    @Override
    public final String getCode(){
        return this.code;
    }
}
