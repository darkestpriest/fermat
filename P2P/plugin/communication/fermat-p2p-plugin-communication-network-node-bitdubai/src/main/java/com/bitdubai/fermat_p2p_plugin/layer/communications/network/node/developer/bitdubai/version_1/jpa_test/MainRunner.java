package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.jpa_test;

import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileStatus;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.JPANamedQuery;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.DatabaseManager;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.ActorCatalogDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.ActorSessionDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.ClientSessionDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.JPADaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.NetworkServiceSessionDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.daos.NodeCatalogDao;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorSession;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.Client;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ClientSession;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.GeoLocation;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkService;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkServiceSession;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantUpdateRecordDataBaseException;
import com.google.common.base.Stopwatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by rrequena on 21/07/16.
 */

public class MainRunner {

    private static final int TOTAL_NODES = 3;

    private static final int TOTAL_CLIENTS = 5;

    private static final int TOTAL_NS = 20;

    private static final int TOTAL_ACTOR = 200;

    public static void main(String[] args) {

        try {

            Stopwatch timer = Stopwatch.createStarted();

            NodeCatalog nodeCatalog = testNodeCatalog();

         //   ClientSession ClientSession = testClientCheckIn(nodeCatalog);

           /*

           040B19757B215891EFEBEBEC7627DCBD35F677CC184227EE6293743C33FC1E9499ED5EBDB5F32414FA9491B5AF6E189C29BB2863A6C77EA49AFF1A2C07294060B9
           049C2CEC069B9B65E6FECE5F36C402BE91F49F7967F0BB2FFAE402067B61F4C4B1F789E63A7D07F159E68EC9D58B0E961A1A50CAE42A8776FD56D5C2C3381ECFCF


           04693E0619F07B31C1C49F7654F246AA75E00776BC341353AB8006E6B24BC8C48BFF754D1AD3E3761571871F21A9AABE6B6F9B6A4F111BE583C037865110AFDF6F
            04E4775845CEE6C4D122C924A0A00C97FD5334FF63335636644C8205A1E2F32A19E8E6A6DA2E52171830D60579B8848430D853C0B31C416E1EF1B48A0BABCE169C */


            System.out.println("exist: " + JPADaoFactory.getActorCatalogDao().exist("04693E0619F07B31C1C49F7654F246AA75E00776BC341353AB8006E6B24BC8C48BFF754D1AD3E3761571871F21A9AABE6B6F9B6A4F111BE583C037865110AFDF6F"));
          //  System.out.println("exist: " + JPADaoFactory.getActorCatalogDao().exist("04D064E309BA90528433FE1D3C81FC2E0F797D065922C8B748CC83FAA06F62DAB8B1601D369DE2FD824BC2EF9320E1017141A890025282C74DEE94357585C141B9"));

            ClientSession clientSession = testClientCheckIn(nodeCatalog);

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
            nodeCatalog.setLocation(new GeoLocation(nodeCatalog.getId(), (10.1 + i), (8.9 + i)));

            list.add(nodeCatalog);

        }

        for (NodeCatalog item: list) {
            dao.save(item);
        }

        System.out.println("Last id: " + eccKeyPair.getPublicKey());
        System.out.println("Total entities: " + dao.count());

        NodeCatalog entity = dao.findById(eccKeyPair.getPublicKey());
        System.out.println("Load NodeCatalog entity:" +entity);
        System.out.println("Method testNodeCatalog() took: " + timer.stop());
        System.out.println(" ---------------------------------------------------------------------------------- ");

        return entity;

    }


