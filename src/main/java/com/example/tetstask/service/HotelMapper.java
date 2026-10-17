package com.example.tetstask.service;

import com.example.tetstask.domain.Address;
import com.example.tetstask.domain.ArrivalTime;
import com.example.tetstask.domain.Contacts;
import com.example.tetstask.domain.Hotel;
import com.example.tetstask.dto.AddressDto;
import com.example.tetstask.dto.ArrivalTimeDto;
import com.example.tetstask.dto.ContactsDto;
import com.example.tetstask.dto.CreateHotelRequest;
import com.example.tetstask.dto.HotelDetailsResponse;
import com.example.tetstask.dto.HotelSummaryResponse;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class HotelMapper {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Hotel toEntity(CreateHotelRequest request) {
        AddressDto address = request.address();
        ContactsDto contacts = request.contacts();
        ArrivalTimeDto arrivalTime = request.arrivalTime();
        return new Hotel(
                request.name().trim(),
                trimToNull(request.description()),
                request.brand().trim(),
                new Address(address.houseNumber(), address.street().trim(), address.city().trim(),
                        address.country().trim(), address.postCode().trim()),
                new Contacts(contacts.phone().trim(), contacts.email().trim()),
                new ArrivalTime(parseTime(arrivalTime.checkIn()), parseTime(arrivalTime.checkOut()))
        );
    }

    public HotelSummaryResponse toSummary(Hotel hotel) {
        Address address = hotel.getAddress();
        String formattedAddress = "%d %s, %s, %s, %s".formatted(
                address.getHouseNumber(), address.getStreet(), address.getCity(),
                address.getPostCode(), address.getCountry()
        );
        return new HotelSummaryResponse(
                hotel.getId(), hotel.getName(), hotel.getDescription(), formattedAddress,
                hotel.getContacts().getPhone()
        );
    }

    public HotelDetailsResponse toDetails(Hotel hotel) {
        Address address = hotel.getAddress();
        Contacts contacts = hotel.getContacts();
        ArrivalTime arrivalTime = hotel.getArrivalTime();
        return new HotelDetailsResponse(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                hotel.getBrand(),
                new AddressDto(address.getHouseNumber(), address.getStreet(), address.getCity(),
                        address.getCountry(), address.getPostCode()),
                new ContactsDto(contacts.getPhone(), contacts.getEmail()),
                new ArrivalTimeDto(formatTime(arrivalTime.getCheckIn()), formatTime(arrivalTime.getCheckOut())),
                hotel.getAmenities()
        );
    }

    private LocalTime parseTime(String value) {
        return value == null ? null : LocalTime.parse(value, TIME_FORMATTER);
    }

    private String formatTime(LocalTime value) {
        return value == null ? null : value.format(TIME_FORMATTER);
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
