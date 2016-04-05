package com.bitdubai.fermat_art_api.layer.sub_app_module.identity.artist.interfaces;

import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.interfaces.ModuleManager;
import com.bitdubai.fermat_art_api.all_definition.exceptions.ARTException;
import com.bitdubai.fermat_art_api.layer.sub_app_module.identity.artist.ArtArtistIdentityPreferenceSettings;

/**
 * Created by Alexander Jimenez (alex_jimenez76@hotmail.com) on 3/23/16.
 */
public interface ArtArtistIdentityManagerModule extends ModuleManager<ArtArtistIdentityPreferenceSettings,ActiveActorIdentityInformation> {
    //TODO: Implementar y Documentar

    ArtArtistIdentityManager getArtArtistIdentityManager() throws ARTException;
}
