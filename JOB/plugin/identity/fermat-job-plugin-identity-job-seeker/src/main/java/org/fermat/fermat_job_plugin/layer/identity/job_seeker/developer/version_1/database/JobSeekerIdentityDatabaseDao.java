package org.fermat.fermat_job_plugin.layer.identity.job_seeker.developer.version_1.database;

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
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantGetJobActorIdentityPrivateKeyException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantGetJobActorIdentityProfileImageException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantListJobsPlatformIdentitiesException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantPersistProfileImageException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantUpdateJobPlatformIdentityException;
import org.fermat.fermat_job_api.layer.identity.job_seeker.interfaces.JobSeeker;
import org.fermat.fermat_job_api.layer.identity.job_seeker.util.JobSeekerRecord;
import org.fermat.fermat_job_plugin.layer.identity.job_seeker.developer.version_1.JobSeekerIdentityPluginRoot;
import org.fermat.fermat_job_plugin.layer.identity.job_seeker.developer.version_1.exceptions.CantInitializeJobSeekerIdentityDatabaseException;
import org.fermat.fermat_job_api.layer.identity.common.exceptions.CantPersistPrivateKeyException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 20/06/16.
 */
public class JobSeekerIdentityDatabaseDao implements DealsWithPluginDatabaseSystem {

    private PluginDatabaseSystem pluginDatabaseSystem;
    private PluginFileSystem pluginFileSystem;
    private UUID pluginId;
    private Database database;

