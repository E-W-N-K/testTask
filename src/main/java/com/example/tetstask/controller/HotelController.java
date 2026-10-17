package com.example.tetstask.controller;

import com.example.tetstask.dto.CreateHotelRequest;
import com.example.tetstask.dto.HotelDetailsResponse;
import com.example.tetstask.dto.HotelSummaryResponse;
import com.example.tetstask.service.HotelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/property-view/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public List<HotelSummaryResponse> findAll() {
        return hotelService.findAll();
    }

    @GetMapping("/{id}")
    public HotelDetailsResponse findById(@PathVariable Long id) {
        return hotelService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HotelSummaryResponse create(@Valid @RequestBody CreateHotelRequest request) {
        return hotelService.create(request);
    }

    @PostMapping("/{id}/amenities")
    public HotelDetailsResponse addAmenities(
            @PathVariable Long id,
            @RequestBody @NotEmpty List<@NotBlank String> amenities
    ) {
        return hotelService.addAmenities(id, amenities);
    }
}
