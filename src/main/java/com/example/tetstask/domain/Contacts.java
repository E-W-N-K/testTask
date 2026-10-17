package com.example.tetstask.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Contacts {

    @Column(nullable = false, length = 64)
    private String phone;

    @Column(nullable = false)
    private String email;

    protected Contacts() {
    }

    public Contacts(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
