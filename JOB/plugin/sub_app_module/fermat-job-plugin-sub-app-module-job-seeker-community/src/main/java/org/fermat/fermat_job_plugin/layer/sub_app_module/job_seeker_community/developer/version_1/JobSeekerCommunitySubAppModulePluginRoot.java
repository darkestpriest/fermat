package org.fermat.fermat_job_plugin.layer.sub_app_module.job_seeker_community.developer.version_1;

import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractModule;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededPluginReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantGetModuleManagerException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.core.PluginInfo;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.interfaces.ModuleManager;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUserManager;

import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces.JobSeekerManager;
import org.fermat.fermat_job_api.layer.identity.employer.interfaces.EmployerIdentityManager;
import org.fermat.fermat_job_api.layer.identity.job_seeker.interfaces.JobSeekerIdentityManager;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.interfaces.JobSeekerCommunitySubAppModuleManager;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_community.utils.JobSeekerCommunitySettings;
import org.fermat.fermat_job_plugin.layer.sub_app_module.job_seeker_community.developer.version_1.structure.JobSeekerCommunityModulePluginManager;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 23/06/16.
 */
@PluginInfo(createdBy = "darkestpriest", maintainerMail = "darkpriestrelative@gmail.com", platform = Platforms.JOB_PLATFORM, layer = Layers.SUB_APP_MODULE, plugin = Plugins.JOB_SEEKER_COMMUNITY)
public class JobSeekerCommunitySubAppModulePluginRoot
        extends AbstractModule<JobSeekerCommunitySettings,ActiveActorIdentityInformation> {

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.USER , addon = Addons.DEVICE_USER)
    private DeviceUserManager deviceUserManager;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_FILE_SYSTEM)
    private PluginFileSystem pluginFileSystem;

    @NeededPluginReference(platform = Platforms.JOB_PLATFORM, layer = Layers.ACTOR_NETWORK_SERVICE, plugin = Plugins.JOB_SEEKER)
    private JobSeekerManager identityManager;

    @NeededPluginReference(platform = Platforms.JOB_PLATFORM, layer = Layers.IDENTITY, plugin = Plugins.JOB_SEEKER)
    private JobSeekerIdentityManager jobSeekerIdentityManager;

    @NeededPluginReference(platform = Platforms.JOB_PLATFORM, layer = Layers.IDENTITY, plugin = Plugins.EMPLOYER)
    private EmployerIdentityManager employerIdentityManager;

    /**
     * Represents the plugin manager
     */
    private JobSeekerCommunitySubAppModuleManager jobSeekerCommunitySubAppModuleManager;

    public JobSeekerCommunitySubAppModulePluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    /**
     * This method returns the plugin manager.
     * @return
     * @throws CantGetModuleManagerException
     */
    @Override
    public ModuleManager getModuleManager()
            throws CantGetModuleManagerException {
        if(jobSeekerCommunitySubAppModuleManager==null){
            jobSeekerCommunitySubAppModuleManager = new JobSeekerCommunityModulePluginManager(
                    pluginFileSystem,
                    pluginId,
                    identityManager,
                    this,
                    employerIdentityManager,
                    jobSeekerIdentityManager);
            this.serviceStatus = ServiceStatus.STARTED;
        }
        return jobSeekerCommunitySubAppModuleManager;
    }
}
