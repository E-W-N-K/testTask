package com.example.tetstask.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private String brand;

    @Embedded
    private Address address;

    @Embedded
    private Contacts contacts;

    @Embedded
    private ArrivalTime arrivalTime;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "hotel_amenities", joinColumns = @JoinColumn(name = "hotel_id"))
    @OrderColumn(name = "list_index")
    @Column(name = "amenity", nullable = false)
    private List<String> amenities = new ArrayList<>();

    protected Hotel() {
    }

    public Hotel(String name, String description, String brand, Address address,
                 Contacts contacts, ArrivalTime arrivalTime) {
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.address = address;
        this.contacts = contacts;
        this.arrivalTime = arrivalTime;
    }

    public void addAmenities(List<String> newAmenities) {
        newAmenities.stream()
                .map(String::trim)
                .filter(amenity -> amenities.stream().noneMatch(existing -> existing.equalsIgnoreCase(amenity)))
                .forEach(amenities::add);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public Address getAddress() {
        return address;
    }

    public Contacts getContacts() {
        return contacts;
    }

    public ArrivalTime getArrivalTime() {
        return arrivalTime;
    }

    public List<String> getAmenities() {
        return List.copyOf(amenities);
    }
}
