package com.identity.service;

import com.identity.dto.CountryDTO;
import com.identity.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CountryService {
    Country save(CountryDTO dto, String token);

    Page<Country> getAll(String search, Pageable pageable);

    Optional<Country> getById(Long id);

    Country update(Long id, CountryDTO dto, String token);

    Country delete(Long id, String token);

    Country patchCountry(Long id, String key, Object value);

    List<Country> uploadCountriesFromExcel(MultipartFile file, String token);

}
