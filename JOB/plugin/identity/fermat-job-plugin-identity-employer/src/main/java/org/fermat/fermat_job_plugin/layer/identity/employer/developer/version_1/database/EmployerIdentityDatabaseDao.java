package org.fermat.fermat_job_plugin.layer.identity.employer.developer.version_1.database;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.AsymmetricCryptography;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.interfaces.KeyPair;
import com.bitdubai.fermat_api.layer.all_definition.enums.DeviceDirectory;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginBinaryFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginTextFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantLoadFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantPersistFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUser;
import com.bitdubai.fermat_wpd_api.layer.wpd_identity.publisher.exceptions.CantCreateNewDeveloperException;

import org.fermat.fermat_job_api.all_definition.enums.ExposureLevel;
import org.fermat.fermat_job_api.all_definition.enums.Frequency;
import org.fermat.fermat_job_api.all_definition.enums.Industry;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantChangeExposureLevelException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantCreateJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantGetIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantGetJobActorIdentityPrivateKeyException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantGetJobActorIdentityProfileImageException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantListJobsPlatformIdentitiesException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantPersistProfileImageException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantUpdateJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.IdentityNotFoundException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantPersistPrivateKeyException;
import org.fermat.fermat_job_api.layer.identity.employer.interfaces.Employer;
import org.fermat.fermat_job_api.layer.identity.employer.utils.EmployerRecord;
import org.fermat.fermat_job_plugin.layer.identity.employer.developer.version_1.EmployerIdentityPluginRoot;
import org.fermat.fermat_job_plugin.layer.identity.employer.developer.version_1.exceptions.CantInitializeEmployerIdentityDatabaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 20/06/16.
 */
public class EmployerIdentityDatabaseDao implements DealsWithPluginDatabaseSystem {

    private PluginDatabaseSystem pluginDatabaseSystem;
    private PluginFileSystem pluginFileSystem;
    private UUID pluginId;
    private Database database;

