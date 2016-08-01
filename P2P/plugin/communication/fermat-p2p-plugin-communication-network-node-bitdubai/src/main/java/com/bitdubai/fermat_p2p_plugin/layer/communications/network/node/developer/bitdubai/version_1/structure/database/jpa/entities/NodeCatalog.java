/*
 * @#NodeCatalog.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;


import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileStatus;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NodeProfile;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.catalog_propagation.nodes.NodesCatalogPropagationConfiguration;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * The class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NodeCatalog</code>
 * represent the data of the node into the catalog
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@Entity
public class NodeCatalog extends AbstractBaseEntity<String>{

    /**
     * Represents the serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represents the Identity public key
     */
    @Id
    @NotNull
    private String id;

    /**
     * Represents the location
     */
    @MapsId
    @OneToOne(cascade = {CascadeType.ALL}, targetEntity = GeoLocation.class)
    private GeoLocation location;

    /**
     * Represents the status of the profile
     */
    @Enumerated(EnumType.STRING)
    private ProfileStatus status;

    /**
     * Represents the defaultPort
     */
    private Integer defaultPort;

    /**
     * Represents the ip
     */
    private String ip;

    /**
     * Represents the name
     */
    private String name;

    /**
     * Represents the lastConnectionTimestamp
     */
    private Timestamp lastConnectionTimestamp;

    /**
     * Represents the lateNotificationsCounter
     */
    private Integer lateNotificationsCounter;

    /**
     * Represents the offlineCounter
     */
    private Integer offlineCounter;

    /**
     * Represents the registeredTimestamp
     */
    private Timestamp registeredTimestamp;

    /**
     * Represents the signature
     */
    private String signature;

    /**
     * Represents the version
     */
    private Integer version;

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
    public NodeCatalog() {
        super();
        this.id = null;
        this.lastConnectionTimestamp = new Timestamp(System.currentTimeMillis());
        this.lateNotificationsCounter = 0;
        this.offlineCounter = 0;
        this.registeredTimestamp = new Timestamp(System.currentTimeMillis());
        this.signature = "";
        this.version = 0;
        this.pendingPropagations = NodesCatalogPropagationConfiguration.DESIRED_PROPAGATIONS;
        this.triedToPropagateTimes = 0;
    }

    /**
     * Constructor with parameter
     * @param id
     */
    public NodeCatalog(String id) {
        super();
        this.id = id;
        this.lastConnectionTimestamp = new Timestamp(System.currentTimeMillis());
        this.lateNotificationsCounter = 0;
        this.offlineCounter = 0;
        this.registeredTimestamp = new Timestamp(System.currentTimeMillis());
        this.signature = "";
        this.version = 0;
        this.pendingPropagations = NodesCatalogPropagationConfiguration.DESIRED_PROPAGATIONS;
        this.triedToPropagateTimes = 0;
        this.location = null;
    }

    /**
     * Constructor with parameters
     * @param nodeProfile
     */
    public NodeCatalog(NodeProfile nodeProfile) {
        super();
        this.id = nodeProfile.getIdentityPublicKey();
        this.ip = nodeProfile.getIp();
        this.name = nodeProfile.getName();
        this.defaultPort = (nodeProfile.getDefaultPort());
        this.status = nodeProfile.getStatus();
        this.lastConnectionTimestamp = new Timestamp(System.currentTimeMillis());
        this.lateNotificationsCounter = 0;
        this.offlineCounter = 0;
        this.registeredTimestamp = new Timestamp(System.currentTimeMillis());
        this.signature = "";
        this.version = 0;

        if (nodeProfile.getLocation() != null){
            this.location = new GeoLocation(this.id, nodeProfile.getLocation().getLatitude(), nodeProfile.getLocation().getLongitude());
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
     * Get the value of defaultPort
     *
     * @return defaultPort
     */
    public Integer getDefaultPort() {
        return defaultPort;
    }

    /**
     * Set the value of defaultPort
     *
     * @param defaultPort
     */
    public void setDefaultPort(Integer defaultPort) {
        this.defaultPort = defaultPort;
    }

    /**
     * Get the value of ip
     *
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * Set the value of ip
     *
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
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
     * Get the value of lastConnectionTimestamp
     *
     * @return lastConnectionTimestamp
     */
    public Timestamp getLastConnectionTimestamp() {
        return lastConnectionTimestamp;
    }

    /**
     * Set the value of lastConnectionTimestamp
     *
     * @param lastConnectionTimestamp
     */
    public void setLastConnectionTimestamp(Timestamp lastConnectionTimestamp) {
        this.lastConnectionTimestamp = lastConnectionTimestamp;
    }

    /**
     * Get the value of lateNotificationsCounter
     *
     * @return lateNotificationsCounter
     */
    public Integer getLateNotificationsCounter() {
        return lateNotificationsCounter;
    }

    /**
     * Set the value of lateNotificationsCounter
     *
     * @param lateNotificationsCounter
     */
    public void setLateNotificationsCounter(Integer lateNotificationsCounter) {
        this.lateNotificationsCounter = lateNotificationsCounter;
    }

    /**
     * Get the value of offlineCounter
     *
     * @return offlineCounter
     */
    public Integer getOfflineCounter() {
        return offlineCounter;
    }

    /**
     * Set the value of offlineCounter
     *
     * @param offlineCounter
     */
    public void setOfflineCounter(Integer offlineCounter) {
        this.offlineCounter = offlineCounter;
    }

    /**
     * Get the value of registeredTimestamp
     *
     * @return registeredTimestamp
     */
    public Timestamp getRegisteredTimestamp() {
        return registeredTimestamp;
    }

    /**
     * Set the value of registeredTimestamp
     *
     * @param registeredTimestamp
     */
    public void setRegisteredTimestamp(Timestamp registeredTimestamp) {
        this.registeredTimestamp = registeredTimestamp;
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
     * Get the value of version
     *
     * @return version
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
     * @see AbstractBaseEntity@hashCode()
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeCatalog)) return false;

        NodeCatalog that = (NodeCatalog) o;

        return getId().equals(that.getId());

    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@toString()
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Get the ClientProfile representation
     * @return ClientProfile
     */
    @Override
    public String toString() {
        return "NodeCatalog{" +
                "id='" + id + '\'' +
                ", location=" + location +
                ", status=" + status +
                ", defaultPort=" + defaultPort +
                ", ip='" + ip + '\'' +
                ", name='" + name + '\'' +
                ", lastConnectionTimestamp=" + lastConnectionTimestamp +
                ", lateNotificationsCounter=" + lateNotificationsCounter +
                ", offlineCounter=" + offlineCounter +
                ", registeredTimestamp=" + registeredTimestamp +
                ", signature='" + signature + '\'' +
                ", version=" + version +
                ", pendingPropagations=" + pendingPropagations +
                ", triedToPropagateTimes=" + triedToPropagateTimes +
                "} ";
    }

    /**
     * Get the NodeProfile representation
     * @return nodeProfile
     */
    public NodeProfile getNodeProfile(){
        NodeProfile nodeProfile = new NodeProfile();
        nodeProfile.setIdentityPublicKey(getId());
        nodeProfile.setName(getName());
        nodeProfile.setDefaultPort(getDefaultPort());
        nodeProfile.setLocation(getLocation());
        nodeProfile.setStatus(getStatus());
        return nodeProfile;
    }

}
