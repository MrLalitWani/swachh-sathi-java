package com.swachhsathi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sensitive_facility_pois")
public class SensitiveFacilityPOI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FacilityType facilityType;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double weight;

    @Column(length = 255)
    private String address;

    public SensitiveFacilityPOI() {
    }

    public SensitiveFacilityPOI(Long id, String name, FacilityType facilityType, Double latitude, Double longitude, Double weight, String address) {
        this.id = id;
        this.name = name;
        this.facilityType = facilityType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.weight = weight;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FacilityType getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