    public static ClientSession testClientCheckIn(NodeCatalog nodeCatalog) throws CantReadRecordDataBaseException, CantUpdateRecordDataBaseException, CantInsertRecordDataBaseException {

        System.out.println(" ---------------------------------------------------------------------------------- ");
        System.out.println(" Executing method testClientCheckIn()");

        Stopwatch timer = Stopwatch.createStarted();
        List<ClientSession> list = new ArrayList<>();
        ClientSessionDao dao = new ClientSessionDao();

        ECCKeyPair id = null;
        String sessionId = null;
        ClientSession clientSession = null;

        for (int i = 0; i < TOTAL_CLIENTS; i++) {

            sessionId = UUID.randomUUID().toString();
            id = new ECCKeyPair();
            Client profile = new Client();
            profile.setDeviceType("device " + i);
            profile.setId(id.getPublicKey());
            profile.setLocation(new GeoLocation(profile.getId(), (10.1 + i), (8.9 + i)));
            profile.setStatus(ProfileStatus.ONLINE);

            clientSession = new ClientSession();
            clientSession.setClient(profile);
            clientSession.setId(sessionId);

            list.add(clientSession);

        }

        ActorSession actorCheckIn = null;
        for (ClientSession item: list) {
            dao.save(item);
            NetworkServiceSession networkServiceSession = testNetworkServiceCheckIn(item);
            actorCheckIn = testActorCheckIn(item, networkServiceSession, nodeCatalog);
        }

        System.out.println("Last id: " + clientSession.getId());
        System.out.println("Total ClientSession entities: " + dao.count());

        ClientSession entity = dao.findById(id.getPublicKey());
        System.out.println("Load ClientSession entity:" + clientSession.getId());

        System.out.println("Method testClientCheckIn() took: " + timer.stop());
        System.out.println(" ---------------------------------------------------------------------------------- ");

        speedTest(actorCheckIn);

        return entity;

    }
    public static void speedTest(ActorSession actorCheckIn){
        System.out.println("#######################################################");
        try {
            HashMap<String, Object> filter = new HashMap<>();
            filter.put("type", actorCheckIn.getActor().getActorType());
            Stopwatch timer = Stopwatch.createStarted();
            List<ActorSession> actorCheckIns = JPADaoFactory.getActorSessionDao().executeNamedQuery(JPANamedQuery.GET_ALL_CHECKED_IN_ACTORS_BY_ACTOR_TYPE, filter, false );
           int total = JPADaoFactory.getActorSessionDao().executeNamedQuery(JPANamedQuery.GET_ALL_CHECKED_IN_ACTORS_BY_ACTOR_TYPE, filter, false).size();
//            System.out.println("actorCheckIns = "+actorCheckIns);
            System.out.println("total = " + total);
            System.out.println("Time consumed:" + timer.stop());
            System.out.println("#######################################################");
            filter.clear();
            filter.put("actor.actorType", actorCheckIn.getActor().getActorType());
            timer = Stopwatch.createStarted();
            actorCheckIns = JPADaoFactory.getActorSessionDao().list(filter);
            total = JPADaoFactory.getActorSessionDao().count(filter);
//            System.out.println("actorCheckIns = "+actorCheckIns);
            System.out.println("total = " + total);
            System.out.println("Time consumed:"+timer.stop());
            System.out.println("#######################################################");
        } catch (CantReadRecordDataBaseException e) {
            e.printStackTrace();
        }
    }

    public static NetworkServiceSession testNetworkServiceCheckIn(ClientSession clientSession) throws CantReadRecordDataBaseException, CantUpdateRecordDataBaseException, CantInsertRecordDataBaseException {

        System.out.println(" ---------------------------------------------------------------------------------- ");
        System.out.println(" Executing method testNetworkServiceCheckIn()");

        Stopwatch timer = Stopwatch.createStarted();
        List<NetworkServiceSession> list = new ArrayList<>();
        NetworkServiceSessionDao dao = new NetworkServiceSessionDao();

        ECCKeyPair id = null;
        NetworkServiceSession networkServiceSession = null;

        for (int i = 0; i < TOTAL_NS; i++) {

            id = new ECCKeyPair();
            NetworkServiceProfile profile = new NetworkServiceProfile();
            profile.setIdentityPublicKey(id.getPublicKey());
            profile.setLocation(new GeoLocation(profile.getIdentityPublicKey(), (10.1 + i), (8.9 + i)));
            profile.setStatus(ProfileStatus.ONLINE);
            profile.setClientIdentityPublicKey(clientSession.getClient().getId());
            profile.setNetworkServiceType(NetworkServiceType.NEGOTIATION_TRANSMISSION);

            NetworkService networkService = new NetworkService(profile);

            networkServiceSession = new NetworkServiceSession();
            networkServiceSession.setSessionId(clientSession.getId());
            networkServiceSession.setNetworkService(networkService);

            list.add(networkServiceSession);

        }

        for (NetworkServiceSession item: list) {
            dao.save(item);
        }

        System.out.println("Last id: " + id.getPublicKey());
        System.out.println("Total NetworkServiceSession entities: " + dao.count());


        NetworkServiceSession entity = dao.findById(networkServiceSession.getId());
        System.out.println("Load NetworkServiceSession entity:" +entity);
        System.out.println("Method testClientCheckIn() took: " + timer.stop());
        System.out.println(" ---------------------------------------------------------------------------------- ");

        return entity;

    }


