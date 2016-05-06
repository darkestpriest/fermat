package com.bitdubai.fermat_tky_plugin.layer.identity.fan_identity.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.DealsWithPluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.user.device_user.exceptions.CantGetLoggedInDeviceUserException;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUser;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUserManager;
import com.bitdubai.fermat_tky_api.all_definitions.enums.ExternalPlatform;
import com.bitdubai.fermat_tky_api.all_definitions.exceptions.ObjectNotSetException;
import com.bitdubai.fermat_tky_api.all_definitions.interfaces.User;
import com.bitdubai.fermat_tky_api.layer.external_api.interfaces.TokenlyApiManager;
import com.bitdubai.fermat_tky_api.layer.identity.fan.exceptions.CantCreateFanIdentityException;
import com.bitdubai.fermat_tky_api.layer.identity.fan.exceptions.CantGetFanIdentityException;
import com.bitdubai.fermat_tky_api.layer.identity.fan.exceptions.CantListFanIdentitiesException;
import com.bitdubai.fermat_tky_api.layer.identity.fan.exceptions.CantUpdateFanIdentityException;
import com.bitdubai.fermat_tky_api.layer.identity.fan.interfaces.Fan;
import com.bitdubai.fermat_tky_plugin.layer.identity.fan_identity.developer.bitdubai.version_1.database.TokenlyFanIdentityDao;
import com.bitdubai.fermat_tky_plugin.layer.identity.fan_identity.developer.bitdubai.version_1.exceptions.CantInitializeTokenlyFanIdentityDatabaseException;

import java.util.List;
import java.util.UUID;

/**
 * Created by Gabriel Araujo 10/03/16.
 */
public class TokenlyIdentityFanManagerImpl implements DealsWithErrors, DealsWithLogger, DealsWithPluginDatabaseSystem, DealsWithPluginFileSystem {
    /**
     * IdentityAssetIssuerManagerImpl member variables
     */
    UUID pluginId;

    /**
     * DealsWithErrors interface member variables
     */
    ErrorManager errorManager;

    /**
     * DealsWithLogger interface mmeber variables
     */
    LogManager logManager;

    /**
     * DealsWithPluginDatabaseSystem interface member variables
     */
    PluginDatabaseSystem pluginDatabaseSystem;

    /**
     * DealsWithPluginFileSystem interface member variables
     */
    PluginFileSystem pluginFileSystem;


    /**
     * DealsWithDeviceUsers Interface member variables.
     */
    private DeviceUserManager deviceUserManager;

    private TokenlyApiManager tokenlyApiManager;

    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    @Override
    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    @Override
    public void setPluginFileSystem(PluginFileSystem pluginFileSystem) {
        this.pluginFileSystem = pluginFileSystem;
    }

    /**
     * Constructor
     *
     * @param errorManager
     * @param logManager
     * @param pluginDatabaseSystem
     * @param pluginFileSystem
     */
    public TokenlyIdentityFanManagerImpl(ErrorManager errorManager, LogManager logManager, PluginDatabaseSystem pluginDatabaseSystem, PluginFileSystem pluginFileSystem, UUID pluginId, DeviceUserManager deviceUserManager, TokenlyApiManager tokenlyApiManager){
        this.errorManager = errorManager;
        this.logManager = logManager;
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginFileSystem = pluginFileSystem;
        this.pluginId = pluginId;
        this.deviceUserManager = deviceUserManager;
        this.tokenlyApiManager = tokenlyApiManager;
    }

    private TokenlyFanIdentityDao getFantIdentityDao() throws CantInitializeTokenlyFanIdentityDatabaseException, CantStartPluginException {
        return new TokenlyFanIdentityDao(this.pluginDatabaseSystem, this.pluginFileSystem, this.pluginId);
    }

