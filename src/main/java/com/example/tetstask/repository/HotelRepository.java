package com.example.tetstask.repository;

import com.example.tetstask.domain.Hotel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {

    @Override
    @EntityGraph(attributePaths = "amenities")
    @Query("select h from Hotel h where h.id = :id")
    Optional<Hotel> findById(Long id);

    @EntityGraph(attributePaths = "amenities")
    @Query("select distinct h from Hotel h order by h.id")
    List<Hotel> findAllWithAmenities();

    @Query("select h.brand, count(h) from Hotel h group by h.brand order by h.brand")
    List<Object[]> countByBrand();

    @Query("select h.address.city, count(h) from Hotel h group by h.address.city order by h.address.city")
    List<Object[]> countByCity();

    @Query("select h.address.country, count(h) from Hotel h group by h.address.country order by h.address.country")
    List<Object[]> countByCountry();

    @Query("select amenity, count(h) from Hotel h join h.amenities amenity group by amenity order by amenity")
    List<Object[]> countByAmenity();
}