    public static ActorSession testActorCheckIn(ClientSession clientSession, NetworkServiceSession networkServiceSession, NodeCatalog nodeCatalog) throws CantReadRecordDataBaseException, CantUpdateRecordDataBaseException, CantInsertRecordDataBaseException {

        System.out.println(" ---------------------------------------------------------------------------------- ");
        System.out.println(" Executing method testActorCheckIn()");

        Stopwatch timer = Stopwatch.createStarted();
        List<ActorSession> list = new ArrayList<>();
        ActorSessionDao dao = new ActorSessionDao();
        ActorCatalogDao actorCatalogDao = new ActorCatalogDao();

        ECCKeyPair id = null;
        ActorSession actorSession = null;

        for (int i = 0; i < TOTAL_ACTOR; i++) {

            id = new ECCKeyPair();
            ActorProfile profile = new ActorProfile();
            profile.setIdentityPublicKey(id.getPublicKey());
            profile.setLocation(new GeoLocation(profile.getIdentityPublicKey(), (10.1 + i), (8.9 + i)));
            profile.setStatus(ProfileStatus.UNKNOWN);
            profile.setClientIdentityPublicKey(clientSession.getClient().getId());
            profile.setNsIdentityPublicKey(networkServiceSession.getNetworkService().getId());
            profile.setAlias("Alias-00" + i);
            profile.setName("Name " + i);
            if(i == TOTAL_ACTOR-1)
                profile.setActorType(Actors.ART_FAN.getCode());
            else
                profile.setActorType(Actors.CBP_CRYPTO_BROKER.getCode());
            profile.setExtraData("content " + i + i);
            profile.setPhoto(("Imagen " + i).getBytes());

            actorSession = new ActorSession();
            actorSession.setSessionId(clientSession.getId());

            ActorCatalog actorCatalog = new ActorCatalog(profile, ("Thumbnail " + i).getBytes(), nodeCatalog, actorSession, "");
            actorSession.setActor(actorCatalog);

            dao.save(actorSession);

        }

        HashMap<String,Object> filters = new HashMap<>();
        String actorType = null;
        List<ActorSession> actorCheckIns;
        long total;
        if(actorType != null && !actorType.isEmpty()) {
            filters.put("type",actorType);
            actorCheckIns = JPADaoFactory.getActorSessionDao().executeNamedQuery(JPANamedQuery.GET_ALL_CHECKED_IN_ACTORS_BY_ACTOR_TYPE, filters, false);
            filters.clear();
            filters.put("type",actorType);
            total = JPADaoFactory.getActorSessionDao().executeNamedQuery(JPANamedQuery.GET_ALL_CHECKED_IN_ACTORS_BY_ACTOR_TYPE, filters, false).size();
        }else {
            actorCheckIns = JPADaoFactory.getActorSessionDao().executeNamedQuery(JPANamedQuery.GET_ALL_CHECKED_IN_ACTORS, filters,false );
            filters.clear();
            total = JPADaoFactory.getActorSessionDao().executeNamedQuery(JPANamedQuery.GET_ALL_CHECKED_IN_ACTORS, filters,false ).size();

        }
        filters.clear();
        filters.put("id",actorSession.getActor().getId());
     //   List<ActorCatalog> actorCatalogs = JPADaoFactory.getActorCatalogDao().executeNamedQuery(JPANamedQuery.GET_ACTOR_CATALOG_BY_ID,filters);
       // System.out.println("actorCatalogs = " + actorCatalogs);
        System.out.println("actors type"+actorCheckIns);
        System.out.println("total"+total);
        System.out.println("##########################################");
        System.out.println("Last id: " + actorSession.getId());
        System.out.println("Total ActorCheckIn entities: " + dao.count());

        ActorSession entity = dao.findById(actorSession.getId());
      //  System.out.println("Load ActorCheckIn entity:" +entity);

        System.out.println("Exist ActorCheckIn entity " + dao.exist(entity.getId()));
      //  System.out.println("ActorProfile " + entity.getActor().getActorProfile());
        System.out.println("Method testActorCheckIn() took: " + timer.stop());
        System.out.println(" ---------------------------------------------------------------------------------- ");

        return entity;

    }


}
