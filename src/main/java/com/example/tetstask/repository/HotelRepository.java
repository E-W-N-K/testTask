package com.example.tetstask.repository;

import com.example.tetstask.domain.Hotel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Override
    @EntityGraph(attributePaths = "amenities")
    @Query("select h from Hotel h where h.id = :id")
    Optional<Hotel> findById(Long id);

    @EntityGraph(attributePaths = "amenities")
    @Query("select distinct h from Hotel h order by h.id")
    List<Hotel> findAllWithAmenities();
}