    public List<Fan> getIdentityFanFromCurrentDeviceUser() throws CantListFanIdentitiesException {
        List<Fan> fans = null;

        try {
            DeviceUser loggedUser = deviceUserManager.getLoggedInDeviceUser();
            fans = getFantIdentityDao().getIdentityFansFromCurrentDeviceUser(loggedUser);
        } catch (CantGetLoggedInDeviceUserException e) {
            throw new CantListFanIdentitiesException(
                    "Error. CAN'T GET ASSET NEW ARTIST IDENTITIES. getIdentityFanFromCurrentDeviceUser - Message: " + e.getMessage(),
                    FermatException.wrapException(e),
                    e.getCause().toString(),
                    "getIdentityFanFromCurrentDeviceUser. Error get logged user device"
            );
        } catch (Exception ex){
            errorManager.reportUnexpectedPluginException(
                    Plugins.TOKENLY_FAN_SUB_APP_MODULE,
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    ex);
            throw new CantListFanIdentitiesException(
                    "Error. CAN'T GET ASSET NEW ARTIST IDENTITIES. getIdentityFanFromCurrentDeviceUser - Message: " + ex.getMessage(),
                    FermatException.wrapException(ex),
                    ex.getCause().toString(),
                    "getIdentityFanFromCurrentDeviceUser. unknown failure"
            );
        }finally {
            return fans;
        }
    }

    public Fan getIdentitFan() throws CantGetFanIdentityException {
        Fan fan = null;

        try {
            fan = getFantIdentityDao().getIdentityFan();
        } catch (CantInitializeTokenlyFanIdentityDatabaseException e) {
            throw new CantInitializeTokenlyFanIdentityDatabaseException(
                    "Error. CAN'T GET Fan Identity. getIdentitFan - Message: " + e.getMessage(),
                    FermatException.wrapException(e),
                    e.getCause().toString(),
                    "getIdentitFan. information doesn't exist."
            );
        } catch (Exception ex){
            errorManager.reportUnexpectedPluginException(
                    Plugins.TOKENLY_FAN_SUB_APP_MODULE,
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    ex);
            throw new CantListFanIdentitiesException(
                    "Error. CAN'T GET Fan Identity. getIdentitFan - Message: " + ex.getMessage(),
                    FermatException.wrapException(ex),
                    ex.getCause().toString(),
                    "getIdentitFan. unknown failure"
            );
        } finally {
            return fan;
        }

    }

    public Fan getIdentitFan(UUID id) throws CantGetFanIdentityException {
        Fan fan = null;
        try {
            fan = getFantIdentityDao().getIdentityFan(id);
        } catch (CantInitializeTokenlyFanIdentityDatabaseException e) {
            throw new CantInitializeTokenlyFanIdentityDatabaseException(
                    "Error. CAN'T GET FAN ID. getIdentitFan - Message: " + e.getMessage(),
                    FermatException.wrapException(e),
                    e.getCause().toString(),
                    "getIdentitFan. unknown failure"
            );
        } catch (Exception ex){
            errorManager.reportUnexpectedPluginException(
                    Plugins.TOKENLY_FAN_SUB_APP_MODULE,
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    ex);
            throw new CantListFanIdentitiesException(
                    "Error. CAN'T GET ASSET NEW ARTIST IDENTITIES. getIdentitFan - Message: " + ex.getMessage(),
                    FermatException.wrapException(ex),
                    ex.getCause().toString(),
                    "getIdentitFan. unknown failure"
            );
        }finally {
            return fan;
        }

    }

    public Fan createNewIdentityFan(User user,String password, byte[] profileImage, ExternalPlatform externalPlatform) throws CantCreateFanIdentityException {
        DeviceUser deviceUser = null;
        ECCKeyPair keyPair = null;
        UUID id = null;
        String publicKey = null, privateKey = null;

        try {
            deviceUser = deviceUserManager.getLoggedInDeviceUser();
            keyPair = new ECCKeyPair();
            id = UUID.randomUUID();
            publicKey = keyPair.getPublicKey();
            privateKey = keyPair.getPrivateKey();

            getFantIdentityDao().createNewUser(user, id, publicKey, privateKey, deviceUser, profileImage, password, externalPlatform);

        } catch (CantGetLoggedInDeviceUserException e) {
            throw new CantListFanIdentitiesException(
                    "Error. CAN'T CREATE NEW ARTIST IDENTITY. createNewIdentityFan - Message: " + e.getMessage(),
                    FermatException.wrapException(e),
                    e.getCause().toString(),
                    "createNewIdentityFan. Error getting current logged in device user"
            );
        } catch (Exception ex){
            errorManager.reportUnexpectedPluginException(
                    Plugins.TOKENLY_FAN_SUB_APP_MODULE,
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    ex);
            throw new CantListFanIdentitiesException(
                    "Error. CAN'T CREATE NEW ARTIST IDENTITY. createNewIdentityFan - Message: " + ex.getMessage(),
                    FermatException.wrapException(ex),
                    ex.getCause().toString(),
                    "createNewIdentityFan. unknown failure"
            );
        }finally {
            return new TokenlyFanIdentityImp(user,id,publicKey,profileImage,externalPlatform,pluginFileSystem, pluginId);
        }
    }

