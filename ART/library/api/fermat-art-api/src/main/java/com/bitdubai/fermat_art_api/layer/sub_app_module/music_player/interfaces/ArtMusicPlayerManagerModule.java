package com.bitdubai.fermat_art_api.layer.sub_app_module.music_player.interfaces;

import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.interfaces.ModuleManager;
import com.bitdubai.fermat_art_api.layer.sub_app_module.music_player.MusicPlayerPreferenceSettings;

/**
 * Created by Alexander Jimenez (alex_jimenez76@hotmail.com) on 4/4/16.
 */
public interface ArtMusicPlayerManagerModule extends ModuleManager<MusicPlayerPreferenceSettings,ActiveActorIdentityInformation> {
    //TODO: Implementar y Documentar

    ArtMusicPlayerManager getMusicPlayerManager();
}
