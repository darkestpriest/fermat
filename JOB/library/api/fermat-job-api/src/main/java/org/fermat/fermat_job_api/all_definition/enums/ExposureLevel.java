package org.fermat.fermat_job_api.all_definition.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 20/06/16.
 */
public enum ExposureLevel implements FermatEnum {

    /**
     * In order to do make code more readable, please keep the elements in the Enum sorted alphabetically.
     */
    HIDE    ("HID"),
    PUBLISH ("PUB"),
    ;

    private final String code;

    ExposureLevel(final String code) {
        this.code = code;
    }

    public static ExposureLevel getByCode(String code) throws InvalidParameterException {

        switch (code) {

            case "HID": return HIDE   ;
            case "PUB": return PUBLISH;

            default:
                throw new InvalidParameterException(
                        "Code Received: " + code,
                        "The received code is not valid for the ExposureLevel enum"
                );
        }
    }

    @Override
    public String getCode() {
        return this.code;
    }

}

