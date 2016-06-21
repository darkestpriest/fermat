package org.fermat.fermat_job_core.layer.sub_app_module.job_seeker_identity;

import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractPluginSubsystem;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartSubsystemException;

import org.fermat.fermat_job_plugin.layer.sub_app_module.job_seeker_identity.developer.DeveloperBitDubai;


/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 21/06/16.
 */
public class JobSeekerSubAppModulePluginSubsystem extends AbstractPluginSubsystem {

    public JobSeekerSubAppModulePluginSubsystem() {
        super(new PluginReference(Plugins.JOB_SEEKER));
    }

    @Override
    public void start() throws CantStartSubsystemException {
        try {
            registerDeveloper(new DeveloperBitDubai());
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            throw new CantStartSubsystemException(e, null, null);
        }
    }
}
