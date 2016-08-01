package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.EventManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.all_definition.util.ip_address.IPAddressHelper;
import com.bitdubai.fermat_api.layer.core.PluginInfo;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginTextFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;
import com.bitdubai.fermat_api.layer.osa_android.location_system.LocationManager;
import com.bitdubai.fermat_api.layer.osa_android.location_system.exceptions.CantGetDeviceLocationException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.NetworkNodeManager;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NodeProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.GsonProvider;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.FermatEmbeddedNodeServer;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.PropagateCatalogAgent;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.actors.ActorsCatalogPropagationConfiguration;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.nodes.NodesCatalogPropagationConfiguration;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.clients.FermatWebSocketClientNodeChannel;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContext;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContextItem;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.AddNodeToCatalogRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.GetActorsCatalogRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.GetNodeCatalogRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.UpdateNodeInCatalogRequest;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.DatabaseManager;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.GeoLocation;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantDeleteRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInitializeCommunicationsNetworkNodeP2PDatabaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInitializeNetworkNodeIdentityException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.ConfigurationManager;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.HexadecimalConverter;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.SeedServerConf;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.util.UPNPService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.ClassUtils;
import org.jboss.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.NetworkNodePluginRoot</code> is
 * responsible of initialize all the component to work together
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 11/11/15.
 *
 * @author  Rart3001
 * @version 1.0
 * @since   Java JDK 1.7
 */
@PluginInfo(createdBy = "Roberto Requena", maintainerMail = "rart3001@gmail.com", platform = Platforms.COMMUNICATION_PLATFORM, layer = Layers.COMMUNICATION, plugin = Plugins.NETWORK_NODE)
public class NetworkNodePluginRoot extends AbstractPlugin implements NetworkNodeManager {

    /**
     * Represent the LOG
     */
    private static final Logger LOG = Logger.getLogger(ClassUtils.getShortClassName(NetworkNodePluginRoot.class));

    /**
     * Represent the IDENTITY_FILE_DIRECTORY
     */
    private static final String IDENTITY_FILE_DIRECTORY = "private";

    /**
     * Represent the IDENTITY_FILE_NAME
     */
    private static final String IDENTITY_FILE_NAME      = "nodeIdentity";

