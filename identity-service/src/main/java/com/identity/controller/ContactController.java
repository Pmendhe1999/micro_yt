package com.identity.controller;

import com.identity.dto.ContactDTO;
import com.identity.dto.ResponceData;
import com.identity.entity.Contact;
import com.identity.service.ContactService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/contacts")
public class ContactController {
    @Autowired
    private ContactService service;

    private static final Logger log = LoggerFactory.getLogger(ContactController.class);

    // CREATE
    @PostMapping
    public ResponseEntity<ResponceData> createContact(
            @Valid @RequestBody ContactDTO contactDTO,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Received request to create Contact: {}", contactDTO);

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

                log.warn("Validation failed for ContactDTO: {}", errors);

                ResponceData response = new ResponceData(
                        "fail", 400, "Validation failed", errors, errors.size());
                return ResponseEntity.badRequest().body(response);
            }

            String token = authHeader.replace("Bearer ", "");
            Contact saved = service.saveContact(contactDTO, token);

            log.info("Contact created successfully with id={} and name={}",
                    saved.getId(), saved.getContactPerson());

            ResponceData response = new ResponceData(
                    "success", 200, "Contact created successfully", saved, 1);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while creating Contact: {}", e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "error", 500, "Unexpected error: " + e.getMessage(), null, 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<ResponceData> getAllContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            log.info("Fetching Contacts page={}, size={}, search={}, sortBy={}, sortDir={}",
                    page, size, search, sortBy, sortDir);

            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Contact> result = service.getAllContacts(search, pageable);

            log.info("Retrieved {} Contacts", result.getTotalElements());

            ResponceData response = new ResponceData(
                    "success", 200, "Retrieved Contacts",
                    result.getContent(), (int) result.getTotalElements());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while fetching Contacts: {}", e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponceData> getContactById(@PathVariable Long id) {
        try {
            log.info("Fetching Contact by id={}", id);

            Optional<Contact> contact = service.getContactById(id);

            if (contact.isPresent()) {
                log.info("Found Contact with id={}", id);
                ResponceData response = new ResponceData(
                        "success", 200, "Retrieved Contact",
                        Collections.singletonList(contact.get()), 1);
                return ResponseEntity.ok(response);
            } else {
                log.warn("Contact not found with id={}", id);
                ResponceData response = new ResponceData(
                        "fail", 404, "Contact not found with id " + id,
                        Collections.emptyList(), 0);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            log.error("Unexpected error while fetching Contact id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponceData> updateContact(
            @PathVariable Long id,
            @Valid @RequestBody ContactDTO updatedContactDTO,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Updating Contact id={} with payload={}", id, updatedContactDTO);

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));
                log.warn("Validation failed for update Contact id={}, errors={}", id, errors);

                ResponceData response = new ResponceData(
                        "fail", 400, "Validation errors",
                        Collections.emptyList(), 0);
                return ResponseEntity.badRequest().body(response);
            }

            String token = authHeader.replace("Bearer ", "");
            Contact updated = service.updateContactReturnEntity(id, updatedContactDTO, token);

            log.info("Contact updated successfully id={}", updated.getId());

            ResponceData response = new ResponceData(
                    "success", 200, "Contact updated successfully",
                    Collections.singletonList(updated), 1);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while updating Contact id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponceData> deleteContact(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            log.info("Deleting Contact id={}", id);

            String token = authHeader.replace("Bearer ", "");
            Contact deleted = service.deleteContactReturnEntity(id, token);

            log.info("Contact deleted successfully id={}", deleted.getId());

            ResponceData response = new ResponceData(
                    "success", 200, "Contact deleted successfully",
                    Collections.singletonList(deleted), 1);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error while deleting Contact id={}: {}", id, e.getMessage(), e);
            ResponceData response = new ResponceData(
                    "fail", 500, "Unexpected error: " + e.getMessage(),
                    Collections.emptyList(), 0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
