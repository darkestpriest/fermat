package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.jpa_test;

import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileStatus;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.DatabaseManager;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.ActorCheckInDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.ClientCheckInDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.NetworkServiceCheckInDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.NodeCatalogDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCheckIn;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.Client;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ClientCheckIn;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.GeoLocation;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkService;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkServiceCheckIn;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantUpdateRecordDataBaseException;
import com.google.common.base.Stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by rrequena on 21/07/16.
 */

public class MainRunner {

    private static final int TOTAL_NODES = 100;

    private static final int TOTAL_CLIENTS = 1;

    private static final int TOTAL_NS = 20;

    private static final int TOTAL_ACTORES = 5;

    public static void main(String[] args) {

        try {

            Stopwatch timer = Stopwatch.createStarted();

            NodeCatalog nodeCatalog = testNodeCatalog();

            ClientCheckIn clientCheckIn = testClientCheckIn();

            NetworkServiceCheckIn networkServiceCheckIn = testNetworkServiceCheckIn(clientCheckIn);

            ActorCheckIn actorCheckIn = testActorCheckIn(clientCheckIn, networkServiceCheckIn, nodeCatalog);

            System.out.println(actorCheckIn.getActor().getActorProfile());

            DatabaseManager.closeDataBase();

            System.out.println("TOTAL TEST TOOK: " + timer.stop());


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static NodeCatalog testNodeCatalog() throws CantReadRecordDataBaseException, CantUpdateRecordDataBaseException, CantInsertRecordDataBaseException {

        System.out.println(" ---------------------------------------------------------------------------------- ");
        System.out.println(" Executing method testNodeCatalog()");

        Stopwatch timer = Stopwatch.createStarted();
        List<NodeCatalog> list = new ArrayList<>();
        NodeCatalogDao dao = new NodeCatalogDao();

        ECCKeyPair eccKeyPair = null;

        for (int i = 0; i < TOTAL_NODES; i++) {

            eccKeyPair = new ECCKeyPair();
            NodeCatalog nodeCatalog = new NodeCatalog();
            nodeCatalog.setId(eccKeyPair.getPublicKey());
            nodeCatalog.setDefaultPort(8080);
            nodeCatalog.setName("Node_" + i);
            nodeCatalog.setIp("10.1.1." + i);
            nodeCatalog.setStatus(ProfileStatus.OFFLINE);
            nodeCatalog.setLocation(new GeoLocation((10.1 + i), (8.9 + i)));

            list.add(nodeCatalog);

        }

        for (NodeCatalog item: list) {
            dao.save(item);
        }

        System.out.println("Last id: " + eccKeyPair.getPublicKey());
        System.out.println("Total entities: " + dao.count());

        NodeCatalog entity = dao.findById(eccKeyPair.getPublicKey());

        System.out.println("Load entity:" +entity);
        System.out.println("Method testNodeCatalog() took: " + timer.stop());

        return entity;

    }


    public static ClientCheckIn testClientCheckIn() throws CantReadRecordDataBaseException, CantUpdateRecordDataBaseException, CantInsertRecordDataBaseException {

        System.out.println(" ---------------------------------------------------------------------------------- ");
        System.out.println(" Executing method testClientCheckIn()");

        Stopwatch timer = Stopwatch.createStarted();
        List<ClientCheckIn> list = new ArrayList<>();
        ClientCheckInDao dao = new ClientCheckInDao();

        String id = null;

        for (int i = 0; i < TOTAL_CLIENTS; i++) {

            id = UUID.randomUUID().toString();
            Client profile = new Client();
            profile.setDeviceType("device " + i);
            profile.setId(new ECCKeyPair().getPublicKey());
            profile.setLocation(new GeoLocation((10.1 + i), (8.9 + i)));
            profile.setStatus(ProfileStatus.ONLINE);

            ClientCheckIn clientCheckIn = new ClientCheckIn();
            clientCheckIn.setClient(profile);
            clientCheckIn.setId(id);

            list.add(clientCheckIn);

        }

        for (ClientCheckIn item: list) {
            dao.save(item);
        }

        System.out.println("Last id: " + id);
        System.out.println("Total entities: " + dao.count());

        ClientCheckIn entity = dao.findById(id);
        System.out.println("Load entity:" +entity);
        System.out.println("Method testClientCheckIn() took: " + timer.stop());

        return entity;

    }


    public static NetworkServiceCheckIn testNetworkServiceCheckIn(ClientCheckIn clientCheckIn) throws CantReadRecordDataBaseException, CantUpdateRecordDataBaseException, CantInsertRecordDataBaseException {

        System.out.println(" ---------------------------------------------------------------------------------- ");
        System.out.println(" Executing method testNetworkServiceCheckIn()");

        Stopwatch timer = Stopwatch.createStarted();
        List<NetworkServiceCheckIn> list = new ArrayList<>();
        NetworkServiceCheckInDao dao = new NetworkServiceCheckInDao();

        String id = null;

        for (int i = 0; i < TOTAL_NS; i++) {

            id = UUID.randomUUID().toString();
            NetworkServiceProfile profile = new NetworkServiceProfile();
            profile.setIdentityPublicKey(new ECCKeyPair().getPublicKey());
            profile.setLocation(new GeoLocation((10.1 + i), (8.9 + i)));
            profile.setStatus(ProfileStatus.ONLINE);
            profile.setClientIdentityPublicKey(clientCheckIn.getClient().getId());
            profile.setNetworkServiceType(NetworkServiceType.NEGOTIATION_TRANSMISSION);

            NetworkService networkService = new NetworkService(profile);

            NetworkServiceCheckIn networkServiceCheckIn = new NetworkServiceCheckIn();
            networkServiceCheckIn.setSessionId(id);
            networkServiceCheckIn.setNetworkService(networkService);

            list.add(networkServiceCheckIn);

        }

        for (NetworkServiceCheckIn item: list) {
            dao.save(item);
        }

        System.out.println("Last id: " + id);
        System.out.println("Total entities: " + dao.count());

        NetworkServiceCheckIn entity = dao.findById(id);
        System.out.println("Load entity:" +entity);
        System.out.println("Method testClientCheckIn() took: " + timer.stop());

        return entity;

    }


    public static ActorCheckIn testActorCheckIn(ClientCheckIn clientCheckIn, NetworkServiceCheckIn networkServiceCheckIn, NodeCatalog nodeCatalog) throws CantReadRecordDataBaseException, CantUpdateRecordDataBaseException, CantInsertRecordDataBaseException {

        System.out.println(" ---------------------------------------------------------------------------------- ");
        System.out.println(" Executing method testActorCheckIn()");

        Stopwatch timer = Stopwatch.createStarted();
        List<ActorCheckIn> list = new ArrayList<>();
        ActorCheckInDao dao = new ActorCheckInDao();

        String id = null;

        for (int i = 0; i < TOTAL_ACTORES; i++) {

            id = UUID.randomUUID().toString();
            ActorProfile profile = new ActorProfile();
            profile.setIdentityPublicKey(new ECCKeyPair().getPublicKey());
            profile.setLocation(new GeoLocation((10.1 + i), (8.9 + i)));
            profile.setStatus(ProfileStatus.UNKNOWN);
            profile.setClientIdentityPublicKey(clientCheckIn.getClient().getId());
            profile.setNsIdentityPublicKey(networkServiceCheckIn.getNetworkService().getId());
            profile.setAlias("Alias-00" + i);
            profile.setName("Name " + i);
            profile.setActorType(Actors.CHAT.getCode());
            profile.setExtraData("content " + i + i);
            profile.setPhoto(("Imagen " + i).getBytes());

            ActorCheckIn actorCheckIn = new ActorCheckIn();
            actorCheckIn.setSessionId(id);

            ActorCatalog actorCatalog = new ActorCatalog(profile, ("Thumbnail " + i).getBytes(), nodeCatalog, actorCheckIn, "");
            actorCheckIn.setActor(actorCatalog);

            list.add(actorCheckIn);

        }

        for (ActorCheckIn item: list) {
            dao.save(item);
        }

        System.out.println("Last id: " + id);
        System.out.println("Total entities: " + dao.count());

        ActorCheckIn entity = dao.findById(id);
        System.out.println("Load entity:" +entity);

        System.out.println("Exist entity " + dao.exist(entity.getId()));
        System.out.println("Method testActorCheckIn() took: " + timer.stop());

        return entity;

    }
}
