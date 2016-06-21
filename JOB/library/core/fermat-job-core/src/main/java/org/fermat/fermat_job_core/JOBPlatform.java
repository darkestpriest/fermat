package org.fermat.fermat_job_core;

import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PlatformReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractPlatform;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantRegisterLayerException;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartPlatformException;

import org.fermat.fermat_job_core.layer.actor_network_service.ActorNetworkServiceLayer;
import org.fermat.fermat_job_core.layer.identity.IdentityLayer;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 18/06/16.
 */
public class JOBPlatform extends AbstractPlatform {

    public JOBPlatform() {
        super(new PlatformReference(Platforms.JOB_PLATFORM));
    }

    @Override
    public void start() throws CantStartPlatformException {

        try {
            registerLayer(new ActorNetworkServiceLayer());
            registerLayer(new IdentityLayer());

        } catch (CantRegisterLayerException e) {

            throw new CantStartPlatformException(
                    e,
                    "JOB Platform.",
                    "Problem trying to register a layer."
            );
        }
    }
}

