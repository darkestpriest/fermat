/*
 * @#Client.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;


import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileStatus;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ClientProfile;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * The interface <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.Client</code> is
 * represent the session of a clientProfile into the node
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 24/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@Entity
public class Client extends AbstractBaseEntity<String>{

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

    @NotNull
    @OneToOne @MapsId
    private ClientSession clientSession;

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
     * Represent the deviceType
     */
    private String deviceType;

    /**
     * Represent the session
     */
    @OneToOne (targetEntity = ClientSession.class, mappedBy="client")
    private ClientSession session;

    /**
     * Constructor
     */
    public Client() {
        super();
        this.id = "";
        this.location = null;
        this.status = null;
        this.deviceType = "";
        this.clientSession = null;
    }

    /**
     * Constructor with parameter
     * @param clientIdentityPublicKey
     */
    public Client(String clientIdentityPublicKey) {
        super();
        this.id = clientIdentityPublicKey;
        this.status = null;
        this.deviceType = null;
        this.location = null;
    }

    /**
     * Constructor with parameter
     * @param clientProfile
     */
    public Client(ClientProfile clientProfile) {
        super();
        this.id = clientProfile.getIdentityPublicKey();
        this.status = clientProfile.getStatus();
        this.deviceType = clientProfile.getDeviceType();

        if (clientProfile.getLocation() != null){
            this.location = new GeoLocation(this.id, clientProfile.getLocation().getLatitude(), clientProfile.getLocation().getLongitude());
        }else {
            this.location = null;
        }

    }

    /**
     * Constructor with parameters
     * @param id
     * @param location
     * @param status
     * @param deviceType
     */
    public Client(String id, GeoLocation location, ProfileStatus status, String deviceType) {
        super();
        this.id = id;
        this.status = status;
        this.deviceType = deviceType;

        if (location != null){
            this.location = new GeoLocation(this.id, location.getLatitude(), location.getLongitude());
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
     * Get the value of deviceType
     *
     * @return deviceType
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * Set the value of deviceType
     *
     * @param deviceType
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * Get the Session value
     *
     * @return Session
     */
    public ClientSession getSession() {
        return session;
    }

    /**
     * Set the value of session
     *
     * @param session
     */
    public void setSession(ClientSession session) {
        this.session = session;
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;

        Client that = (Client) o;

        return getId().equals(that.getId());

    }

    public ClientSession getClientSession() {
        return clientSession;
    }

    public void setClientSession(ClientSession clientSession) {
        this.clientSession = clientSession;
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
        final StringBuilder sb = new StringBuilder("Client{");
        sb.append("id='").append(id).append('\'');
        sb.append(", location=").append(location);
        sb.append(", status=").append(status);
        sb.append(", deviceType='").append(deviceType).append('\'');
        sb.append(", clientSession='").append(clientSession).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Get the ClientProfile representation
     * @return ClientProfile
     */
    public ClientProfile getClientProfile(){

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setIdentityPublicKey(this.getId());
        clientProfile.setDeviceType(this.getDeviceType());
        clientProfile.setStatus(this.getStatus());
        clientProfile.setLocation(this.getLocation());

        return clientProfile;
    }
}
