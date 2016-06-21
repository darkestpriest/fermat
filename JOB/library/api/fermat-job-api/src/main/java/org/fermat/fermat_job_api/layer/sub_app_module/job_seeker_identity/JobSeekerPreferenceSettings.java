package org.fermat.fermat_job_api.layer.sub_app_module.job_seeker_identity;

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
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 21/06/16.
 */
public class JobSeekerPreferenceSettings implements SubAppSettings, Serializable {


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

    @Override
    public void setIsPresentationHelpEnabled(boolean b) {

    }
}
