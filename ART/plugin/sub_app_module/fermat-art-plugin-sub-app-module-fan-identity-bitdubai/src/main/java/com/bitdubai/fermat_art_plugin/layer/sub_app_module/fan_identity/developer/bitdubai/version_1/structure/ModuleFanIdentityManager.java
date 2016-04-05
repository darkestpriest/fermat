package com.bitdubai.fermat_art_plugin.layer.sub_app_module.fan_identity.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.exceptions.ActorIdentityNotSelectedException;
import com.bitdubai.fermat_api.layer.modules.exceptions.CantGetSelectedActorIdentityException;
import com.bitdubai.fermat_art_api.all_definition.exceptions.CantPublishIdentityException;
import com.bitdubai.fermat_art_api.all_definition.exceptions.IdentityNotFoundException;
import com.bitdubai.fermat_art_api.layer.identity.fan.exceptions.CantCreateFanIdentityException;
import com.bitdubai.fermat_art_api.layer.identity.fan.exceptions.CantGetFanIdentityException;
import com.bitdubai.fermat_art_api.layer.identity.fan.exceptions.CantListFanIdentitiesException;
import com.bitdubai.fermat_art_api.layer.identity.fan.exceptions.CantUpdateFanIdentityException;
import com.bitdubai.fermat_art_api.layer.identity.fan.exceptions.FanIdentityAlreadyExistsException;
import com.bitdubai.fermat_art_api.layer.identity.fan.interfaces.Fan;
import com.bitdubai.fermat_art_api.layer.sub_app_module.identity.fan.interfaces.ArtFanIdentityManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import java.util.List;

/**
 * Created by alexander on 3/15/16.
 */
public class ModuleFanIdentityManager implements ArtFanIdentityManager {
    private final ErrorManager errorManager;
    private final com.bitdubai.fermat_art_api.layer.identity.fan.interfaces.FanIdentityManager fanIdentityManager;

    public ModuleFanIdentityManager(ErrorManager errorManager, com.bitdubai.fermat_art_api.layer.identity.fan.interfaces.FanIdentityManager fanIdentityManager) {
        this.errorManager = errorManager;
        this.fanIdentityManager = fanIdentityManager;
    }

    @Override
    public List<Fan> listIdentitiesFromCurrentDeviceUser() throws CantListFanIdentitiesException {
        return fanIdentityManager.listIdentitiesFromCurrentDeviceUser();
    }

    @Override
    public Fan createArtistIdentity(String alias, byte[] imageBytes) throws CantCreateFanIdentityException, FanIdentityAlreadyExistsException {
        return fanIdentityManager.createArtistIdentity(alias,imageBytes);
    }

    @Override
    public void updateFanIdentity(String alias, String publicKey, byte[] imageProfile, String external) throws CantUpdateFanIdentityException {
        fanIdentityManager.updateFanIdentity(alias,publicKey,imageProfile,external);
    }

    @Override
    public Fan getFanIdentity(String publicKey) throws CantGetFanIdentityException, IdentityNotFoundException {
        return fanIdentityManager.getFanIdentity(publicKey);
    }

    @Override
    public void publishIdentity(String publicKey) throws CantPublishIdentityException, IdentityNotFoundException {
        fanIdentityManager.publishIdentity(publicKey);
    }
}
