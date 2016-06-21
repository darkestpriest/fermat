package org.fermat.fermat_job_plugin.layer.sub_app_module.job_seeker_identity.developer.version_1;

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
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUserManager;

import org.fermat.fermat_job_api.layer.identity.job_seeker.interfaces.JobSeekerIdentityManager;
import org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_identity.JobSeekerPreferenceSettings;
import org.fermat.fermat_job_plugin.layer.sub_app_module.job_seeker_identity.developer.version_1.structure.JobSeekerIdentityModulePluginManager;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 21/06/16.
 */
@PluginInfo(createdBy = "darkestpriest", maintainerMail = "darkpriestrelative@gmail.com", platform = Platforms.JOB_PLATFORM, layer = Layers.SUB_APP_MODULE, plugin = Plugins.JOB_SEEKER)
public class JobSeekerIdentitySubAppModulePluginRoot
        extends AbstractModule<JobSeekerPreferenceSettings,ActiveActorIdentityInformation> {

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.USER , addon = Addons.DEVICE_USER)
    private DeviceUserManager deviceUserManager;

    @NeededPluginReference(platform = Platforms.JOB_PLATFORM, layer = Layers.IDENTITY, plugin = Plugins.JOB_SEEKER)
    private JobSeekerIdentityManager identityManager;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_FILE_SYSTEM)
    private PluginFileSystem pluginFileSystem;

    private JobSeekerIdentityModulePluginManager jobSeekerIdentityModulePluginManager;

    public JobSeekerIdentitySubAppModulePluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    /**
     * This method returns the plugin manager.
     * @return
     * @throws CantGetModuleManagerException
     */
    @Override
    public JobSeekerIdentityModulePluginManager getModuleManager()
            throws CantGetModuleManagerException {
        if (jobSeekerIdentityModulePluginManager == null){
            jobSeekerIdentityModulePluginManager = new JobSeekerIdentityModulePluginManager(
                    pluginFileSystem,
                    pluginId,
                    identityManager,
                    deviceUserManager,
                    this);

            this.serviceStatus = ServiceStatus.STARTED;
        }
        return jobSeekerIdentityModulePluginManager;
    }

}
