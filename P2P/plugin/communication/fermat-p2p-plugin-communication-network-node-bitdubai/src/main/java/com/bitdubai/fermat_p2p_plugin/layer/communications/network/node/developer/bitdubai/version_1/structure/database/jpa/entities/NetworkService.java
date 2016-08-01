/*
 * @#NetworkService.java - 2016
 * Copyright Fermat.org, All rights reserved.
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;


import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileStatus;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * The interface <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities.NetworkService</code> is
 * represent the session of a clientProfile into the node
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 23/07/16
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@Entity
public class NetworkService extends AbstractBaseEntity<String>{

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
     * Represent the networkServiceType
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private NetworkServiceType networkServiceType;

    /**
     * Represent the client
     */
    @NotNull
    @ManyToOne @MapsId
    private Client client;

    /**
     * Represent the session
     */
    @OneToOne (targetEntity = NetworkServiceSession.class, mappedBy="networkService")
    private NetworkServiceSession session;

    /**
     * Constructor with parameter
     * @param nsIdentityPublicKey
     */
    public NetworkService(String nsIdentityPublicKey) {
        this.id = nsIdentityPublicKey;
        this.location = null;
        this.status = null;
        this.networkServiceType = null;
        this.client = null;
        this.location = null;
    }

    /**
     * Constructor with parameter
     * @param networkServiceProfile
     */
    public NetworkService(NetworkServiceProfile networkServiceProfile) {
        this.id = networkServiceProfile.getIdentityPublicKey();
        this.status = networkServiceProfile.getStatus();
        this.networkServiceType = networkServiceProfile.getNetworkServiceType();
        this.client = new Client(networkServiceProfile.getClientIdentityPublicKey());
        if (networkServiceProfile.getLocation() != null){
            this.location = new GeoLocation(this.id, networkServiceProfile.getLocation().getLatitude(), networkServiceProfile.getLocation().getLongitude());
        }else {
            this.location = null;
        }
    }

    /**
     * Constructor with parameters
     * @param location
     * @param status
     * @param networkServiceType
     * @param clientIdentityPublicKey
     */
    public NetworkService(GeoLocation location, ProfileStatus status, NetworkServiceType networkServiceType, String clientIdentityPublicKey) {
        this.location = location;
        this.status = status;
        this.networkServiceType = networkServiceType;
        this.client = new Client(clientIdentityPublicKey);
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
     * Get the value of networkServiceType
     *
     * @return networkServiceType
     */
    public NetworkServiceType getNetworkServiceType() {
        return networkServiceType;
    }

    /**
     * Set the value of networkServiceType
     *
     * @param networkServiceType
     */
    public void setNetworkServiceType(NetworkServiceType networkServiceType) {
        this.networkServiceType = networkServiceType;
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
     * Get the Session value
     *
     * @return Session
     */
    public NetworkServiceSession getSession() {
        return session;
    }

    /**
     * Set the value of session
     *
     * @param session
     */
    public void setSession(NetworkServiceSession session) {
        this.session = session;
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NetworkService)) return false;

        NetworkService that = (NetworkService) o;

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
        final StringBuilder sb = new StringBuilder("NetworkService{");
        sb.append("id='").append(id).append('\'');
        sb.append(", location=").append(location);
        sb.append(", status=").append(status);
        sb.append(", networkServiceType=").append(networkServiceType);
        sb.append(", client=").append(client);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Get the NetworkServiceProfile representation
     * @return NetworkServiceProfile
     */
    public NetworkServiceProfile getNetworkServiceProfile(){

        NetworkServiceProfile networkServiceProfile = new NetworkServiceProfile();
        networkServiceProfile.setIdentityPublicKey(this.getId());
        networkServiceProfile.setNetworkServiceType(this.getNetworkServiceType());
        networkServiceProfile.setStatus(this.getStatus());
        networkServiceProfile.setLocation(this.getLocation());
        networkServiceProfile.setClientIdentityPublicKey(this.getClient().getId());

        return networkServiceProfile;
    }
}
