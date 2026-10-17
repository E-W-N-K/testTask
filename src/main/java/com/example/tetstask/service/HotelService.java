package com.example.tetstask.service;

import com.example.tetstask.domain.Hotel;
import com.example.tetstask.dto.CreateHotelRequest;
import com.example.tetstask.dto.HotelDetailsResponse;
import com.example.tetstask.dto.HotelSummaryResponse;
import com.example.tetstask.exception.HotelNotFoundException;
import com.example.tetstask.repository.HotelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    public HotelService(HotelRepository hotelRepository, HotelMapper hotelMapper) {
        this.hotelRepository = hotelRepository;
        this.hotelMapper = hotelMapper;
    }

    public List<HotelSummaryResponse> findAll() {
        return hotelRepository.findAllWithAmenities().stream().map(hotelMapper::toSummary).toList();
    }

    public HotelDetailsResponse findById(Long id) {
        return hotelMapper.toDetails(getHotel(id));
    }

    @Transactional
    public HotelSummaryResponse create(CreateHotelRequest request) {
        Hotel hotel = hotelRepository.save(hotelMapper.toEntity(request));
        return hotelMapper.toSummary(hotel);
    }

    @Transactional
    public HotelDetailsResponse addAmenities(Long id, List<String> amenities) {
        Hotel hotel = getHotel(id);
        hotel.addAmenities(amenities);
        return hotelMapper.toDetails(hotel);
    }

    private Hotel getHotel(Long id) {
        return hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException(id));
    }
}
