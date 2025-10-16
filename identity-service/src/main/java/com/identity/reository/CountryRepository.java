package com.identity.reository;

import com.identity.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  CountryRepository extends JpaRepository<Country, Long> {

    boolean existsByCountryNameIgnoreCase(String countryName);

    Page<Country> findByCountryNameContainingIgnoreCase(String countryName, Pageable pageable);

    Optional<Country> findByCountryNameIgnoreCase(String countryName);

    boolean existsByCountryName(String countryName);
}
