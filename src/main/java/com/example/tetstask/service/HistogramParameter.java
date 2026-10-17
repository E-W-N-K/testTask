package com.example.tetstask.service;

import com.example.tetstask.exception.UnsupportedHistogramParameterException;

import java.util.Locale;

public enum HistogramParameter {
    BRAND,
    CITY,
    COUNTRY,
    AMENITIES;

    public static HistogramParameter from(String value) {
        try {
            return valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new UnsupportedHistogramParameterException(value);
        }
    }
}
