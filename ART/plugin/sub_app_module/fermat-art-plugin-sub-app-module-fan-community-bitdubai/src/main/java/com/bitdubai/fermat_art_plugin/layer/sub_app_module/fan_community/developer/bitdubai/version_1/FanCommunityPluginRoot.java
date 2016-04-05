package com.bitdubai.fermat_art_plugin.layer.sub_app_module.fan_community.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededPluginReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.exceptions.ActorIdentityNotSelectedException;
import com.bitdubai.fermat_api.layer.modules.exceptions.CantGetSelectedActorIdentityException;
import com.bitdubai.fermat_api.layer.modules.interfaces.ModuleManager;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_art_api.layer.actor_connection.fan.interfaces.FanActorConnectionManager;
import com.bitdubai.fermat_art_api.layer.sub_app_module.community.fan.FanCommunityPreferenceSettings;
import com.bitdubai.fermat_art_api.layer.sub_app_module.community.fan.interfaces.ArtFanCommunityManager;
import com.bitdubai.fermat_art_api.layer.sub_app_module.community.fan.interfaces.ArtFanCommunityManagerModule;
import com.bitdubai.fermat_art_plugin.layer.sub_app_module.fan_community.developer.bitdubai.version_1.structure.FanCommunityManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 08/03/16.
 */
public class FanCommunityPluginRoot extends AbstractPlugin implements ArtFanCommunityManagerModule{
    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.ERROR_MANAGER)
    private ErrorManager errorManager;

    @NeededPluginReference(platform = Platforms.ART_PLATFORM, layer = Layers.IDENTITY,plugin = Plugins.ARTIST_IDENTITY)
    private FanActorConnectionManager fanActorConnectionManager;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_FILE_SYSTEM)
    PluginFileSystem pluginFileSystem;

    private SettingsManager<FanCommunityPreferenceSettings> settingsManager;
    private ArtFanCommunityManager artFanCommunityManager;
    private FanCommunityManager fanCommunityManager;
    /**
     * Default constructor
     */
    public FanCommunityPluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    private void initPluginManager(){
        this.fanCommunityManager = new FanCommunityManager(
                errorManager
                ,fanActorConnectionManager
        );
    }

    @Override
    public void start() throws CantStartPluginException {
        try{
            initPluginManager();
            System.out.println("########### FanCommunityPluginRoot");
        } catch (Exception e) {
            this.errorManager.reportUnexpectedPluginException(
                    Plugins.FAN_COMMUNITY_SUB_APP_MODULE,
                    UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN,
                    e);
            throw new CantStartPluginException(
                    CantStartPluginException.DEFAULT_MESSAGE,
                    e, "Cant start Sub App Fan Community Sub App Module plugin.",
                    null);
        }
        /**
         * nothing left to do.
         */
        this.serviceStatus = ServiceStatus.STARTED;
    }
    public void stop(){
        this.serviceStatus = ServiceStatus.STOPPED;
    }

    /**
     * This method is used by the fermat-core to get the plugin manager in execution time.
     * @return
     */
    @Override
    public ArtFanCommunityManager getFanCommunityManager() {
        if(artFanCommunityManager==null){
            artFanCommunityManager=new FanCommunityManager(
                    errorManager,
                    fanActorConnectionManager);
        }
        return artFanCommunityManager;    }

    @Override
    public SettingsManager<FanCommunityPreferenceSettings> getSettingsManager() {
        if (this.settingsManager != null)
            return this.settingsManager;

        this.settingsManager = new SettingsManager<>(
                pluginFileSystem,
                pluginId
        );

        return this.settingsManager;
    }

    @Override
    public ActiveActorIdentityInformation getSelectedActorIdentity() throws CantGetSelectedActorIdentityException, ActorIdentityNotSelectedException {
        return null;
    }

    @Override
    public void createIdentity(String name, String phrase, byte[] profile_img) throws Exception {

    }

    @Override
    public void setAppPublicKey(String publicKey) {

    }

    @Override
    public int[] getMenuNotifications() {
        return new int[0];
    }
}
