package com.identity.reository;

import com.identity.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findByContactPersonContainingIgnoreCase(String name, Pageable pageable);

    List<Contact> findByUser_UserId(Long userId);
}
