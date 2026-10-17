package com.example.tetstask.dto;

public record HotelSummaryResponse(
        Long id,
        String name,
        String description,
        String address,
        String phone
) {
}
