package org.fermat.fermat_job_plugin.layer.identity.job_seeker.developer.version_1.database;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseDataType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateTableException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.InvalidOwnerIdException;

import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 20/06/16.
 */
public class JobSeekerIdentityDatabaseFactory {
    
    private PluginDatabaseSystem pluginDatabaseSystem;
    /**
     * Constructor with parameters to instantiate class.
     */
    public JobSeekerIdentityDatabaseFactory(final PluginDatabaseSystem pluginDatabaseSystem) {

        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    /**
     * Create the database
     *
     * @param ownerId      the owner id
     * @param databaseName the database name
     * @return Database
     * @throws CantCreateDatabaseException
     */
    protected Database createDatabase(
            final UUID ownerId,
            final String databaseName) throws CantCreateDatabaseException {
        try {
            Database database = this.pluginDatabaseSystem.createDatabase(ownerId, databaseName);
            DatabaseTableFactory table;
            DatabaseFactory databaseFactory = database.getDatabaseFactory();
            /**
             * Create Crypto Broker table.
             */
            table = databaseFactory.newTableFactory(
                    ownerId,
                    JobSeekerIdentityDatabaseConstants.JOB_SEEKER_TABLE_NAME);

            table.addColumn(JobSeekerIdentityDatabaseConstants.JOB_SEEKER_PUBLIC_KEY_COLUMN_NAME, DatabaseDataType.TEXT, 130, Boolean.TRUE );
            table.addColumn(JobSeekerIdentityDatabaseConstants.JOB_SEEKER_ALIAS_COLUMN_NAME, DatabaseDataType.TEXT, 100, Boolean.FALSE);
            table.addColumn(JobSeekerIdentityDatabaseConstants.JOB_SEEKER_DEVICE_USER_PUBLIC_KEY_COLUMN_NAME, DatabaseDataType.TEXT, 130, Boolean.FALSE);
            table.addColumn(JobSeekerIdentityDatabaseConstants.JOB_SEEKER_EXPOSURE_LEVEL_COLUMN_NAME, DatabaseDataType.TEXT,  10, Boolean.FALSE);
            table.addColumn(JobSeekerIdentityDatabaseConstants.JOB_SEEKER_ACCURACY_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0, Boolean.FALSE);
            table.addColumn(JobSeekerIdentityDatabaseConstants.JOB_SEEKER_FREQUENCY_COLUMN_NAME, DatabaseDataType.STRING,  10, Boolean.FALSE);

            table.addIndex(JobSeekerIdentityDatabaseConstants.JOB_SEEKER_FIRST_KEY_COLUMN);

            try {
                //Create the table
                databaseFactory.createTable(ownerId, table);
            } catch (CantCreateTableException e) {
                throw new CantCreateDatabaseException(
                        e,
                        "",
                        "Exception not handled by the plugin, There is a problem and I cannot create the table.");
            }

            return database;

        } catch (final CantCreateDatabaseException e) {
            /**
             * I can not handle this situation.
             */
            throw new CantCreateDatabaseException(
                    e,
                    "",
                    "Exception not handled by the plugin, There is a problem and I cannot create the database.");
        } catch (final InvalidOwnerIdException e) {
            /**
             * This shouldn't happen here because I was the one who gave the owner id to the database file system,
             * but anyway, if this happens, I can not continue.
             */
            throw new CantCreateDatabaseException(
                    e,
                    "",
                    "There is a problem with the ownerId of the database.");
        }

    }

}
