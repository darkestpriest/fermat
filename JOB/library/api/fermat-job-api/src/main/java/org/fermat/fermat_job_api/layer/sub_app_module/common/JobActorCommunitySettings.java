package org.fermat.fermat_job_api.layer.sub_app_module.common;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_pip_api.all_definition.sub_app_module.settings.exceptions.CantGetDefaultLanguageException;
import com.bitdubai.fermat_pip_api.all_definition.sub_app_module.settings.exceptions.CantGetDefaultSkinException;
import com.bitdubai.fermat_pip_api.all_definition.sub_app_module.settings.exceptions.CantLoadSubAppSettings;
import com.bitdubai.fermat_pip_api.all_definition.sub_app_module.settings.exceptions.CantSaveSubAppSettings;
import com.bitdubai.fermat_pip_api.all_definition.sub_app_module.settings.exceptions.CantSetDefaultLanguageException;
import com.bitdubai.fermat_pip_api.all_definition.sub_app_module.settings.exceptions.CantSetDefaultSkinException;
import com.bitdubai.fermat_pip_api.all_definition.sub_app_module.settings.interfaces.SubAppSettings;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 22/06/16.
 */
public class JobActorCommunitySettings implements SubAppSettings, Serializable {

    private String lastSelectedIdentityPublicKey;
    private Actors lastSelectedActorType;
    private boolean presentationHelpEnabled;

    /**
     * This method returns the last selected identity public key.
     * @return
     */
    public String getLastSelectedIdentityPublicKey() {
        return this.lastSelectedIdentityPublicKey;
    }

    /**
     * This method sets the last selected identity public key.
     * @param identityPublicKey
     */
    public void setLastSelectedIdentityPublicKey(String identityPublicKey) {
        this.lastSelectedIdentityPublicKey = identityPublicKey;
    }

    /**
     * This method the last selected actor type
     * @return
     */
    public Actors getLastSelectedActorType() {
        return this.lastSelectedActorType;
    }

    /**
     * This method sets the last selected actor type
     * @param actorType
     */
    public void setLastSelectedActorType(Actors actorType) {
        this.lastSelectedActorType = actorType;
    }

    /**
     * This method sets the presentation help enabled
     * @param b
     */
    @Override
    public void setIsPresentationHelpEnabled(boolean b) {
        this.presentationHelpEnabled = b;
    }

    /**
     * This method returns if the presentation help is enabled.
     * @return
     */
    public boolean isPresentationHelpEnabled() {
        return this.presentationHelpEnabled;
    }

    /**
     * The following method are not used in this version, we implemented only for avoid compilation
     * issues.
     */

    @Override
    public UUID getDefaultLanguage() throws CantGetDefaultLanguageException {
        return null;
    }

    @Override
    public UUID getDefaultSkin() throws CantGetDefaultSkinException {
        return null;
    }

    @Override
    public void setDefaultLanguage(UUID languageId) throws CantSetDefaultLanguageException {

    }

    @Override
    public void setDefaultSkin(UUID skinId) throws CantSetDefaultSkinException {

    }

    @Override
    public void setPreferenceSettings(String walletPreferenceSettings, String walletPublicKey)
            throws CantSaveSubAppSettings {

    }

    @Override
    public String getPreferenceSettings(String walletPublicKey) throws CantLoadSubAppSettings {
        return null;
    }
}
