package com.example.tetstask.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateHotelRequest(
        @NotBlank String name,
        @Size(max = 2000) String description,
        @NotBlank String brand,
        @NotNull @Valid AddressDto address,
        @NotNull @Valid ContactsDto contacts,
        @NotNull @Valid ArrivalTimeDto arrivalTime
) {
}
