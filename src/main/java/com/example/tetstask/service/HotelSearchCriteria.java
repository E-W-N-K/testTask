package com.example.tetstask.service;

public record HotelSearchCriteria(
        String name,
        String brand,
        String city,
        String country,
        String amenities
) {
}
