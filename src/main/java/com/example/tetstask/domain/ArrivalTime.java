package com.example.tetstask.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalTime;

@Embeddable
public class ArrivalTime {

    @Column(name = "check_in", nullable = false)
    private LocalTime checkIn;

    @Column(name = "check_out")
    private LocalTime checkOut;

    protected ArrivalTime() {
    }

    public ArrivalTime(LocalTime checkIn, LocalTime checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public LocalTime getCheckIn() {
        return checkIn;
    }

    public LocalTime getCheckOut() {
        return checkOut;
    }
}
