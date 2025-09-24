package com.identity.controller;

import com.identity.dto.ResponceData;
import com.identity.entity.Country;
import com.identity.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {

    @Autowired
    private CountryService service;

    private static final Logger log = LoggerFactory.getLogger(CountryController.class);




    @GetMapping
    public ResponseEntity<ResponceData> getAllCountries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "countryId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        try {
            log.info("Fetching Countries page={}, size={}, search={}, sortBy={}, sortDir={}", page, size, search, sortBy, sortDir);

            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Country> result = service.getAllCountries(search, pageable);

            log.info("Retrieved {} Countries", result.getTotalElements());
            ResponceData response = new ResponceData(
                    "success",
                    200,
                    "Retrieved Countries",
                    result.getContent(),
                    (int) result.getTotalElements()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Unexpected error while fetching Countries: {}", e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail",
                    500,
                    "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(),
                    0
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
