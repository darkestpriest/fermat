package org.fermat.fermat_job_plugin.layer.identity.employer.developer.version_1;

import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededPluginReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.core.PluginInfo;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.location_system.LocationManager;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUserManager;

import org.fermat.fermat_job_api.all_definition.exceptions.CantExposeIdentitiesException;
import org.fermat.fermat_job_api.layer.actor_network_service.job_seeker.interfaces.JobSeekerManager;
import org.fermat.fermat_job_plugin.layer.identity.employer.developer.version_1.database.EmployerIdentityDatabaseDao;
import org.fermat.fermat_job_plugin.layer.identity.employer.developer.version_1.database.EmployerIdentityDeveloperDatabaseFactory;
import org.fermat.fermat_job_plugin.layer.identity.employer.developer.version_1.exceptions.CantInitializeEmployerIdentityDatabaseException;
import org.fermat.fermat_job_plugin.layer.identity.employer.developer.version_1.structure.EmployerIdentityPluginManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 20/06/16.
 */
@PluginInfo(createdBy = "darkestpriest", maintainerMail = "darkpriestrelative@gmail.com", platform = Platforms.JOB_PLATFORM, layer = Layers.IDENTITY, plugin = Plugins.EMPLOYER)
public class EmployerIdentityPluginRoot
        extends AbstractPlugin
        implements DatabaseManagerForDevelopers {

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM       , layer = Layers.USER                 , addon  = Addons.DEVICE_USER           )
    private DeviceUserManager deviceUserManager;

    @NeededAddonReference (platform = Platforms.OPERATIVE_SYSTEM_API    , layer = Layers.SYSTEM               , addon  = Addons .PLUGIN_DATABASE_SYSTEM)
    private PluginDatabaseSystem pluginDatabaseSystem;

    @NeededAddonReference (platform = Platforms.OPERATIVE_SYSTEM_API    , layer = Layers.SYSTEM               , addon  = Addons .PLUGIN_FILE_SYSTEM    )
    private PluginFileSystem pluginFileSystem;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.DEVICE_LOCATION)
    private LocationManager locationManager;

    @NeededPluginReference(platform = Platforms.JOB_PLATFORM  , layer = Layers.ACTOR_NETWORK_SERVICE, plugin = Plugins.JOB_SEEKER         )
    private JobSeekerManager jobSeekerManager;

    /**
     * Represents the plugin manager
     */
    EmployerIdentityPluginManager employerIdentityPluginManager;

    /**
     * Represents the plugin database dao.
     */
    EmployerIdentityDatabaseDao employerIdentityDatabaseDao;

    /**
     * Configuration variables
     */
    public static final String EMPLOYER_IDENTITY_PROFILE_IMAGE_FILE_NAME = "employerIdentityProfileImage";
    public static final String EMPLOYER_IDENTITY_PRIVATE_KEYS_FILE_NAME = "employerIdentityPrivateKey";

    /**
     * Default constructor.
     */
    public EmployerIdentityPluginRoot(){
        super(new PluginVersionReference(new Version()));
    }

    /**
     * This method returns the plugin manager
     * @return
     */
    public FermatManager getManager(){
        return this.employerIdentityPluginManager;
    }

    @Override
    public void start(){
        try{
            //Init database
            initDatabaseDao();
            //Init Manager
            initManager();
            //Test methods
            //testCreateIdentity();
            //Expose identities
            employerIdentityPluginManager.exposeIdentities();
        } catch (CantInitializeEmployerIdentityDatabaseException e) {
            reportError(UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, e);
        } catch (CantExposeIdentitiesException e) {
            reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
        } catch (Exception e){
            reportError(
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    e);
        }
    }

    /**
     * This method init the plugin database dao.
     * @throws CantInitializeEmployerIdentityDatabaseException
     */
    private void initDatabaseDao() throws CantInitializeEmployerIdentityDatabaseException {
        this.employerIdentityDatabaseDao = new EmployerIdentityDatabaseDao(
                pluginDatabaseSystem,
                pluginFileSystem,
                pluginId);
        this.employerIdentityDatabaseDao.initialize();
    }

    /**
     * This method init the plugin manager.
     */
    private void initManager(){
        employerIdentityPluginManager = new EmployerIdentityPluginManager(
                this,
                employerIdentityDatabaseDao,
                deviceUserManager,
                jobSeekerManager,
                locationManager);
    }

    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        EmployerIdentityDeveloperDatabaseFactory employerIdentityDeveloperDatabaseFactory =
                new EmployerIdentityDeveloperDatabaseFactory(
                        this.pluginDatabaseSystem,
                        this.pluginId);
        return employerIdentityDeveloperDatabaseFactory.getDatabaseList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(
            DeveloperObjectFactory developerObjectFactory,
            DeveloperDatabase developerDatabase) {
        EmployerIdentityDeveloperDatabaseFactory employerIdentityDeveloperDatabaseFactory =
                new EmployerIdentityDeveloperDatabaseFactory(
                        this.pluginDatabaseSystem,
                        this.pluginId);
        return employerIdentityDeveloperDatabaseFactory.getDatabaseTableList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(
            DeveloperObjectFactory developerObjectFactory,
            DeveloperDatabase developerDatabase,
            DeveloperDatabaseTable developerDatabaseTable) {
        try {
            EmployerIdentityDeveloperDatabaseFactory employerIdentityDeveloperDatabaseFactory =
                    new EmployerIdentityDeveloperDatabaseFactory(
                            this.pluginDatabaseSystem,
                            this.pluginId);
            employerIdentityDeveloperDatabaseFactory.initializeDatabase();
            return employerIdentityDeveloperDatabaseFactory.getDatabaseTableContent(
                    developerObjectFactory,
                    developerDatabaseTable);
        } catch (CantInitializeEmployerIdentityDatabaseException e) {
            reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }
        return new ArrayList<>();
    }

    /**
     * Test methods.
     * Please, comment it when the tes is finished.
     */
    /*private void testCreateIdentity(){
        try{
            JobSeeker jobSeeker = jobSeekerIdentityPluginManager.createNewIdentity(
                    "Manuel",
                    deviceUserManager.getLoggedInDeviceUser(),
                    new byte[0],
                    ExposureLevel.PUBLISH, 10,
                    Frequency.NORMAL);
            System.out.println("Job Identity Test: "+jobSeeker);
        } catch (Exception e){
            System.out.println("Job Identity Test: "+e);
            e.printStackTrace();
        }
    }*/
}
