package com.identity.service;

import com.identity.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CountryService {
    Page<Country> getAllCountries(String search, Pageable pageable);
}
