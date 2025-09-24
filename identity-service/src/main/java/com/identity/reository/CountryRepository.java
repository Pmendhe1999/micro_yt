package com.identity.reository;

import com.identity.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  CountryRepository extends JpaRepository<Country, Long> {
    Page<Country> findByCountryNameContainingIgnoreCase(String countryName, Pageable pageable);

}
