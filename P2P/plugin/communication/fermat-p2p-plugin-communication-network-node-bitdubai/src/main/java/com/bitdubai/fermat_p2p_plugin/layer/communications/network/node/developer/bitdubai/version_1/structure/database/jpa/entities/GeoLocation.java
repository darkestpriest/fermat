package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.jpa.entities;


import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;
import com.bitdubai.fermat_api.layer.osa_android.location_system.LocationSource;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * Created by rrequena on 24/07/16.
 */
@Entity
public class GeoLocation extends AbstractBaseEntity<String> implements Location {

    /**
     * Represent the serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represent the id
     */
    @Id
    @NotNull
    private String id;

    /**
     * Represent the latitude
     */
    @NotNull
    private Double latitude;

    /**
     * Represent the longitude
     */
    @NotNull
    private Double longitude;

    /**
     * Represent the altitude
     */
    private Double altitude;

    /**
     * Represent the accuracy
     */
    private Long accuracy;

    /**
     * Represent the altitudeAccuracy
     */
    private Double altitudeAccuracy;

    /**
     * Represent the time
     */
    private Long time;

    /**
     * Represent the source
     */
    @Transient
    private LocationSource source;

    /**
     * Constructor
     */
    public GeoLocation() {
        this.id = null;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.altitude = 0.0;
        this.accuracy = 0L;
        this.altitudeAccuracy = null;
        this.time = System.currentTimeMillis();
        this.source = null;
    }

    /**
     * Constructor whit parameters
     * @param latitude
     * @param longitude
     */
    public GeoLocation(String idPublicKeyIdentity, Double latitude, Double longitude) {
        this.id = idPublicKeyIdentity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = 0.0;
        this.accuracy = 0L;
        this.altitudeAccuracy = null;
        this.time = System.currentTimeMillis();
        this.source = null;
    }

    /**
     * Constructor whit parameters
     * @param latitude
     * @param longitude
     * @param altitude
     * @param accuracy
     * @param altitudeAccuracy
     * @param time
     * @param source
     */
    public GeoLocation(String idPublicKeyIdentity, Double latitude, Double longitude, Double altitude, Long accuracy, Double altitudeAccuracy, Long time, LocationSource source) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.accuracy = accuracy;
        this.altitudeAccuracy = altitudeAccuracy;
        this.time = time;
        this.source = source;
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@getId()
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
     * Get the value of latitude
     *
     * @return latitude
     */
    @Override
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Set the value of latitude
     *
     * @param latitude
     */
    @Override
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Get the value of longitude
     *
     * @return longitude
     */
    @Override
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Set the value of longitude
     *
     * @param longitude
     */
    @Override
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Get the value of altitude
     *
     * @return altitude
     */
    @Override
    public Double getAltitude() {
        return altitude;
    }

    /**
     * Set the value of altitude
     *
     * @param altitude
     */
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    /**
     * Get the value of accuracy
     *
     * @return accuracy
     */
    @Override
    public long getAccuracy() {
        return accuracy;
    }

    /**
     * Set the value of accuracy
     *
     * @param accuracy
     */
    public void setAccuracy(Long accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * Get the value of altitudeAccuracy
     *
     * @return altitudeAccuracy
     */
    @Override
    public Double getAltitudeAccuracy() {
        return altitudeAccuracy;
    }

    /**
     * Set the value of altitudeAccuracy
     *
     * @param altitudeAccuracy
     */
    public void setAltitudeAccuracy(Double altitudeAccuracy) {
        this.altitudeAccuracy = altitudeAccuracy;
    }

    /**
     * Get the value of time
     *
     * @return time
     */
    @Override
    public Long getTime() {
        return time;
    }

    /**
     * Set the value of time
     *
     * @param time
     */
    public void setTime(Long time) {
        this.time = time;
    }

    /**
     * Get the value of source
     *
     * @return source
     */
    @Override
    public LocationSource getSource() {
        return source;
    }

    /**
     * Set the value of source
     *
     * @param source
     */
    public void setSource(LocationSource source) {
        this.source = source;
    }

    /**
     * (non-javadoc)
     * @see AbstractBaseEntity@equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeoLocation)) return false;

        GeoLocation that = (GeoLocation) o;

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
     * @see Object@toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GeoLocation{");
        sb.append("id=").append(id);
        sb.append(", latitude=").append((latitude != null ? latitude.doubleValue() : null));
        sb.append(", longitude=").append((longitude != null ? longitude.doubleValue() : null));
        sb.append(", altitude=").append((altitude != null ? altitude.doubleValue() : null));
        sb.append(", accuracy=").append((accuracy != null ? accuracy.doubleValue() : null));
        sb.append(", altitudeAccuracy=").append((altitudeAccuracy != null ? altitudeAccuracy.doubleValue() : null));
        sb.append(", time=").append(time);
        sb.append(", source=").append(source);
        sb.append('}');
        return sb.toString();
    }
}
