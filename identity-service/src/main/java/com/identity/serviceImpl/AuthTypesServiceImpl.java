package com.identity.serviceImpl;

import com.identity.entity.AuthTypes;
import com.identity.reository.AuthTypesRepository;
import com.identity.service.AuthTypesService;
import com.identity.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthTypesServiceImpl implements AuthTypesService {

    @Autowired
    private AuthTypesRepository repository;

    @Autowired
    private JwtService jwtService;  // 🔹 same JWT service you’re using for ProjectRegistration

    @Override
    public String saveAuthType(AuthTypes authType, String token) {
        if (repository.existsByAuthTypeName(authType.getAuthTypeName())) {
            throw new IllegalArgumentException("Auth type already exists");
        }

        String createdByUser = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        // No createdBy/modifiedBy fields in your entity → but you can add if needed

        repository.save(authType);
        return "Auth type '" + authType.getAuthTypeName() + "' created successfully by " + createdByUser + " (" + role + ")";
    }

    @Override
    public List<AuthTypes> getAllAuthTypes() {
        return repository.findAll();
    }

    @Override
    public Optional<AuthTypes> getAuthTypeById(Long id) {
        return repository.findById(id);
    }

    @Override
    public String updateAuthType(Long id, AuthTypes updatedAuthType, String token) {
        AuthTypes existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Auth type not found with id " + id));

        String modifiedByUser = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        existing.setAuthTypeName(updatedAuthType.getAuthTypeName());
        existing.setStatus(updatedAuthType.getStatus());
        existing.setFunctionality(updatedAuthType.getFunctionality());
        existing.setDescription(updatedAuthType.getDescription());

        repository.save(existing);
        return "Auth type updated successfully by " + modifiedByUser + " (" + role + ")";
    }

    @Override
    public String deleteAuthType(Long id, String token) {
        AuthTypes existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Auth type not found with id " + id));

        String deletedByUser = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        repository.delete(existing);
        return "Auth type deleted successfully by " + deletedByUser + " (" + role + ")";
    }
}