    public EmployerIdentityDatabaseDao(
            PluginDatabaseSystem pluginDatabaseSystem,
            PluginFileSystem pluginFileSystem,
            UUID pluginId) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginFileSystem = pluginFileSystem;
        this.pluginId = pluginId;
    }

    public void initialize() throws CantInitializeEmployerIdentityDatabaseException {
        try {
            database = this.pluginDatabaseSystem.openDatabase(this.pluginId, this.pluginId.toString());
        } catch (DatabaseNotFoundException e) {
            try {
                EmployerIdentityDatabaseFactory databaseFactory =
                        new EmployerIdentityDatabaseFactory(pluginDatabaseSystem);
                database = databaseFactory.createDatabase(pluginId, pluginId.toString());
            } catch (CantCreateDatabaseException f) {
                throw new CantInitializeEmployerIdentityDatabaseException(
                        CantCreateDatabaseException.DEFAULT_MESSAGE,
                        f,
                        "",
                        "There is a problem and i cannot create the database.");
            } catch (Exception z) {
                throw new CantInitializeEmployerIdentityDatabaseException(
                        CantOpenDatabaseException.DEFAULT_MESSAGE,
                        z,
                        "",
                        "Generic Exception.");
            }
        } catch (CantOpenDatabaseException e) {
            throw new CantInitializeEmployerIdentityDatabaseException(
                    CantOpenDatabaseException.DEFAULT_MESSAGE,
                    e,
                    "",
                    "Exception not handled by the plugin, there is a problem and i cannot open the database.");
        } catch (Exception e) {
            throw new CantInitializeEmployerIdentityDatabaseException(
                    CantOpenDatabaseException.DEFAULT_MESSAGE,
                    e,
                    "",
                    "Generic Exception.");
        }
    }

    /**
     * This method contains all the logic to create a new identity
     * @param deviceUser
     * @param accuracy
     * @param frequency
     * @throws CantCreateNewDeveloperException
     */
    public Employer createNewEmployerIdentity(
            final String alias,
            final DeviceUser deviceUser,
            byte[] imageProfile,
            ExposureLevel exposureLevel,
            long accuracy,
            Frequency frequency,
            Industry industry) throws CantCreateJobPlatformIdentityException {
        try {
            if (aliasExists (alias)) {
                throw new CantCreateNewDeveloperException (
                        "Cant create new Employer Identity, alias exists.",
                        "Employer Identity",
                        "Cant create new Employer Identity, alias exists.");
            }
            KeyPair eccKeyPair = new ECCKeyPair();
            String publicKey = eccKeyPair.getPublicKey();
            persistNewEmployerIdentityPrivateKeysFile(
                    publicKey,
                    eccKeyPair.getPrivateKey());
            DatabaseTable table = this.database.getTable(
                    EmployerIdentityDatabaseConstants.EMPLOYER_TABLE_NAME);
            DatabaseTableRecord record = table.getEmptyRecord();
            record.setStringValue(
                    EmployerIdentityDatabaseConstants.EMPLOYER_PUBLIC_KEY_COLUMN_NAME,
                    publicKey);
            record.setStringValue(
                    EmployerIdentityDatabaseConstants.EMPLOYER_ALIAS_COLUMN_NAME,
                    alias);
            record.setStringValue(
                    EmployerIdentityDatabaseConstants.EMPLOYER_DEVICE_USER_PUBLIC_KEY_COLUMN_NAME,
                    deviceUser.getPublicKey());
            record.setFermatEnum(
                    EmployerIdentityDatabaseConstants.EMPLOYER_EXPOSURE_LEVEL_COLUMN_NAME,
                    exposureLevel);
            record.setLongValue(
                    EmployerIdentityDatabaseConstants.EMPLOYER_ACCURACY_COLUMN_NAME,
                    accuracy);
            record.setFermatEnum(
                    EmployerIdentityDatabaseConstants.EMPLOYER_FREQUENCY_COLUMN_NAME,
                    frequency);
            record.setFermatEnum(EmployerIdentityDatabaseConstants.EMPLOYER_INDUSTRY_COLUMN_NAME,
                    industry);
            table.insertRecord(record);
            persistNewEmployerIdentityProfileImage(
                    publicKey,
                    imageProfile);
            return new EmployerRecord(
                    alias,
                    eccKeyPair,
                    exposureLevel,
                    accuracy,
                    frequency,
                    industry,
                    imageProfile);
        } catch (CantInsertRecordException e){
            throw new CantCreateJobPlatformIdentityException (
                    e.getMessage(),
                    e,
                    "Employer Identity",
                    "Cannot create new Employer Identity, insert database problems.");
        } catch (CantPersistPrivateKeyException e){
            throw new CantCreateJobPlatformIdentityException (
                    e.getMessage(), 
                    e, 
                    "Employer Identity", 
                    "Cant create new Employer Identity,persist private key error.");
        } catch (Exception e) {
            throw new CantCreateJobPlatformIdentityException (
                    e.getMessage(), 
                    FermatException.wrapException(e),
                    "Employer Identity", 
                    "Cant create new Employer Identity, unknown failure.");
        }
    }

    public Employer updateCryptoBrokerIdentity(
            String alias, 
            String publicKey,
            byte[] imageProfile,
            long accuracy,
            Frequency frequency,
            ExposureLevel exposureLevel,
            Industry industry) throws CantUpdateJobPlatformIdentityException {
        try {
            DatabaseTable table = this.database.getTable(
                    EmployerIdentityDatabaseConstants.EMPLOYER_TABLE_NAME);
            DatabaseTableRecord record = table.getEmptyRecord();
            table.addStringFilter(
                    EmployerIdentityDatabaseConstants.EMPLOYER_PUBLIC_KEY_COLUMN_NAME,
                    publicKey,
                    DatabaseFilterType.EQUAL);
            record.setStringValue(
                    EmployerIdentityDatabaseConstants.EMPLOYER_ALIAS_COLUMN_NAME,
                    alias);
            record.setLongValue(
                    EmployerIdentityDatabaseConstants.EMPLOYER_ACCURACY_COLUMN_NAME,
                    accuracy);
            record.setFermatEnum(
                    EmployerIdentityDatabaseConstants.EMPLOYER_FREQUENCY_COLUMN_NAME,
                    frequency);
            record.setFermatEnum(
                    EmployerIdentityDatabaseConstants.EMPLOYER_EXPOSURE_LEVEL_COLUMN_NAME,
                    exposureLevel);
            record.setFermatEnum(EmployerIdentityDatabaseConstants.EMPLOYER_INDUSTRY_COLUMN_NAME,
                    industry);
            table.updateRecord(record);
            updateEmployerIdentityProfileImage(publicKey, imageProfile);
            String privateKey = getEmployerIdentityPrivateKey(publicKey);
            ECCKeyPair eccKeyPair = new ECCKeyPair(privateKey, publicKey);
            return new EmployerRecord(
                    alias,
                    eccKeyPair,
                    exposureLevel,
                    accuracy,
                    frequency,
                    industry,
                    imageProfile);
        } catch (CantUpdateRecordException e) {
            throw new CantUpdateJobPlatformIdentityException(
                    e.getMessage(), 
                    e, 
                    "Updating Employer Identity",
                    "Cannot update the record in database");
        } catch (CantPersistProfileImageException e) {
            throw new CantUpdateJobPlatformIdentityException(
                    e.getMessage(), 
                    e, 
                    "Updating Employer Identity",
                    "Cannot persist the image");
        } catch (CantGetJobActorIdentityPrivateKeyException e) {
            throw new CantUpdateJobPlatformIdentityException(
                    e.getMessage(),
                    e,
                    "Updating Employer Identity",
                    "Cannot get the private key from file");
        }
    }

    /**
     * This method returns the identities created in the device.
     * @param deviceUser
     * @return
     * @throws CantListJobsPlatformIdentitiesException
     */
    public final List<Employer> listIdentitiesFromDeviceUser(
            final DeviceUser deviceUser) 
            throws CantListJobsPlatformIdentitiesException {
        try {
            final DatabaseTable table = this.database.getTable (
                    EmployerIdentityDatabaseConstants.EMPLOYER_TABLE_NAME);
            table.addStringFilter(
                    EmployerIdentityDatabaseConstants.EMPLOYER_DEVICE_USER_PUBLIC_KEY_COLUMN_NAME,
                    deviceUser.getPublicKey(),
                    DatabaseFilterType.EQUAL);
            table.loadToMemory();
            final List<Employer> list = new ArrayList<>();
            for (DatabaseTableRecord record : table.getRecords ())
                list.add(getIdentityFromRecord(record));
            return list;
        } catch (final CantGetJobActorIdentityProfileImageException e) {
            throw new CantListJobsPlatformIdentitiesException(
                    e.getMessage(),
                    e,
                    "Listing Employer Identities",
                    "Problem trying to get the profile image of the identity.");
        } catch (final CantLoadTableToMemoryException e) {
            throw new CantListJobsPlatformIdentitiesException(
                    e.getMessage(),
                    e,
                    "Listing Employer Identities",
                    "Cant load " + EmployerIdentityDatabaseConstants.EMPLOYER_TABLE_NAME + " table in memory.");
        } catch (final CantGetJobActorIdentityPrivateKeyException e) {
            throw new CantListJobsPlatformIdentitiesException(
                    e.getMessage(),
                    e,
                    "Listing Employer Identities",
                    "Can't get private key.");
        } catch (final InvalidParameterException e) {
            throw new CantListJobsPlatformIdentitiesException(
                    e.getMessage(),
                    FermatException.wrapException(e),
                    "Listing Employer Identities",
                    "Error trying to identify some enum.");
        }
    }

    /**
     * This method contains all the logic to change the exposure level from an identity.
     * @param publicKey
     * @param exposureLevel
     * @throws CantChangeExposureLevelException
     * @throws IdentityNotFoundException
     */
    public final void changeExposureLevel(
            final String publicKey,
            final ExposureLevel exposureLevel) 
            throws CantChangeExposureLevelException, IdentityNotFoundException {
        try {
            final DatabaseTable table = this.database.getTable (
                    EmployerIdentityDatabaseConstants.EMPLOYER_TABLE_NAME);
            table.addStringFilter(
                    EmployerIdentityDatabaseConstants.EMPLOYER_PUBLIC_KEY_COLUMN_NAME,
                    publicKey,
                    DatabaseFilterType.EQUAL);
            table.loadToMemory();
            List<DatabaseTableRecord> records = table.getRecords();
            if (!records.isEmpty()) {
                DatabaseTableRecord record = records.get(0);
                record.setFermatEnum(
                        EmployerIdentityDatabaseConstants.EMPLOYER_EXPOSURE_LEVEL_COLUMN_NAME,
                        exposureLevel);
                table.updateRecord(record);
            } else
                throw new IdentityNotFoundException(
                        "publicKey: "+publicKey+" Cannot find an Identity with that publicKey.");
        } catch (final CantUpdateRecordException e) {
            throw new CantChangeExposureLevelException(
                    e,
                    "Change the exposure level",
                    "Exception not handled by the plugin, there is a problem in database and I cannot update the record.");
        } catch (final CantLoadTableToMemoryException e) {
            throw new CantChangeExposureLevelException(
                    e,
                    "Change the exposure level",
                    "Exception not handled by the plugin, there is a problem in database and I cannot load the table.");
        }
    }

    /**
     * This method returns an identity by a given public key
     * @param publicKey
     * @return
     * @throws CantGetIdentityException
     * @throws IdentityNotFoundException
     */
    public final Employer getIdentity(final String publicKey) 
            throws CantGetIdentityException, 
            IdentityNotFoundException {
        try {
            final DatabaseTable table = this.database.getTable(
                    EmployerIdentityDatabaseConstants.EMPLOYER_TABLE_NAME);
            table.addStringFilter(
                    EmployerIdentityDatabaseConstants.EMPLOYER_PUBLIC_KEY_COLUMN_NAME,
                    publicKey,
                    DatabaseFilterType.EQUAL);
            table.loadToMemory();
            List<DatabaseTableRecord> records = table.getRecords();
            if (!records.isEmpty())
                return getIdentityFromRecord(records.get(0));
            else
                throw new IdentityNotFoundException("publicKey: "+publicKey+" Cannot find an Identity with that publicKey.");
        } catch (
                final CantGetJobActorIdentityPrivateKeyException |
                CantGetJobActorIdentityProfileImageException   |
                InvalidParameterException                        e) {
            throw new CantGetIdentityException(
                    e,
                    "Getting an identity",
                    "Exception not handled by the plugin, there is a problem in database and I cannot update the record.");
        } catch (final CantLoadTableToMemoryException e) {
            throw new CantGetIdentityException(
                    e,
                    "Getting an identity",
                    "Exception not handled by the plugin, there is a problem in database and I cannot load the table.");
        }
    }

    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }
    
    /**
     * This method checks if an alias exists in database.
     * @param alias
     * @return
     * @throws CantCreateNewDeveloperException
     */
    private boolean aliasExists(String alias) throws CantCreateNewDeveloperException {
        try {
            DatabaseTable table = this.database.getTable(
                    EmployerIdentityDatabaseConstants.EMPLOYER_TABLE_NAME);
            table.addStringFilter(
                    EmployerIdentityDatabaseConstants.EMPLOYER_ALIAS_COLUMN_NAME,
                    alias,
                    DatabaseFilterType.EQUAL);
            table.loadToMemory();
            return table.getRecords ().size () > 0;
        } catch (CantLoadTableToMemoryException em) {
            throw new CantCreateNewDeveloperException (
                    em.getMessage(),
                    em,
                    "Employer Identity",
                    "Cant load " + EmployerIdentityDatabaseConstants.EMPLOYER_TABLE_NAME + " table in memory.");
        }
    }

    /**
     * This method persist new Employer Identity Private Key
     * @param publicKey
     * @param privateKey
     * @throws CantPersistPrivateKeyException
     */
    private void  persistNewEmployerIdentityPrivateKeysFile(
            String publicKey,
            String privateKey)
            throws CantPersistPrivateKeyException {
        try {
            PluginTextFile file = this.pluginFileSystem.createTextFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    EmployerIdentityPluginRoot.EMPLOYER_IDENTITY_PRIVATE_KEYS_FILE_NAME + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );
            file.setContent(privateKey);
            file.persistToMedia();
        } catch (CantPersistFileException e) {
            throw new CantPersistPrivateKeyException(
                    "CAN'T PERSIST PRIVATE KEY ",
                    e,
                    "Persisting Employer private key.",
                    "Error persist file.");
        } catch (CantCreateFileException e) {
            throw new CantPersistPrivateKeyException(
                    "CAN'T PERSIST PRIVATE KEY ",
                    e,
                    "Persisting Employer private key.",
                    "Error creating file.");
        }
    }

    /**
     * This method persists the profile image
     * @param publicKey
     * @param profileImage
     * @throws CantPersistProfileImageException
     */
    private void  persistNewEmployerIdentityProfileImage(
            String publicKey,
            byte[] profileImage) 
            throws CantPersistProfileImageException {
        try {
            PluginBinaryFile file = this.pluginFileSystem.createBinaryFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    EmployerIdentityPluginRoot.EMPLOYER_IDENTITY_PROFILE_IMAGE_FILE_NAME + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );
            file.setContent(profileImage);
            file.persistToMedia();
        } catch (CantPersistFileException e) {
            throw new CantPersistProfileImageException(
                    "CAN'T PERSIST PROFILE IMAGE ",
                    e,
                    "Persisting the image profile",
                    "Error persist file.");
        } catch (CantCreateFileException e) {
            throw new CantPersistProfileImageException(
                    "CAN'T PERSIST PROFILE IMAGE ",
                    e,
                    "Persisting the image profile",
                    "Error creating file.");
        }
    }

    /**
     * This method updates the image profile
     * @param publicKey
     * @param profileImage
     * @throws CantPersistProfileImageException
     */
    private void updateEmployerIdentityProfileImage(
            String publicKey,
            byte[] profileImage)
            throws CantPersistProfileImageException {
        try {
            this.pluginFileSystem.deleteBinaryFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    EmployerIdentityPluginRoot.EMPLOYER_IDENTITY_PRIVATE_KEYS_FILE_NAME + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT);

            PluginBinaryFile file = this.pluginFileSystem.createBinaryFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    EmployerIdentityPluginRoot.EMPLOYER_IDENTITY_PROFILE_IMAGE_FILE_NAME + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );
            file.setContent(profileImage);
            file.persistToMedia();
        } catch (CantPersistFileException e) {
            throw new CantPersistProfileImageException(
                    "CAN'T PERSIST PROFILE IMAGE ",
                    e,
                    "Updating the file with the image profile", "Error persisting the file.");
        } catch (CantCreateFileException e) {
            throw new CantPersistProfileImageException(
                    "CAN'T PERSIST PROFILE IMAGE ", 
                    e,
                    "Updating the file with the image profile", 
                    "Error creating file.");
        } catch (FileNotFoundException e) {
            throw new CantPersistProfileImageException(
                    "CAN'T PERSIST PROFILE IMAGE ", 
                    e,
                    "Updating the file with the image profile", 
                    "Error removing file.");
        }
    }

    /**
     * This method get a Employer from database record.
     * @param record
     * @return
     * @throws CantGetJobActorIdentityPrivateKeyException
     * @throws CantGetJobActorIdentityProfileImageException
     * @throws InvalidParameterException
     */
    private Employer getIdentityFromRecord(final DatabaseTableRecord record) 
            throws CantGetJobActorIdentityPrivateKeyException, 
            CantGetJobActorIdentityProfileImageException, 
            InvalidParameterException {
        String alias = record.getStringValue(
                EmployerIdentityDatabaseConstants.EMPLOYER_ALIAS_COLUMN_NAME);
        String privateKey = getEmployerIdentityPrivateKey(
                record.getStringValue(
                        EmployerIdentityDatabaseConstants.EMPLOYER_PUBLIC_KEY_COLUMN_NAME));
        byte[] profileImage = getEmployerIdentityProfileImageByPrivateKey(
                record.getStringValue(
                        EmployerIdentityDatabaseConstants.EMPLOYER_PUBLIC_KEY_COLUMN_NAME));
        ExposureLevel exposureLevel = ExposureLevel.getByCode(
                record.getStringValue(
                        EmployerIdentityDatabaseConstants.EMPLOYER_EXPOSURE_LEVEL_COLUMN_NAME));
        KeyPair keyPair = AsymmetricCryptography.createKeyPair(privateKey);
        long accuracy = record.getLongValue(
                EmployerIdentityDatabaseConstants.EMPLOYER_ACCURACY_COLUMN_NAME);
        Frequency frequency = Frequency.getByCode(
                record.getStringValue(
                        EmployerIdentityDatabaseConstants.EMPLOYER_FREQUENCY_COLUMN_NAME)
        );
        Industry industry = Industry.getByCode(
                record.getStringValue(
                        EmployerIdentityDatabaseConstants.EMPLOYER_INDUSTRY_COLUMN_NAME)
        );
        return new EmployerRecord(
                alias,
                keyPair,
                exposureLevel,
                accuracy,
                frequency,
                industry,
                profileImage);
    }

    /**
     * This method gets the identity private key from file.
     * @param publicKey
     * @return
     * @throws CantGetJobActorIdentityPrivateKeyException
     */
    private String getEmployerIdentityPrivateKey(String publicKey) 
            throws CantGetJobActorIdentityPrivateKeyException {
        try {
            PluginTextFile file = this.pluginFileSystem.getTextFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    EmployerIdentityPluginRoot.EMPLOYER_IDENTITY_PRIVATE_KEYS_FILE_NAME + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );
            file.loadFromMedia();
            return file.getContent();
        } catch (CantLoadFileException e) {
            throw new CantGetJobActorIdentityPrivateKeyException(
                    "CAN'T GET PRIVATE KEY ", 
                    e,
                    "Getting private key from file.", 
                    "Error loaded file.");
        } catch (CantCreateFileException e) {
            throw new CantGetJobActorIdentityPrivateKeyException(
                    "CAN'T GET PRIVATE KEY ",
                    e,
                    "Getting private key from file.",
                    "Error getting developer identity private keys file.");
        } catch (FileNotFoundException e) {
            throw new CantGetJobActorIdentityPrivateKeyException(
                    "CAN'T GET PRIVATE KEY ", 
                    e,
                    "Error getting developer identity private keys file.",
                    "File not found.");
        }
    }

    /**
     * This method returns the image profile from a file by the identity public key
     * @param publicKey
     * @return
     * @throws CantGetJobActorIdentityProfileImageException
     */
    private byte[] getEmployerIdentityProfileImageByPrivateKey(String publicKey) 
            throws CantGetJobActorIdentityProfileImageException {
        byte[] profileImage;
        try {
            PluginBinaryFile file = this.pluginFileSystem.getBinaryFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    EmployerIdentityPluginRoot.EMPLOYER_IDENTITY_PROFILE_IMAGE_FILE_NAME + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );
            file.loadFromMedia();
            profileImage = file.getContent();
        } catch (CantLoadFileException e) {
            throw new CantGetJobActorIdentityProfileImageException(
                    "CAN'T GET IMAGE PROFILE ",
                    e,
                    "Loading image profile",
                    "Error loaded file.");
        } catch (FileNotFoundException |CantCreateFileException e) {
            throw new CantGetJobActorIdentityProfileImageException(
                    "CAN'T GET IMAGE PROFILE ",
                    e,
                    "Loading image profile",
                    "Error getting developer identity private keys file.");
        } catch (Exception e) {
            throw  new CantGetJobActorIdentityProfileImageException(
                    "CAN'T GET IMAGE PROFILE ",
                    FermatException.wrapException(e),
                    "Loading image profile",
                    "Unexpected exception");
        }
        return profileImage;
    }
}
