package com.identity.service;

import com.identity.dto.ContactDTO;
import com.identity.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ContactService {
    Contact saveContact(ContactDTO contactDTO, String token);
    Page<Contact> getAllContacts(String search, Pageable pageable);
    Optional<Contact> getContactById(Long id);
    Contact updateContactReturnEntity(Long id, ContactDTO updatedContactDTO, String token);
    Contact deleteContactReturnEntity(Long id, String token);

    List<Contact> getContactsByUserId(Long userId);
}
