package com.example.tetstask.service;

import com.example.tetstask.domain.Hotel;
import com.example.tetstask.dto.CreateHotelRequest;
import com.example.tetstask.dto.HotelDetailsResponse;
import com.example.tetstask.dto.HotelSummaryResponse;
import com.example.tetstask.exception.HotelNotFoundException;
import com.example.tetstask.repository.HotelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public List<HotelSummaryResponse> search(HotelSearchCriteria criteria) {
        return hotelRepository.findAll(HotelSpecifications.matches(criteria), Sort.by("id")).stream()
                .map(hotelMapper::toSummary)
                .toList();
    }

    public Map<String, Long> histogram(String parameter) {
        List<Object[]> rows = switch (HistogramParameter.from(parameter)) {
            case BRAND -> hotelRepository.countByBrand();
            case CITY -> hotelRepository.countByCity();
            case COUNTRY -> hotelRepository.countByCountry();
            case AMENITIES -> hotelRepository.countByAmenity();
        };

        Map<String, Long> histogram = new LinkedHashMap<>();
        rows.forEach(row -> histogram.put((String) row[0], (Long) row[1]));
        return histogram;
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