    public JobSeekerIdentityDatabaseDao(
            PluginDatabaseSystem pluginDatabaseSystem,
            PluginFileSystem pluginFileSystem,
            UUID pluginId) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginFileSystem = pluginFileSystem;
        this.pluginId = pluginId;
    }

    public void initialize() throws CantInitializeJobSeekerIdentityDatabaseException {
        try {
            database = this.pluginDatabaseSystem.openDatabase(this.pluginId, this.pluginId.toString());
        } catch (DatabaseNotFoundException e) {
            try {
                JobSeekerIdentityDatabaseFactory databaseFactory =
                        new JobSeekerIdentityDatabaseFactory(pluginDatabaseSystem);
                database = databaseFactory.createDatabase(pluginId, pluginId.toString());
            } catch (CantCreateDatabaseException f) {
                throw new CantInitializeJobSeekerIdentityDatabaseException(
                        CantCreateDatabaseException.DEFAULT_MESSAGE,
                        f,
                        "",
                        "There is a problem and i cannot create the database.");
            } catch (Exception z) {
                throw new CantInitializeJobSeekerIdentityDatabaseException(
                        CantOpenDatabaseException.DEFAULT_MESSAGE,
                        z,
                        "",
                        "Generic Exception.");
            }
        } catch (CantOpenDatabaseException e) {
            throw new CantInitializeJobSeekerIdentityDatabaseException(
                    CantOpenDatabaseException.DEFAULT_MESSAGE,
                    e,
                    "",
                    "Exception not handled by the plugin, there is a problem and i cannot open the database.");
        } catch (Exception e) {
            throw new CantInitializeJobSeekerIdentityDatabaseException(
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
    public void createNewJobSeekerIdentity(
            final String alias,
            final DeviceUser deviceUser,
            byte[] imageProfile,
            ExposureLevel exposureLevel,
            long accuracy,
            Frequency frequency) throws CantCreateNewDeveloperException {
        try {
            if (aliasExists (alias)) {
                throw new CantCreateNewDeveloperException (
                        "Cant create new Job Seeker Identity, alias exists.",
                        "Job Seeker Identity",
                        "Cant create new Job Seeker Identity, alias exists.");
            }
            KeyPair eccKeyPair = new ECCKeyPair();
            String publicKey = eccKeyPair.getPublicKey();
            persistNewJobSeekerIdentityPrivateKeysFile(
                    publicKey,
                    eccKeyPair.getPrivateKey());
            DatabaseTable table = this.database.getTable(
                    JobSeekerIdentityDatabaseConstants.JOB_SEEKER_TABLE_NAME);
            DatabaseTableRecord record = table.getEmptyRecord();
            record.setStringValue(
                    JobSeekerIdentityDatabaseConstants.JOB_SEEKER_PUBLIC_KEY_COLUMN_NAME,
                    publicKey);
            record.setStringValue(
                    JobSeekerIdentityDatabaseConstants.JOB_SEEKER_ALIAS_COLUMN_NAME,
                    alias);
            record.setStringValue(
                    JobSeekerIdentityDatabaseConstants.JOB_SEEKER_DEVICE_USER_PUBLIC_KEY_COLUMN_NAME,
                    deviceUser.getPublicKey());
            record.setFermatEnum(
                    JobSeekerIdentityDatabaseConstants.JOB_SEEKER_EXPOSURE_LEVEL_COLUMN_NAME,
                    exposureLevel);
            record.setLongValue(
                    JobSeekerIdentityDatabaseConstants.JOB_SEEKER_ACCURACY_COLUMN_NAME,
                    accuracy);
            record.setFermatEnum(
                    JobSeekerIdentityDatabaseConstants.JOB_SEEKER_FREQUENCY_COLUMN_NAME,
                    frequency);
            table.insertRecord(record);
            persistNewJobSeekerIdentityProfileImage(
                    publicKey,
                    imageProfile);
        } catch (CantInsertRecordException e){
            throw new CantCreateNewDeveloperException (
                    e.getMessage(),
                    e,
                    "Job Seeker Identity",
                    "Cannot create new Job Seeker Identity, insert database problems.");
        } catch (CantPersistPrivateKeyException e){
            throw new CantCreateNewDeveloperException (
                    e.getMessage(), 
                    e, 
                    "Job Seeker Identity", 
                    "Cant create new Job Seeker Identity,persist private key error.");
        } catch (Exception e) {
            throw new CantCreateNewDeveloperException (
                    e.getMessage(), 
                    FermatException.wrapException(e),
                    "Job Seeker Identity", 
                    "Cant create new Job Seeker Identity, unknown failure.");
        }
    }

    public void updateCryptoBrokerIdentity(
            String alias, 
            String publicKey,
            byte[] imageProfile,
            long accuracy,
            Frequency frequency) throws CantUpdateJobPlatformIdentityException {
        try {
            DatabaseTable table = this.database.getTable(
                    JobSeekerIdentityDatabaseConstants.JOB_SEEKER_TABLE_NAME);
            DatabaseTableRecord record = table.getEmptyRecord();
            table.addStringFilter(
                    JobSeekerIdentityDatabaseConstants.JOB_SEEKER_PUBLIC_KEY_COLUMN_NAME,
                    publicKey,
                    DatabaseFilterType.EQUAL);
            record.setStringValue(
                    JobSeekerIdentityDatabaseConstants.JOB_SEEKER_ALIAS_COLUMN_NAME,
                    alias);
            record.setLongValue(
                    JobSeekerIdentityDatabaseConstants.JOB_SEEKER_ACCURACY_COLUMN_NAME,
                    accuracy);
            record.setFermatEnum(
                    JobSeekerIdentityDatabaseConstants.JOB_SEEKER_FREQUENCY_COLUMN_NAME,
                    frequency);
            table.updateRecord(record);
            updateJobSeekerIdentityProfileImage(publicKey, imageProfile);
        } catch (CantUpdateRecordException e) {
            throw new CantUpdateJobPlatformIdentityException(
                    e.getMessage(), 
                    e, 
                    "Updating Job Seeker Identity",
                    "Cannot update the record in database");
        } catch (CantPersistProfileImageException e) {
            throw new CantUpdateJobPlatformIdentityException(
                    e.getMessage(), 
                    e, 
                    "Updating Job Seeker Identity",
                    "Cannot persist the image");
        }
    }

    /**
     * This method returns the identities created in the device.
     * @param deviceUser
     * @return
     * @throws CantListJobsPlatformIdentitiesException
     */
    public final List<JobSeeker> listIdentitiesFromDeviceUser(
            final DeviceUser deviceUser) 
            throws CantListJobsPlatformIdentitiesException {
        try {
            final DatabaseTable table = this.database.getTable (
                    JobSeekerIdentityDatabaseConstants.JOB_SEEKER_TABLE_NAME);
            table.addStringFilter(
                    JobSeekerIdentityDatabaseConstants.JOB_SEEKER_DEVICE_USER_PUBLIC_KEY_COLUMN_NAME,
                    deviceUser.getPublicKey(),
                    DatabaseFilterType.EQUAL);
            table.loadToMemory();
            final List<JobSeeker> list = new ArrayList<>();
            for (DatabaseTableRecord record : table.getRecords ())
                list.add(getIdentityFromRecord(record));
            return list;
        } catch (final CantGetJobActorIdentityProfileImageException e) {
            throw new CantListJobsPlatformIdentitiesException(
                    e.getMessage(),
                    e,
                    "Listing Job Seeker Identities",
                    "Problem trying to get the profile image of the identity.");
        } catch (final CantLoadTableToMemoryException e) {
            throw new CantListJobsPlatformIdentitiesException(
                    e.getMessage(),
                    e,
                    "Listing Job Seeker Identities",
                    "Cant load " + JobSeekerIdentityDatabaseConstants.JOB_SEEKER_TABLE_NAME + " table in memory.");
        } catch (final CantGetJobActorIdentityPrivateKeyException e) {
            throw new CantListJobsPlatformIdentitiesException(
                    e.getMessage(),
                    e,
                    "Listing Job Seeker Identities",
                    "Can't get private key.");
        } catch (final InvalidParameterException e) {
            throw new CantListJobsPlatformIdentitiesException(
                    e.getMessage(),
                    FermatException.wrapException(e),
                    "Listing Job Seeker Identities",
                    "Error trying to identify some enum.");
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
                    JobSeekerIdentityDatabaseConstants.JOB_SEEKER_TABLE_NAME);
            table.addStringFilter(
                    JobSeekerIdentityDatabaseConstants.JOB_SEEKER_ALIAS_COLUMN_NAME,
                    alias,
                    DatabaseFilterType.EQUAL);
            table.loadToMemory();
            return table.getRecords ().size () > 0;
        } catch (CantLoadTableToMemoryException em) {
            throw new CantCreateNewDeveloperException (
                    em.getMessage(),
                    em,
                    "Job Seeker Identity",
                    "Cant load " + JobSeekerIdentityDatabaseConstants.JOB_SEEKER_TABLE_NAME + " table in memory.");
        }
    }

    /**
     * This method persist new Job Seeker Identity Private Key
     * @param publicKey
     * @param privateKey
     * @throws CantPersistPrivateKeyException
     */
    private void  persistNewJobSeekerIdentityPrivateKeysFile(
            String publicKey,
            String privateKey)
            throws CantPersistPrivateKeyException {
        try {
            PluginTextFile file = this.pluginFileSystem.createTextFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    JobSeekerIdentityPluginRoot.JOB_SEEKER_IDENTITY_PRIVATE_KEYS_FILE_NAME + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );
            file.setContent(privateKey);
            file.persistToMedia();
        } catch (CantPersistFileException e) {
            throw new CantPersistPrivateKeyException(
                    "CAN'T PERSIST PRIVATE KEY ",
                    e,
                    "Persisting Job Seeker private key.",
                    "Error persist file.");
        } catch (CantCreateFileException e) {
            throw new CantPersistPrivateKeyException(
                    "CAN'T PERSIST PRIVATE KEY ",
                    e,
                    "Persisting Job Seeker private key.",
                    "Error creating file.");
        }
    }

    /**
     * This method persists the profile image
     * @param publicKey
     * @param profileImage
     * @throws CantPersistProfileImageException
     */
    private void  persistNewJobSeekerIdentityProfileImage(
            String publicKey,
            byte[] profileImage) 
            throws CantPersistProfileImageException {
        try {
            PluginBinaryFile file = this.pluginFileSystem.createBinaryFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    JobSeekerIdentityPluginRoot.JOB_SEEKER_IDENTITY_PROFILE_IMAGE_FILE_NAME + "_" + publicKey,
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
    private void updateJobSeekerIdentityProfileImage(
            String publicKey,
            byte[] profileImage)
            throws CantPersistProfileImageException {
        try {
            this.pluginFileSystem.deleteBinaryFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    JobSeekerIdentityPluginRoot.JOB_SEEKER_IDENTITY_PRIVATE_KEYS_FILE_NAME + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT);

            PluginBinaryFile file = this.pluginFileSystem.createBinaryFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    JobSeekerIdentityPluginRoot.JOB_SEEKER_IDENTITY_PROFILE_IMAGE_FILE_NAME + "_" + publicKey,
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
     * This method get a Job Seeker from database record.
     * @param record
     * @return
     * @throws CantGetJobActorIdentityPrivateKeyException
     * @throws CantGetJobActorIdentityProfileImageException
     * @throws InvalidParameterException
     */
    private JobSeeker getIdentityFromRecord(final DatabaseTableRecord record) 
            throws CantGetJobActorIdentityPrivateKeyException, 
            CantGetJobActorIdentityProfileImageException, 
            InvalidParameterException {
        String alias = record.getStringValue(
                JobSeekerIdentityDatabaseConstants.JOB_SEEKER_ALIAS_COLUMN_NAME);
        String privateKey = getJobSeekerIdentityPrivateKey(
                record.getStringValue(
                        JobSeekerIdentityDatabaseConstants.JOB_SEEKER_PUBLIC_KEY_COLUMN_NAME));
        byte[] profileImage = getJobSeekerIdentityProfileImageByPrivateKey(
                record.getStringValue(
                        JobSeekerIdentityDatabaseConstants.JOB_SEEKER_PUBLIC_KEY_COLUMN_NAME));
        ExposureLevel exposureLevel = ExposureLevel.getByCode(
                record.getStringValue(
                        JobSeekerIdentityDatabaseConstants.JOB_SEEKER_EXPOSURE_LEVEL_COLUMN_NAME));
        KeyPair keyPair = AsymmetricCryptography.createKeyPair(privateKey);
        long accuracy = record.getLongValue(
                JobSeekerIdentityDatabaseConstants.JOB_SEEKER_ACCURACY_COLUMN_NAME);
        Frequency frequency = Frequency.getByCode(
                JobSeekerIdentityDatabaseConstants.JOB_SEEKER_FREQUENCY_COLUMN_NAME);
        return new JobSeekerRecord(
                alias,
                keyPair,
                exposureLevel,
                accuracy,
                frequency,
                profileImage);
    }

    /**
     * This method gets the identity private key from file.
     * @param publicKey
     * @return
     * @throws CantGetJobActorIdentityPrivateKeyException
     */
    private String getJobSeekerIdentityPrivateKey(String publicKey) 
            throws CantGetJobActorIdentityPrivateKeyException {
        try {
            PluginTextFile file = this.pluginFileSystem.getTextFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    JobSeekerIdentityPluginRoot.JOB_SEEKER_IDENTITY_PRIVATE_KEYS_FILE_NAME + "_" + publicKey,
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
    private byte[] getJobSeekerIdentityProfileImageByPrivateKey(String publicKey) 
            throws CantGetJobActorIdentityProfileImageException {
        byte[] profileImage;
        try {
            PluginBinaryFile file = this.pluginFileSystem.getBinaryFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    JobSeekerIdentityPluginRoot.JOB_SEEKER_IDENTITY_PROFILE_IMAGE_FILE_NAME + "_" + publicKey,
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