    /**
     * EventManager references definition.
     */
    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.EVENT_MANAGER)
    private EventManager eventManager;

    /**
     * EventManager references definition.
     */
    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.DEVICE_LOCATION)
    private LocationManager locationManager;

    /**
     * PluginFileSystem references definition.
     */
    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_FILE_SYSTEM)
    protected PluginFileSystem pluginFileSystem;

    /**
     * Represent the node identity
     */
    private ECCKeyPair identity;

    /**
     * Represent the propagateCatalogAgent
     */
    private PropagateCatalogAgent propagateCatalogAgent;

    /**
     * Represent the fermatEmbeddedNodeServer instance
     */
    private FermatEmbeddedNodeServer fermatEmbeddedNodeServer;

    /**
     * Represent the nodeProfile
     */
    private NodeProfile nodeProfile;

    /**
     * Represent the server public ip
     */
    private String serverPublicIp;

    /**
     * Constructor
     */
    public NetworkNodePluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    /**
     * (non-javadoc)
     *
     * @see AbstractPlugin#start()
     */
    @Override
    public void start() throws CantStartPluginException {

        LOG.info("Calling method - start()...");
        LOG.info("pluginId = " + pluginId);

        /*
         * Validate required resources
         */
        validateInjectedResources();

        try {

            /*
             * Clean Sessions tables
             */
            cleanSessionTables();

            /*
             * Initialize the identity of the node
             */
            initializeIdentity();

            /*
             * Initialize the configuration file
             */
            initializeConfigurationFile();

            /*
             * Get the server ip
             */
            generateNodePublicIp();

            /*
             * Generate the profile of the node
             */
            generateNodeProfile();

            /*
             * Create and start the internal server
             */
            fermatEmbeddedNodeServer = new FermatEmbeddedNodeServer();
            fermatEmbeddedNodeServer.start();

            LOG.info("Add references to the node context...");

            /*
             * Add references to the node context
             */
            NodeContext.add(NodeContextItem.EVENT_MANAGER, eventManager);
            NodeContext.add(NodeContextItem.FERMAT_EMBEDDED_NODE_SERVER, fermatEmbeddedNodeServer);
            NodeContext.add(NodeContextItem.PLUGIN_FILE_SYSTEM, pluginFileSystem);
            NodeContext.add(NodeContextItem.PLUGIN_ROOT, this);

            /*
             * Process the node catalog
             */
            initializeNodeCatalog();

            /*
             * Initialize propagate catalog agents
             */
           // LOG.info("Initializing propagate catalog agents ...");
            this.propagateCatalogAgent = new PropagateCatalogAgent(this);
            this.propagateCatalogAgent.start();

            /*
             * Try to forwarding port
             */
            UPNPService.portForwarding(Integer.parseInt(ConfigurationManager.getValue(ConfigurationManager.PORT)), ConfigurationManager.getValue(ConfigurationManager.NODE_NAME));

        } catch (CantInitializeCommunicationsNetworkNodeP2PDatabaseException exception) {


            exception.printStackTrace();

            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Error trying to initialize the network node database.");
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("Plugin ID: ");
            contextBuffer.append(pluginId.toString());

            String context = contextBuffer.toString();
            String possibleCause = "The Network Node Service triggered an unexpected problem that wasn't able to solve by itself";
            CantStartPluginException pluginStartException = new CantStartPluginException(CantStartPluginException.DEFAULT_MESSAGE, exception, context, possibleCause);

            super.reportError(UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, pluginStartException);

            throw pluginStartException;

        } catch (Exception exception) {

            exception.printStackTrace();

            String context = "Plugin ID: " + pluginId;
            String possibleCause = "The Network Node Service triggered an unexpected problem that wasn't able to solve by itself";
            CantStartPluginException pluginStartException = new CantStartPluginException(CantStartPluginException.DEFAULT_MESSAGE, exception, context, possibleCause);

            super.reportError(UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, pluginStartException);
            throw pluginStartException;
        }
    }

    @Override
    public void pause() {

        try {

            this.propagateCatalogAgent.pause();

        } catch (Exception e) {

            super.reportError(UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, e);

        }
    }

    @Override
    public void resume() {

        try {

            this.propagateCatalogAgent.resume();

        } catch (Exception e) {

            super.reportError(UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, e);
        }
    }

    @Override
    public void stop() {

        try {

            this.propagateCatalogAgent.stop();
            UPNPService.removePortForwarding(Integer.parseInt(ConfigurationManager.getValue(ConfigurationManager.PORT)));
            DatabaseManager.closeDataBase();

        } catch (Exception e) {

            super.reportError(UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, e);

        }
    }

    /**
     * Generate de node public ip
     */
    private void generateNodePublicIp() {

        try {

            if (ConfigurationManager.getValue(ConfigurationManager.PUBLIC_IP).equals(FermatEmbeddedNodeServer.DEFAULT_IP)){

                serverPublicIp = IPAddressHelper.getCurrentIPAddress();
                LOG.info(">>>> Server public ip: " + serverPublicIp + " get by online service");
                ConfigurationManager.updateValue(ConfigurationManager.PUBLIC_IP, serverPublicIp);

            }else {

                serverPublicIp = ConfigurationManager.getValue(ConfigurationManager.PUBLIC_IP);
                LOG.info(">>>> Server public ip: " + serverPublicIp + " get from configuration file");
            }

        }catch (Exception e){

            LOG.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            LOG.warn("! Could not get the external ip with the online service, it must be configured manually in the configuration file !");
            LOG.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            serverPublicIp = ConfigurationManager.getValue(ConfigurationManager.PUBLIC_IP);
        }
    }

    /**
     * Generate the node location
     * @return Location
     */
    private Location generateNodeLocation(){

        Location location = null;

        try {

            if (ConfigurationManager.getValue(ConfigurationManager.LATITUDE).equals("0.0") && ConfigurationManager.getValue(ConfigurationManager.LONGITUDE).equals("0.0")){

                LOG.info(">>>> Trying to get the location of the node...");
                location = locationManager.getLocation();
                ConfigurationManager.updateValue(ConfigurationManager.LATITUDE, location.getLatitude().toString());
                ConfigurationManager.updateValue(ConfigurationManager.LONGITUDE, location.getLongitude().toString());

            }else {

                LOG.info(">>>> Getting the location from the configuration file");
                location = new GeoLocation(nodeProfile.getIdentityPublicKey(), new Double(ConfigurationManager.getValue(ConfigurationManager.LATITUDE)), new Double(ConfigurationManager.getValue(ConfigurationManager.LONGITUDE)));
            }

        }catch (Exception e){

            LOG.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            LOG.warn("! Could not get the location with the online service, it must be configured manually in the configuration file !");
            LOG.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            location = new GeoLocation(nodeProfile.getIdentityPublicKey(), new Double(ConfigurationManager.getValue(ConfigurationManager.LATITUDE)), new Double(ConfigurationManager.getValue(ConfigurationManager.LONGITUDE)));
        }

        return location;

    }

    /**
     * Generate the node profile of this node
     */
    private void generateNodeProfile() throws CantGetDeviceLocationException {

        LOG.info("Generating Node Profile...");

        nodeProfile = new NodeProfile();
        nodeProfile.setIdentityPublicKey(identity.getPublicKey());
        nodeProfile.setIp(serverPublicIp);
        nodeProfile.setDefaultPort(Integer.valueOf(ConfigurationManager.getValue(ConfigurationManager.PORT)));
        nodeProfile.setName(ConfigurationManager.getValue(ConfigurationManager.NODE_NAME));
        nodeProfile.setLocation(generateNodeLocation());

        LOG.info("Node Profile = "+nodeProfile);

    }

    /**
     * Initializes the configuration file
     */
    private void initializeConfigurationFile() throws ConfigurationException, IOException {

        LOG.info("Starting initializeConfigurationFile()...");

        if(ConfigurationManager.isExist()){

            ConfigurationManager.load();

        }else {

            LOG.info("Configuration file doesn't exit");
            ConfigurationManager.create(identity.getPublicKey());
            ConfigurationManager.load();
        }

    }

    /**
     * This method validates if all required resources are injected into
     * the plugin root by the fermat system.
     *
     * @throws CantStartPluginException if something goes wrong.
     */
    private void validateInjectedResources() throws CantStartPluginException {

         /*
         * If all resources are inject
         */
        if (eventManager == null   ||
              pluginFileSystem == null ) {

            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Plugin ID: " + pluginId);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("pluginFileSystem: " + pluginFileSystem);

            String context = contextBuffer.toString();
            String possibleCause = "No all required resource are injected";
            CantStartPluginException pluginStartException = new CantStartPluginException(CantStartPluginException.DEFAULT_MESSAGE, null, context, possibleCause);

            super.reportError(UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, pluginStartException);

            throw pluginStartException;

        }

    }

    /**
     * Initializes the identity of this plugin
     */
    private void initializeIdentity() throws CantInitializeNetworkNodeIdentityException {

        LOG.info("Calling method - initializeIdentity()...");

        try {

            LOG.info("Loading identity...");

         /*
          * Load the file with the identity
          */
            PluginTextFile pluginTextFile = pluginFileSystem.getTextFile(pluginId, IDENTITY_FILE_DIRECTORY, IDENTITY_FILE_NAME, FilePrivacy.PRIVATE, FileLifeSpan.PERMANENT);
            String content = pluginTextFile.getContent();

            LOG.info("content = " + content);

            identity = new ECCKeyPair(content);

        } catch (FileNotFoundException e) {

            /*
             * The file does not exist, maybe is the first time that the plugin is running on this device,
             * We need to create a new identity for the network node.
             */
            try {

                LOG.info("No previous identity found - Proceeding to create new one...");

                /*
                 * Create the new identity
                 */
                identity = new ECCKeyPair();

                LOG.info("identity.getPrivateKey() = " + identity.getPrivateKey());
                LOG.info("identity.getPublicKey() = " + identity.getPublicKey());

                /*
                 * save the identity into the identity file
                 */
                PluginTextFile pluginTextFile = pluginFileSystem.createTextFile(pluginId, IDENTITY_FILE_DIRECTORY, IDENTITY_FILE_NAME, FilePrivacy.PRIVATE, FileLifeSpan.PERMANENT);
                pluginTextFile.setContent(identity.getPrivateKey());
                pluginTextFile.persistToMedia();

            } catch (Exception exception) {
                /*
                 * The file cannot be created. I can not handle this situation.
                 */
                super.reportError(UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, exception);

                throw new CantInitializeNetworkNodeIdentityException(exception, "", "Unhandled Error.");
            }


        } catch (CantCreateFileException cantCreateFileException) {

            /*
             * The file cannot be load. I can not handle this situation.
             */
            super.reportError(UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, cantCreateFileException);

            throw new CantInitializeNetworkNodeIdentityException(cantCreateFileException, "", "Error trying to create the file.");

        }

    }

    /**
     * Create a new instance of the client to the seed node
     * @return
     */
    private FermatWebSocketClientNodeChannel getFermatWebSocketClientNodeChannelInstanceSeedNode(){

        return new FermatWebSocketClientNodeChannel(SeedServerConf.DEFAULT_IP, SeedServerConf.DEFAULT_PORT);
    }

    /**
     * Validate if the current node belongs to the list of seed servers
     *
     * @return boolean
     */
    private boolean isSeedServer(String serverIp){

        if (serverIp.equals(SeedServerConf.DEFAULT_IP)){
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    /**
     * Method that validate if the node profile registered had changed
     * from the registration
     */
    private boolean validateNodeProfileRegisterChange() {

        String jsonString = new String(HexadecimalConverter.convertHexStringToByteArray(ConfigurationManager.getValue(ConfigurationManager.LAST_REGISTER_NODE_PROFILE)));

        LOG.info("Last Profile Registered = " + jsonString);

        NodeProfile lastNodeProfileRegister = NodeProfile.fromJson(jsonString);
        if (!nodeProfile.equals(lastNodeProfileRegister)){
            return Boolean.TRUE;
        }

        return Boolean.FALSE;

    }

    /**
     * Method that requests to the seed server the registration of
     * the current node profile.
     */
    private void requestRegisterProfileInTheNodeCatalog(){

        try {

            LOG.info("Requesting registration of the node profile in the node catalog...");

            FermatWebSocketClientNodeChannel fermatWebSocketClientNodeChannel = getFermatWebSocketClientNodeChannelInstanceSeedNode();
            AddNodeToCatalogRequest addNodeToCatalogMsgRequest = new AddNodeToCatalogRequest(nodeProfile);
            fermatWebSocketClientNodeChannel.sendMessage(addNodeToCatalogMsgRequest.toJson(), PackageType.ADD_NODE_TO_CATALOG_REQUEST);

        }catch (Exception e){
            LOG.error("Can't request Register Profile In The Node Catalog: ", e);

        }
    }

    /**
     * Method that requests to the seed server to update
     * the profile of this node
     */
    private void requestUpdateProfileInTheNodeCatalog(){

        try {

            LOG.info("Requesting update of the profile on the node catalog...");

            FermatWebSocketClientNodeChannel fermatWebSocketClientNodeChannel = getFermatWebSocketClientNodeChannelInstanceSeedNode();
            UpdateNodeInCatalogRequest updateNodeInCatalogMsgRequest = new UpdateNodeInCatalogRequest(nodeProfile);
            fermatWebSocketClientNodeChannel.sendMessage(updateNodeInCatalogMsgRequest.toJson(), PackageType.UPDATE_NODE_IN_CATALOG_REQUEST);

        }catch (Exception e){
            LOG.error("Can't request Update Profile In The Node Catalog: ", e);
        }
    }

    /**
     * Validate if the catalog is empty
     * if it is, we'll request the data to the seed server
     *
     * @throws CantReadRecordDataBaseException if something goes wrong.
     */
    private void requestNodesCatalogTransactions() throws CantReadRecordDataBaseException {

        try {

            LOG.info("Request the list of transactions in the node catalog");

            FermatWebSocketClientNodeChannel fermatWebSocketClientNodeChannel = getFermatWebSocketClientNodeChannelInstanceSeedNode();
            GetNodeCatalogRequest getNodeCatalogTransactionsMsjRequest = new GetNodeCatalogRequest(0, NodesCatalogPropagationConfiguration.MAX_REQUESTABLE_ITEMS);
            fermatWebSocketClientNodeChannel.sendMessage(getNodeCatalogTransactionsMsjRequest.toJson(), PackageType.GET_NODE_CATALOG_REQUEST);

        }catch (Exception e){
            LOG.error("Can't request Nodes Catalog Transactions: ", e);
        }
    }

    /**
     * Validate if the catalog if empty request the data to the seed server
     *
     * @throws CantReadRecordDataBaseException
     */
    private void requestActorsCatalogTransactions() throws CantReadRecordDataBaseException {

        try {

            LOG.info("Request the list of transactions in the actors catalog");

            FermatWebSocketClientNodeChannel fermatWebSocketClientNodeChannel = getFermatWebSocketClientNodeChannelInstanceSeedNode();
            GetActorsCatalogRequest getActorCatalogTransactionsMsjRequest = new GetActorsCatalogRequest(0, ActorsCatalogPropagationConfiguration.MAX_REQUESTABLE_ITEMS);
            fermatWebSocketClientNodeChannel.sendMessage(getActorCatalogTransactionsMsjRequest.toJson(), PackageType.GET_ACTOR_CATALOG_REQUEST);


        } catch (Exception e){
            LOG.error("Can't request Actors Catalog Transactions: ", e);
        }

    }

    /**
     * Process the node into the node catalog
     */
    private void initializeNodeCatalog() throws Exception {

        LOG.info("Initialize node catalog");
        boolean isSeedServer = isSeedServer(this.serverPublicIp);
        Boolean isRegister = isRegisterInNodeCatalog(isSeedServer);

        LOG.info("Is Register? = " + isRegister);
        LOG.info("Am I a Seed Node? = " + isSeedServer);

        /*
         * Validate if the node are the seed server
         */
        if (isSeedServer){

            /*
             * Validate if the node is registered in the node catalog
             */
            if (isRegister){

                /*
                 * Validate if the node server profile register had changed
                 */
                if (validateNodeProfileRegisterChange()){
                    updateNodeProfileOnCatalog();
                }

            } else {
                insertNodeProfileIntoCatalog();
            }

        } else {

            /*
             * Validate if the node is registered in the node catalog
             */
            if (isRegister){

                    /*
                     * Validate if the node server profile register had changed
                     */
                if (validateNodeProfileRegisterChange()){
                    requestUpdateProfileInTheNodeCatalog();
                }

            }else {
                requestRegisterProfileInTheNodeCatalog();
            }

            requestNodesCatalogTransactions();
            requestActorsCatalogTransactions();
        }
    }

    /**
     * Insert the node profile into the catalog
     * @throws CantInsertRecordDataBaseException
     */
    private void insertNodeProfileIntoCatalog() throws Exception {

        LOG.info("Inserting my profile in my the node catalog...");

        /*
         * Create the NodesCatalog entity
         */
        NodeCatalog nodeCatalog = new NodeCatalog();
        nodeCatalog.setIp(nodeProfile.getIp());
        nodeCatalog.setDefaultPort(nodeProfile.getDefaultPort());
        nodeCatalog.setId(nodeProfile.getIdentityPublicKey());
        nodeCatalog.setName(nodeProfile.getName());
        nodeCatalog.setOfflineCounter(0);
        nodeCatalog.setLastConnectionTimestamp(new Timestamp(System.currentTimeMillis()));
        nodeCatalog.setTriedToPropagateTimes(0);
        nodeCatalog.setLocation(new GeoLocation(nodeProfile.getIdentityPublicKey(), nodeProfile.getLocation().getLatitude(), nodeProfile.getLocation().getLongitude()));
        nodeCatalog.setVersion(0);
        nodeCatalog.setPendingPropagations(NodesCatalogPropagationConfiguration.DESIRED_PROPAGATIONS);

        /*
         * Insert NodesCatalog into data base
         */

        JPADaoFactory.getNodeCatalogDao().persist(nodeCatalog);

        ConfigurationManager.updateValue(ConfigurationManager.REGISTERED_IN_CATALOG, String.valueOf(Boolean.TRUE));
        ConfigurationManager.updateValue(ConfigurationManager.LAST_REGISTER_NODE_PROFILE, HexadecimalConverter.convertHexString(nodeProfile.toJson().getBytes("UTF-8")));

    }

    /**
     * Update the node profile into the catalog
     * @throws CantInsertRecordDataBaseException
     */
    private void updateNodeProfileOnCatalog() throws Exception {

        LOG.info("Updating my profile in the node catalog");

        if (JPADaoFactory.getNodeCatalogDao().exist(nodeProfile.getIdentityPublicKey())) {

            /*
             * Create the NodesCatalog entity
             */
            NodeCatalog nodeCatalog = new NodeCatalog();
            nodeCatalog.setIp(nodeProfile.getIp());
            nodeCatalog.setDefaultPort(nodeProfile.getDefaultPort());
            nodeCatalog.setId(nodeProfile.getIdentityPublicKey());
            nodeCatalog.setName(nodeProfile.getName());
            nodeCatalog.setOfflineCounter(0);
            nodeCatalog.setLastConnectionTimestamp(new Timestamp(System.currentTimeMillis()));
            nodeCatalog.setTriedToPropagateTimes(0);
            nodeCatalog.setLocation((GeoLocation) nodeProfile.getLocation());
            int nodeCatalogVersion = JPADaoFactory.getNodeCatalogDao().getNodeVersionById(nodeProfile.getIdentityPublicKey());
            nodeCatalog.setVersion(nodeCatalogVersion+1);
            nodeCatalog.setPendingPropagations(NodesCatalogPropagationConfiguration.DESIRED_PROPAGATIONS);

            /*
             * Update NodesCatalog into data base
             */
            JPADaoFactory.getNodeCatalogDao().update(nodeCatalog);

            ConfigurationManager.updateValue(ConfigurationManager.REGISTERED_IN_CATALOG, String.valueOf(Boolean.TRUE));
            ConfigurationManager.updateValue(ConfigurationManager.LAST_REGISTER_NODE_PROFILE, HexadecimalConverter.convertHexString(nodeProfile.toJson().getBytes("UTF-8")));

        } else {

            insertNodeProfileIntoCatalog();

        }
    }

    /**
     * Validate is register in the catalog
     * @return boolean
     */
    private boolean isRegisterInNodeCatalog(boolean isSeedServer){

        HttpURLConnection httpURLConnection = null;

        try {

            if (isSeedServer)
                return JPADaoFactory.getNodeCatalogDao().exist(getIdentity().getPublicKey());

            URL url = new URL("http://" + SeedServerConf.DEFAULT_IP + ":" + SeedServerConf.DEFAULT_PORT + "/fermat/rest/api/v1/nodes/registered/"+getIdentity().getPublicKey());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String respond = reader.readLine();

            if (httpURLConnection.getResponseCode() == 200 && respond != null && respond.contains("success")) {

               /*
                * Decode into a json Object
                */
                JsonParser parser = GsonProvider.getJsonParser();
                JsonObject respondJsonObject = (JsonObject) parser.parse(respond.trim());

                LOG.info(respondJsonObject);

                if (respondJsonObject.get("success").getAsBoolean()){
                    return respondJsonObject.get("isRegistered").getAsBoolean();
                }else {
                    return Boolean.FALSE;
                }

            }else{
                return Boolean.FALSE;
            }

        }catch (Exception e){
            return Boolean.FALSE;
        }finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }

    }

    /**
     * This method clean all data from de check in tables.
     *  - CHECK_IN_CLIENT
     *  - CHECK_IN_NETWORK_SERVICE
     *  - CHECK_IN_ACTORS
     */
    private void cleanSessionTables() throws CantReadRecordDataBaseException, CantDeleteRecordDataBaseException {

        try {

            LOG.info("Deleting older session and his associate entities");
            JPADaoFactory.getClientSessionDao().deleteAll();
            JPADaoFactory.getClientDao().deleteAll();
            JPADaoFactory.getNetworkServiceSessionDao().deleteAll();
            JPADaoFactory.getNetworkServiceDao().deleteAll();
            JPADaoFactory.getActorSessionDao().deleteAll();

        }catch (Exception e){
            LOG.error("Can't Deleting older session and his associate entities: "+e.getMessage());
        }

    }

    public NodeProfile getNodeProfile() {
        return nodeProfile;
    }

    /**
     * Get the identity
     *
     * @return ECCKeyPair
     */
    public ECCKeyPair getIdentity() {
        return identity;
    }
}
