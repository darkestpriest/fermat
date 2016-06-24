package org.fermat.fermat_job_core.layer.sub_app_module;

import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractLayer;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantRegisterPluginException;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartLayerException;

import org.fermat.fermat_job_core.layer.sub_app_module.job_seeker_community.JobSeekerCommunitySubAppModulePluginSubsystem;
import org.fermat.fermat_job_core.layer.sub_app_module.job_seeker_identity.JobSeekerSubAppModulePluginSubsystem;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 21/06/16.
 */
public class SubAppModuleLayer extends AbstractLayer {

    public SubAppModuleLayer() {
        super(Layers.SUB_APP_MODULE);
    }

    public void start() throws CantStartLayerException {

        try {

            registerPlugin(new JobSeekerCommunitySubAppModulePluginSubsystem());
            registerPlugin(new JobSeekerSubAppModulePluginSubsystem());

        } catch(CantRegisterPluginException e) {

            throw new CantStartLayerException(
                    e,
                    "",
                    "Problem trying to register a plugin."
            );
        }
    }
}
