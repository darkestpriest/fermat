package org.fermat.fermat_job_core.layer.actor_network_service;

import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractLayer;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantRegisterPluginException;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartLayerException;

import org.fermat.fermat_job_core.layer.actor_network_service.job_seeker.JobSeekerPluginSubsystem;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 18/06/16.
 */
public class ActorNetworkServiceLayer extends AbstractLayer {

    public ActorNetworkServiceLayer() {
        super(Layers.ACTOR_NETWORK_SERVICE);
    }

    public void start() throws CantStartLayerException {

        try {

            registerPlugin(new JobSeekerPluginSubsystem()  );

        } catch(CantRegisterPluginException e) {

            throw new CantStartLayerException(
                    e,
                    "",
                    "Problem trying to register a plugin."
            );
        }
    }

}
