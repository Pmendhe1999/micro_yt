package com.identity.serviceImpl;

import com.identity.entity.AuthenticationType;
import com.identity.reository.AuthenticationTypeRepository;
import com.identity.service.AuthenticationTypeService;
import com.identity.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationTypeServiceImpl implements AuthenticationTypeService {

    @Autowired
    private AuthenticationTypeRepository repository;

    @Autowired
    private JwtService jwtService;

    @Override
    public String saveAuthType(AuthenticationType authType, String token) {
        if (repository.existsByType(authType.getType())) {
            throw new IllegalArgumentException("Authentication type already exists");
        }

        String createdByUser = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        authType.setCreatedBy(createdByUser + " (" + role + ")");
        authType.setCreatedDate(LocalDateTime.now());
        authType.setLastModifiedBy(createdByUser + " (" + role + ")");
        authType.setLastModifiedDate(LocalDateTime.now());

        repository.save(authType);
        return "Authentication type '" + authType.getType() + "' created successfully by " + createdByUser;
    }

    @Override
    public List<AuthenticationType> getAllAuthTypes() {
        return repository.findAll();
    }

    @Override
    public Optional<AuthenticationType> getAuthTypeById(Long id) {
        return repository.findById(id);
    }

    @Override
    public String updateAuthType(Long id, AuthenticationType updatedAuthType, String token) {
        AuthenticationType existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Authentication type not found with id " + id));

        String modifiedByUser = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        existing.setType(updatedAuthType.getType());
        existing.setStatus(updatedAuthType.isStatus());
        existing.setLastModifiedBy(modifiedByUser + " (" + role + ")");
        existing.setLastModifiedDate(LocalDateTime.now());

        repository.save(existing);
        return "Authentication type updated successfully by " + modifiedByUser;
    }

    @Override
    public String deleteAuthType(Long id, String token) {
        AuthenticationType existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Authentication type not found with id " + id));

        String deletedByUser = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        repository.delete(existing);
        return "Authentication type deleted successfully by " + deletedByUser + " (" + role + ")";
    }

}
