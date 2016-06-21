package org.fermat.fermat_job_api.layer.sub_app_module.common;

import com.bitdubai.fermat_api.layer.modules.ModuleSettingsImpl;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.interfaces.ModuleManager;
import com.bitdubai.fermat_pip_api.all_definition.sub_app_module.settings.interfaces.SubAppSettings;

import org.fermat.fermat_job_api.all_definition.exceptions.CantExposeIdentityException;
import org.fermat.fermat_job_api.all_definition.interfaces.JobIdentity;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantHideIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantListJobsPlatformIdentitiesException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantPublishIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.IdentityNotFoundException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 21/06/16.
 */
public interface JobIdentitySubAppModuleManager<T extends SubAppSettings, K extends JobIdentity>
        extends ModuleManager<T, ActiveActorIdentityInformation>,
        ModuleSettingsImpl<T>,
        Serializable {

        /**
         * This method contains the logic to publish an identity.
         * @param publicKey
         * @throws CantPublishIdentityException
         * @throws IdentityNotFoundException
         */
        void publishIdentity(String publicKey)
                throws CantPublishIdentityException,
                IdentityNotFoundException, CantExposeIdentityException;

        /**
         * This method contains all the logic to hide an identity
         * @param publicKey
         * @throws CantHideIdentityException
         * @throws IdentityNotFoundException
         */
        void hideIdentity(String publicKey)
                throws CantHideIdentityException,
                IdentityNotFoundException;

        /**
         * This method returns an identities list from current device user
         * @return
         * @throws CantListJobsPlatformIdentitiesException
         */
        List<K> getIdentitiesFromCurrentDeviceUser()
                throws CantListJobsPlatformIdentitiesException;

        /**
         * This method returns true if exists any identity in the device
         * @return
         * @throws CantListJobsPlatformIdentitiesException
         */
        boolean hasIdentity() throws CantListJobsPlatformIdentitiesException;
}
