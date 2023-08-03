package cz.jakvitov.wes.persistence.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class CityId implements Serializable {

    private Double latitude;

    private Double longitude;


    public CityId() {
    }

    public CityId(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "CityId{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
