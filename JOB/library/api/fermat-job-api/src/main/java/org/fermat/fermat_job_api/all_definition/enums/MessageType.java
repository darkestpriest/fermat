package org.fermat.fermat_job_api.all_definition.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 14/06/16.
 */
public enum MessageType implements FermatEnum {

    /**
     * Please for doing the code more readable, keep the elements of the enum ordered.
     */
    CONNECTION_INFORMATION ("INF"),
    CONNECTION_REQUEST     ("REQ"),
    RESUME_REQUEST         ("RRE"),

    ;

    private String code;

    MessageType(String code) {
        this.code = code;
    }

    public static MessageType getByCode(String code) throws InvalidParameterException {

        switch (code) {

            case "INF": return CONNECTION_INFORMATION;
            case "REQ": return CONNECTION_REQUEST    ;
            case "RRE": return RESUME_REQUEST        ;

            default:
                throw new InvalidParameterException(
                        "Code Received: " + code,
                        "This code is not valid for the MessageType enum."
                );
        }

    }

    @Override
    public String getCode() {
        return this.code;
    }

}
