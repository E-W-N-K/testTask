package com.example.tetstask.exception;

public class HotelNotFoundException extends RuntimeException {

    public HotelNotFoundException(Long id) {
        super("Hotel with id %d was not found".formatted(id));
    }
}
