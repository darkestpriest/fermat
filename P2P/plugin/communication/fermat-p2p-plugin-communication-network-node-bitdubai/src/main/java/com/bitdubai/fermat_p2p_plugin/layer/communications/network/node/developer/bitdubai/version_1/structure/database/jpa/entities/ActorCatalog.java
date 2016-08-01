/*
 * @#ActorCatalog.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;


import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileStatus;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.ActorCatalogUpdateTypes;

import java.sql.Timestamp;
import java.util.Arrays;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * The class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.ActorCatalog</code>
 * represent the data of the actor into the catalog
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@Entity
@NamedQueries({
    @NamedQuery(name="ActorCatalog.getActorCatalogById",        query = "SELECT a FROM ActorCatalog a WHERE a.id = :id"),
    @NamedQuery(name="ActorCatalog.getActorCatalogByActorType", query = "SELECT a FROM ActorCatalog a WHERE a.actorType = :type"),
    @NamedQuery(name="ActorCatalog.getActorCatalog",            query = "SELECT a FROM ActorCatalog a")
})
public class ActorCatalog extends AbstractBaseEntity<String>{

    /**
     * Represent the serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represent the Identity public key
     */
    @Id
    @NotNull
    private String id;

    /**
     * Represent the location
     */
    @MapsId
    @OneToOne(cascade = {CascadeType.ALL}, targetEntity = GeoLocation.class)
    private GeoLocation location;

    /**
     * Represent the status of the profile
     */
    @Enumerated(EnumType.STRING)
    private ProfileStatus status;

    /**
     * Represent the actorType
     */
    @NotNull
    private String actorType;

    /**
     * Represent the alias
     */
    private String alias;

    /**
     * Represent the extraData
     */
    private String extraData;

    /**
     * Represent the name
     */
    @NotNull
    private String name;

    /**
     * Represent the photo
     */
    @Lob
    @Basic(fetch= FetchType.LAZY)
    private byte[] photo;

    /**
     * Represent the hostedTimestamp
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp hostedTimestamp;

    /**
     * Represent the lastUpdateTime
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp lastUpdateTime;

    /**
     * Represent the lastConnection
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp lastConnection;

    /**
     * Represent the thumbnail
     */
    @Lob
    @Basic(fetch= FetchType.EAGER)
    private byte[] thumbnail;

    /**
     * Represent the homeNode
     */
    @ManyToOne @MapsId
    private NodeCatalog homeNode;

    /**
     * Represent the session
     */
    @OneToOne (targetEntity = ActorSession.class, mappedBy="actor")
    private ActorSession session;

    /**
     * Represent the networkService
     */
    @NotNull
    @ManyToOne @MapsId
    private NetworkService networkService;

    /**
     * Represent the clientIdentityPublicKey
     */
    @NotNull
    @ManyToOne @MapsId
    private Client client;

    /**
     * Represent the signature
     */
    private String signature;

    /**
     * Represents the version
     */
    private Integer version;

    /**
     * Represents the version
     */
    private ActorCatalogUpdateTypes lastUpdateType;

    /**
     * Represents the pendingPropagations
     */
    private Integer pendingPropagations;

    /**
     * Represents the triedToPropagateTimes
     */
    private Integer triedToPropagateTimes;

    /**
     * Constructor
     */
    public ActorCatalog(){
        super();
        this.hostedTimestamp = new Timestamp(System.currentTimeMillis());
        this.lastUpdateTime = new Timestamp(System.currentTimeMillis());
        this.lastConnection = new Timestamp(System.currentTimeMillis());
        this.thumbnail = null;
        this.homeNode = null;
        this.session = null;
        this.signature = "";
    }

    /**
     * Constructor with parameter
     * @param actorProfile
     */
    public ActorCatalog(ActorProfile actorProfile) {
        super();
        this.id = actorProfile.getIdentityPublicKey();
        this.name = actorProfile.getName();
        this.alias = actorProfile.getAlias();
        this.client = new Client(actorProfile.getClientIdentityPublicKey());
        this.networkService = new NetworkService(actorProfile.getNsIdentityPublicKey());
        this.networkService.setClient(client);
        this.extraData = actorProfile.getExtraData();
        this.photo = actorProfile.getPhoto();
        this.actorType = actorProfile.getActorType();
        this.status = actorProfile.getStatus();
        this.hostedTimestamp = new Timestamp(System.currentTimeMillis());
        this.lastUpdateTime = new Timestamp(System.currentTimeMillis());
        this.lastConnection = new Timestamp(System.currentTimeMillis());
        this.thumbnail = null;
        this.homeNode = null;
        this.session = null;
        this.signature = "";

        if (actorProfile.getLocation() != null){
            this.location = new GeoLocation(this.id, actorProfile.getLocation().getLatitude(), actorProfile.getLocation().getLongitude());
        }else {
            this.location = null;
        }

    }


    /**
     * Constructor with parameters
     * @param actorProfile
     * @param thumbnail
     * @param homeNode
     * @param signature
     */
    public ActorCatalog(final ActorProfile actorProfile, final byte[] thumbnail, final NodeCatalog homeNode, final String signature) {
        super();
        this.id = actorProfile.getIdentityPublicKey();
        this.name = actorProfile.getName();
        this.alias = actorProfile.getAlias();
        this.client = new Client(actorProfile.getClientIdentityPublicKey());
        this.networkService = new NetworkService(actorProfile.getNsIdentityPublicKey());
        this.networkService.setClient(client);
        this.extraData = actorProfile.getExtraData();
        this.photo = actorProfile.getPhoto();
        this.actorType = actorProfile.getActorType();
        this.status = actorProfile.getStatus();
        this.hostedTimestamp = new Timestamp(System.currentTimeMillis());
        this.lastUpdateTime = new Timestamp(System.currentTimeMillis());
        this.lastConnection = new Timestamp(System.currentTimeMillis());
        this.thumbnail = thumbnail;
        this.homeNode = homeNode;
        this.session = null;
        this.signature = signature;

        if (actorProfile.getLocation() != null){
            this.location = new GeoLocation(this.id, actorProfile.getLocation().getLatitude(), actorProfile.getLocation().getLongitude());
        }else {
            this.location = null;
        }

    }

    /**
     * Constructor with parameters
     * @param actorProfile
     * @param thumbnail
     * @param homeNode
     * @param session
     * @param signature
     */
    public ActorCatalog(ActorProfile actorProfile, byte[] thumbnail, NodeCatalog homeNode, ActorSession session, String signature) {
        super();
        this.id = actorProfile.getIdentityPublicKey();
        this.name = actorProfile.getName();
        this.alias = actorProfile.getAlias();
        this.networkService = new NetworkService(actorProfile.getNsIdentityPublicKey());
        this.extraData = actorProfile.getExtraData();
        this.photo = actorProfile.getPhoto();
        this.actorType = actorProfile.getActorType();
        this.client = new Client(actorProfile.getClientIdentityPublicKey());
        this.status = actorProfile.getStatus();
        this.hostedTimestamp = new Timestamp(System.currentTimeMillis());
        this.lastUpdateTime = new Timestamp(System.currentTimeMillis());
        this.lastConnection = new Timestamp(System.currentTimeMillis());
        this.thumbnail = thumbnail;
        this.homeNode = homeNode;
        this.session = session;
        this.signature = signature;

        if (actorProfile.getLocation() != null){
            this.location = new GeoLocation(this.id, actorProfile.getLocation().getLatitude(), actorProfile.getLocation().getLongitude());
        }else {
            this.location = null;
        }

    }

    /**
     * Get the value of id
     *
     * @return id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the value of location
     *
     * @return location
     */
    public GeoLocation getLocation() {
        return location;
    }

    /**
     * Set the value of location
     *
     * @param location
     */
    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    /**
     * Get the value of status
     *
     * @return status
     */
    public ProfileStatus getStatus() {
        return status;
    }

    /**
     * Set the value of status
     *
     * @param status
     */
    public void setStatus(ProfileStatus status) {
        this.status = status;
    }

    /**
     * Get the value of actorType
     *
     * @return actorType
     */
    public String getActorType() {
        return actorType;
    }

    /**
     * Set the value of actorType
     *
     * @param actorType
     */
    public void setActorType(String actorType) {
        this.actorType = actorType;
    }

    /**
     * Get the value of alias
     *
     * @return alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Set the value of alias
     *
     * @param alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Get the value of extraData
     *
     * @return extraData
     */
    public String getExtraData() {
        return extraData;
    }

    /**
     * Set the value of extraData
     *
     * @param extraData
     */
    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    /**
     * Get the value of name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value of photo
     *
     * @return photo
     */
    public byte[] getPhoto() {
        return photo;
    }

    /**
     * Set the value of photo
     *
     * @param photo
     */
    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    /**
     * Get the value of hostedTimestamp
     *
     * @return hostedTimestamp
     */
    public Timestamp getHostedTimestamp() {
        return hostedTimestamp;
    }

    /**
     * Set the value of hostedTimestamp
     *
     * @param hostedTimestamp
     */
    public void setHostedTimestamp(Timestamp hostedTimestamp) {
        this.hostedTimestamp = hostedTimestamp;
    }

    /**
     * Get the value of lastUpdateTime
     *
     * @return lastUpdateTime
     */
    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * Set the value of lastUpdateTime
     *
     * @param lastUpdateTime
     */
    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * Get the value of lastConnection
     *
     * @return lastConnection
     */
    public Timestamp getLastConnection() {
        return lastConnection;
    }

    /**
     * Set the value of lastConnection
     *
     * @param lastConnection
     */
    public void setLastConnection(Timestamp lastConnection) {
        this.lastConnection = lastConnection;
    }

    /**
     * Get the value of thumbnail
     *
     * @return thumbnail
     */
    public byte[] getThumbnail() {
        return thumbnail;
    }

    /**
     * Set the value of thumbnail
     *
     * @param thumbnail
     */
    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * Get the value of homeNode
     *
     * @return homeNode
     */
    public NodeCatalog getHomeNode() {
        return homeNode;
    }

    /**
     * Set the value of homeNode
     *
     * @param homeNode
     */
    public void setHomeNode(NodeCatalog homeNode) {
        this.homeNode = homeNode;
    }

    /**
     * Get the value of session
     *
     * @return session
     */
    public ActorSession getSession() {
        return session;
    }

    /**
     * Set the value of session
     *
     * @param session
     */
    public void setSession(ActorSession session) {
        this.session = session;
    }

    /**
     * Get the value of networkService
     *
     * @return networkService
     */
    public NetworkService getNetworkService() {
        return networkService;
    }

    /**
     * Set the value of networkService
     *
     * @param networkService
     */
    public void setNetworkService(NetworkService networkService) {
        this.networkService = networkService;
    }

    /**
     * Get the value of client
     *
     * @return client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Set the value of client
     *
     * @param client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Get the value of signature
     *
     * @return signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Set the value of signature
     *
     * @param signature
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * Get the Version value
     *
     * @return Version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Set the value of version
     *
     * @param version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Get the LastUpdateType value
     *
     * @return LastUpdateType
     */
    public ActorCatalogUpdateTypes getLastUpdateType() {
        return lastUpdateType;
    }

    /**
     * Set the value of lastUpdateType
     *
     * @param lastUpdateType
     */
    public void setLastUpdateType(ActorCatalogUpdateTypes lastUpdateType) {
        this.lastUpdateType = lastUpdateType;
    }

    /**
     * Get the PendingPropagations value
     *
     * @return PendingPropagations
     */
    public Integer getPendingPropagations() {
        return pendingPropagations;
    }

    /**
     * Set the value of pendingPropagations
     *
     * @param pendingPropagations
     */
    public void setPendingPropagations(Integer pendingPropagations) {
        this.pendingPropagations = pendingPropagations;
    }

    /**
     * Get the TriedToPropagateTimes value
     *
     * @return TriedToPropagateTimes
     */
    public Integer getTriedToPropagateTimes() {
        return triedToPropagateTimes;
    }

    /**
     * Set the value of triedToPropagateTimes
     *
     * @param triedToPropagateTimes
     */
    public void setTriedToPropagateTimes(Integer triedToPropagateTimes) {
        this.triedToPropagateTimes = triedToPropagateTimes;
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActorCatalog)) return false;

        ActorCatalog that = (ActorCatalog) o;

        return getId().equals(that.getId());

    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@hashCode()
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * (non-javadoc)
     *
     * @see AbstractBaseEntity@toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ActorCatalog{");
        sb.append("id='").append(id).append('\'');
        sb.append(", location=").append((location != null ? location.getId() : null));
        sb.append(", status=").append(status);
        sb.append(", actorType='").append(actorType).append('\'');
        sb.append(", alias='").append(alias).append('\'');
        sb.append(", extraData='").append(extraData).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", photo=").append(Arrays.toString(photo));
        sb.append(", hostedTimestamp=").append(hostedTimestamp);
        sb.append(", lastUpdateTime=").append(lastUpdateTime);
        sb.append(", lastConnection=").append(lastConnection);
        sb.append(", thumbnail=").append(Arrays.toString(thumbnail));
        sb.append(", homeNode=").append((homeNode != null ? homeNode.getId() : null));
        sb.append(", session=").append((session != null ? session.getId() : null));
        sb.append(", networkService=").append((networkService != null ? networkService.getId() : null));
        sb.append(", client=").append((client != null ? client.getId() : null));
        sb.append(", signature='").append(signature).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Get the ActorProfile representation
     * @return actorProfile
     */
    public ActorProfile getActorProfile(){

        ActorProfile actorProfile = new ActorProfile();
        actorProfile.setIdentityPublicKey(this.getId());
        actorProfile.setAlias(this.getAlias());
        actorProfile.setName(this.getName());
        actorProfile.setActorType(this.getActorType());
        actorProfile.setPhoto(this.getPhoto());
        actorProfile.setPhoto(this.getThumbnail());
        actorProfile.setExtraData(this.getExtraData());
        actorProfile.setLocation(this.getLocation());
        actorProfile.setNsIdentityPublicKey(this.getNetworkService().getId());
        actorProfile.setClientIdentityPublicKey(this.getClient().getId());

        if (session != null){
            actorProfile.setStatus(ProfileStatus.ONLINE);
        }else {
            actorProfile.setStatus(ProfileStatus.UNKNOWN);
        }

        return actorProfile;
    }


}