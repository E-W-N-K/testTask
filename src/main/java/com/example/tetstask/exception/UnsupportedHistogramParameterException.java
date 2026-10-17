package com.example.tetstask.exception;

public class UnsupportedHistogramParameterException extends RuntimeException {

    public UnsupportedHistogramParameterException(String parameter) {
        super("Unsupported histogram parameter '%s'. Allowed values: brand, city, country, amenities"
                .formatted(parameter));
    }
}