    public void updateIdentityFan(User user,String password, UUID id, String publicKey, byte[] profileImage, ExternalPlatform externalPlatform) throws CantUpdateFanIdentityException {
        try {
            getFantIdentityDao().updateIdentityFanUser(user, password, id, publicKey, profileImage, externalPlatform);

        } catch (CantInitializeTokenlyFanIdentityDatabaseException e) {
            throw new CantUpdateFanIdentityException(
                    "Error. Can't update the fan identity. updateIdentityFan - Message: " + e.getMessage(),
                    FermatException.wrapException(e),
                    e.getCause().toString(),
                    "updateIdentityFan. Cant Initialize Tokenly Fan Identity Database"
            );
        } catch (CantUpdateFanIdentityException e) {
            throw new CantUpdateFanIdentityException(
                    "Error. Can't update the fan identity. updateIdentityFan - Message: " + e.getMessage(),
                    FermatException.wrapException(e),
                    e.getCause().toString(),
                    "updateIdentityFan. Can't Update Fan Identity. Unexpected error in database"
            );
        } catch (CantStartPluginException e) {
            throw new CantUpdateFanIdentityException(
                    "Error. Can't update the fan identity. updateIdentityFan - Message: " + e.getMessage(),
                    FermatException.wrapException(e),
                    e.getCause().toString(),
                    "updateIdentityFan. Can't Start Plugin. Unknown Failure"
            );
        } catch (Exception ex){
            errorManager.reportUnexpectedPluginException(
                    Plugins.TOKENLY_FAN_SUB_APP_MODULE,
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    ex);
            throw new CantUpdateFanIdentityException(
                    "Error. Can't update the fan identity. updateIdentityFan - Message: " + ex.getMessage(),
                    FermatException.wrapException(ex),
                    ex.getCause().toString(),
                    "updateIdentityFan. unknown failure"
            );
        }
    }

    /**
     * This method updates a Fan Identity.
     * @param fan
     * @throws CantUpdateFanIdentityException
     */
    public void updateIdentityFan(Fan fan) throws CantUpdateFanIdentityException{
        try{
            getFantIdentityDao().updateIdentityFanUser(fan);
        } catch (CantInitializeTokenlyFanIdentityDatabaseException e) {
            throw new CantUpdateFanIdentityException(
                    "Error. Can't update the fan identity. updateIdentityFan - Message: " + e.getMessage(),
                    FermatException.wrapException(e),
                    e.getCause().toString(),
                    "updateIdentityFan. Can't Initialize Tokenly Fan Identity Database"
            );
        } catch (CantUpdateFanIdentityException e) {
            throw new CantUpdateFanIdentityException(
                    "Error. Can't update the fan identity. updateIdentityFan - Message: " + e.getMessage(),
                    FermatException.wrapException(e),
                    e.getCause().toString(),
                    "updateIdentityFan. Can't Update Fan Identity Exception"
            );
        } catch (CantStartPluginException e) {
            throw new CantUpdateFanIdentityException(
                    "Error. Can't update the fan identity. updateIdentityFan - Message: " + e.getMessage(),
                    FermatException.wrapException(e),
                    e.getCause().toString(),
                    "updateIdentityFan. Can't Start Plugin"
            );
        } catch (Exception ex){
            errorManager.reportUnexpectedPluginException(
                    Plugins.TOKENLY_FAN_SUB_APP_MODULE,
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    ex);
            throw new CantUpdateFanIdentityException(
                    "Error. Can't update the fan identity. updateIdentityFan - Message: " + ex.getMessage(),
                    FermatException.wrapException(ex),
                    ex.getCause().toString(),
                    "updateIdentityFan. Unexpected error in database"
            );
        }
    }

}
