package com.bitdubai.fermat_cht_api.layer.middleware.interfaces;

import java.util.UUID;

/**
 * Created by franklin on 31/03/16.
 */
public interface Group {
    /**
     * Returns the group id that representing the id of the group to which the user belongs
     * @return groupId
     */
    UUID getGroupId();
    /**
     * The method <code>getNameGroup</code> gives us the public key of the represented a Actor
     *
     * @return the publicKey
     */
    String getNameGroup();
}
