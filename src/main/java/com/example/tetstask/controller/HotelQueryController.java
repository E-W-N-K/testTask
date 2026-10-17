package com.example.tetstask.controller;

import com.example.tetstask.dto.HotelSummaryResponse;
import com.example.tetstask.service.HotelSearchCriteria;
import com.example.tetstask.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/property-view")
@Tag(name = "Hotel queries", description = "Hotel search and aggregation")
public class HotelQueryController {

    private final HotelService hotelService;

    public HotelQueryController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/search")
    @Operation(summary = "Search hotels", description = "All supplied filters are combined; matching is case-insensitive")
    public List<HotelSummaryResponse> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String amenities
    ) {
        return hotelService.search(new HotelSearchCriteria(name, brand, city, country, amenities));
    }

    @GetMapping("/histogram/{param}")
    @Operation(summary = "Build a hotel histogram")
    public Map<String, Long> histogram(
            @Parameter(description = "One of: brand, city, country, amenities", example = "city")
            @PathVariable String param
    ) {
        return hotelService.histogram(param);
    }
}
