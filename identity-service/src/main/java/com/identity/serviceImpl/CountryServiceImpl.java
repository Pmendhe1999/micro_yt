package com.identity.serviceImpl;

import com.identity.controller.ApplicationController;
import com.identity.entity.Country;
import com.identity.reository.CountryRepository;
import com.identity.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl  implements CountryService {

    @Autowired
    private  CountryRepository repository;

    private static final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);


    @Override
    public Page<Country> getAllCountries(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                log.debug("Fetching Countries with search filter: {}", search);
                return repository.findByCountryNameContainingIgnoreCase(search, pageable);
            }
            log.debug("Fetching all Countries without filter");
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching Countries: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching Countries: " + e.getMessage(), e);
        }
    }
}
