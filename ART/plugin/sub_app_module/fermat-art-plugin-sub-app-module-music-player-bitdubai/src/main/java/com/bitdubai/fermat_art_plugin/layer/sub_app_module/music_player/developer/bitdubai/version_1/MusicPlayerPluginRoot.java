package com.bitdubai.fermat_art_plugin.layer.sub_app_module.music_player.developer.bitdubai.version_1;

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
import com.bitdubai.fermat_art_api.layer.sub_app_module.music_player.MusicPlayerPreferenceSettings;
import com.bitdubai.fermat_art_api.layer.sub_app_module.music_player.interfaces.ArtMusicPlayerManager;
import com.bitdubai.fermat_art_api.layer.sub_app_module.music_player.interfaces.ArtMusicPlayerManagerModule;
import com.bitdubai.fermat_art_plugin.layer.sub_app_module.music_player.developer.bitdubai.version_1.structure.MusicPlayerManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_tky_api.layer.song_wallet.interfaces.SongWalletTokenlyManager;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 29/03/16.
 */
public class MusicPlayerPluginRoot extends AbstractPlugin implements ArtMusicPlayerManagerModule{

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.ERROR_MANAGER)
    private ErrorManager errorManager;

    @NeededPluginReference(platform = Platforms.ART_PLATFORM, layer = Layers.IDENTITY,plugin = Plugins.MUSIC_PLAYER_SUB_APP_MODULE)
    private SongWalletTokenlyManager songWalletTokenlyManager;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_FILE_SYSTEM)
    PluginFileSystem pluginFileSystem;

    private MusicPlayerManager musicPlayerManager;
    private ArtMusicPlayerManager artMusicPlayerManager;
    private SettingsManager<MusicPlayerPreferenceSettings> settingsManager;
    /**
     * Default constructor
     */
    public MusicPlayerPluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    private void initPluginManager(){
        this.musicPlayerManager = new MusicPlayerManager(errorManager,songWalletTokenlyManager);
    }

    @Override
    public void start() throws CantStartPluginException {
        try{
            initPluginManager();
            System.out.println("ART MUSIC PLAYER STARTED2");
        } catch (Exception e) {
            this.errorManager.reportUnexpectedPluginException(
                    Plugins.MUSIC_PLAYER_SUB_APP_MODULE,
                    UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN,
                    e);
            throw new CantStartPluginException(
                    CantStartPluginException.DEFAULT_MESSAGE,
                    e, "Cant start Sub App Music Player Module plugin.",
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
    public ArtMusicPlayerManager getMusicPlayerManager() {
        if(artMusicPlayerManager==null){
            artMusicPlayerManager=new MusicPlayerManager(
                    errorManager,
                    songWalletTokenlyManager
                    );
        }
        return artMusicPlayerManager;    }

    @Override
    public SettingsManager<MusicPlayerPreferenceSettings> getSettingsManager() {
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
