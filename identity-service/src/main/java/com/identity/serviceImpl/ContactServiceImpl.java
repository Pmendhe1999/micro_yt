package com.identity.serviceImpl;

import com.identity.dto.ContactDTO;
import com.identity.entity.Contact;
import com.identity.entity.UserCredential;
import com.identity.reository.ContactRepository;
import com.identity.reository.UserRepository;
import com.identity.service.ContactService;
import com.identity.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Override
    public Contact saveContact(ContactDTO contactDTO, String token) {
        try {
            String createdByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            UserCredential user = userRepository.findByUserId(contactDTO.getUserId())
                    .orElseThrow(() -> new NoSuchElementException("User not found"));

            Contact contact = new Contact();
            contact.setContactPerson(contactDTO.getContactPerson());
            contact.setEmail(contactDTO.getEmail());
            contact.setMobileNumber(contactDTO.getMobileNumber());
            contact.setPosition(contactDTO.getPosition());
            contact.setUser(user);

            Contact savedContact = repository.save(contact);

            log.info("Contact '{}' created for userId={} by user={} role={}",
                    savedContact.getContactPerson(), user.getUserId(), createdByUser, role);

            return savedContact;

        } catch (Exception e) {
            log.error("Error occurred while saving Contact: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while saving contact: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<Contact> getAllContacts(String search, Pageable pageable) {
        try {
            if (search != null && !search.isEmpty()) {
                log.debug("Fetching Contacts with search filter: {}", search);
                return repository.findByContactPersonContainingIgnoreCase(search, pageable);
            }
            log.debug("Fetching all Contacts without filter");
            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching Contacts: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching Contacts: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Contact> getContactById(Long id) {
        try {
            log.debug("Fetching Contact by id={}", id);
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error occurred while fetching Contact with id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching Contact with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Contact updateContactReturnEntity(Long id, ContactDTO updatedContactDTO, String token) {
        try {
            Contact existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Contact not found with id " + id));

            String modifiedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            UserCredential user = userRepository.findByUserId(updatedContactDTO.getUserId())
                    .orElseThrow(() -> new NoSuchElementException("User not found"));

            existing.setContactPerson(updatedContactDTO.getContactPerson());
            existing.setEmail(updatedContactDTO.getEmail());
            existing.setMobileNumber(updatedContactDTO.getMobileNumber());
            existing.setPosition(updatedContactDTO.getPosition());
            existing.setUser(user);

            repository.save(existing);

            log.info("Contact id={} updated by user={} role={}", id, modifiedByUser, role);

            return existing;

        } catch (Exception e) {
            log.error("Unexpected error while updating Contact id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while updating Contact with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Contact deleteContactReturnEntity(Long id, String token) {
        try {
            Contact existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Contact not found with id " + id));

            String deletedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);

            log.info("Contact id={} deleted by user={} role={}", id, deletedByUser, role);

            return existing;

        } catch (Exception e) {
            log.error("Unexpected error while deleting Contact id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while deleting Contact with id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<Contact> getContactsByUserId(Long userId) {
        try {
            log.debug("Fetching contacts for userId={}", userId);
            return repository.findByUser_UserId(userId);
        } catch (Exception e) {
            log.error("Error occurred while fetching contacts for userId={}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching contacts for userId " + userId + ": " + e.getMessage(), e);
        }
    }
}
