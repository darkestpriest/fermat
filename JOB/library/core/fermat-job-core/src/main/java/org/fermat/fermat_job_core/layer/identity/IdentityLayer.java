package org.fermat.fermat_job_core.layer.identity;

import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractLayer;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantRegisterPluginException;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartLayerException;

import org.fermat.fermat_job_core.layer.identity.employer.EmployerIdentityPluginSubsystem;
import org.fermat.fermat_job_core.layer.identity.job_seeker.JobSeekerIdentityPluginSubsystem;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 21/06/16.
 */
public class IdentityLayer extends AbstractLayer {

    public IdentityLayer() {
        super(Layers.IDENTITY);
    }

    public void start() throws CantStartLayerException {

        try {

            registerPlugin(new EmployerIdentityPluginSubsystem());
            registerPlugin(new JobSeekerIdentityPluginSubsystem());

        } catch(CantRegisterPluginException e) {

            throw new CantStartLayerException(
                    e,
                    "",
                    "Problem trying to register a plugin."
            );
        }
    }

}

