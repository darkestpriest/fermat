package org.fermat.fermat_job_core.layer.actor_network_service.job_seeker;

import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractPluginSubsystem;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartSubsystemException;

import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.DeveloperBitDubai;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 18/06/16.
 */
public class JobSeekerPluginSubsystem extends AbstractPluginSubsystem {

    public JobSeekerPluginSubsystem() {
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
