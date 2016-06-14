package org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.core.PluginInfo;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.abstract_classes.AbstractActorNetworkService;

import org.fermat.fermat_job_plugin.layer.actor_network_service.job_seeker.developer.version_1.structure.JobSeekerActorNetworkServicePluginManager;

import java.util.List;

@PluginInfo(createdBy = "darkestpriest", maintainerMail = "darkpriestrelative@gmail.com", platform = Platforms.JOB_PLATFORM, layer = Layers.ACTOR_NETWORK_SERVICE, plugin = Plugins.JOB_SEEKER)
public class JobSeekerActorNetworkServicePluginRoot extends AbstractActorNetworkService implements DatabaseManagerForDevelopers{

    /**
     * Represents the plugin manager
     */
    JobSeekerActorNetworkServicePluginManager jobSeekerActorNetworkServicePluginManager;

    /**
     * Default constructor.
     */
    public JobSeekerActorNetworkServicePluginRoot(){

        super(
                new PluginVersionReference(new Version()),
                EventSource.JOB_SEEKER_ACTOR_NETWORK_SERVICE,
                NetworkServiceType.JOB_SEEKER
        );

    }

    @Override
    public FermatManager getManager() {
        return jobSeekerActorNetworkServicePluginManager;
    }

    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        //TODO: to implement
        return null;
    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase) {
        //TODO: to implement
        return null;
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        //TODO: to implement
        return null;
    }
}