package com.identity.serviceImpl;

import com.identity.dto.AuthTypeDTO;
import com.identity.entity.AppFunction;
import com.identity.entity.AuthType;
import com.identity.entity.AuthTypeMaster;
import com.identity.reository.AppFunctionRepository;
import com.identity.reository.AuthTypeMasterRepository;
import com.identity.reository.AuthTypeRepository;
import com.identity.service.AuthTypeService;
import com.identity.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AuthTypeServiceImpl implements AuthTypeService {

    @Autowired
    private  AuthTypeRepository repository;
    @Autowired
    private  AppFunctionRepository appFunctionRepository;
    @Autowired
    private  AuthTypeMasterRepository authTypeMasterRepository;
    @Autowired
    private  JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(AuthTypeServiceImpl.class);

    @Override
    public AuthType saveAuthType(AuthTypeDTO dto, String token) {
        try {
            if (repository.existsByName(dto.getName())) {
                log.warn("Attempt to create duplicate AuthType: {}", dto.getName());
                throw new IllegalArgumentException("AuthType already exists with name: " + dto.getName());
            }

            String createdByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            AppFunction appFunction = appFunctionRepository.findById(dto.getAppFuncId())
                    .orElseThrow(() -> new NoSuchElementException("AppFunction not found"));

            AuthTypeMaster authTypeMaster = authTypeMasterRepository.findById(dto.getAuthTypeMasterId())
                    .orElseThrow(() -> new NoSuchElementException("AuthTypeMaster not found"));

            AuthType entity = new AuthType();
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setStatus(dto.getStatus());
            entity.setAppFunction(appFunction);
            entity.setAuthTypeMaster(authTypeMaster);
            entity.setCreatedDate(LocalDateTime.now());
            entity.setLastModifiedDate(LocalDateTime.now());

            AuthType saved = repository.save(entity);

            log.info("AuthType '{}' created by user={} role={}", saved.getName(), createdByUser, role);
            return saved;

        } catch (IllegalArgumentException e) {
            log.error("Business validation failed while saving AuthType: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while saving AuthType: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while saving AuthType: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<AuthType> getAllAuthTypes(String search, List<Long> appFuncIds, List<Long> authTypeMasterIds, Pageable pageable) {
        try {
            log.debug("Fetching AuthTypes with search={}, authFuncIds={}, authTypeMasterIds={}", search, appFuncIds, authTypeMasterIds);

            if ((search != null && !search.isEmpty()) ||
                    (appFuncIds != null && !appFuncIds.isEmpty()) ||
                    (authTypeMasterIds != null && !authTypeMasterIds.isEmpty())) {
                return repository.findByFilters(search, appFuncIds, authTypeMasterIds, pageable);
            }

            return repository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error occurred while fetching AuthTypes: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching AuthTypes: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<AuthType> getAuthTypeById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error occurred while fetching AuthType id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching AuthType with id " + id, e);
        }
    }

    @Override
    public AuthType updateAuthType(Long id, AuthTypeDTO dto, String token) {
        try {
            AuthType existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("AuthType not found with id " + id));

            String modifiedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            AppFunction appFunction = appFunctionRepository.findById(dto.getAppFuncId())
                    .orElseThrow(() -> new NoSuchElementException("AppFunction not found"));

            AuthTypeMaster authTypeMaster = authTypeMasterRepository.findById(dto.getAuthTypeMasterId())
                    .orElseThrow(() -> new NoSuchElementException("AuthTypeMaster not found"));

            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setStatus(dto.getStatus());
            existing.setAppFunction(appFunction);
            existing.setAuthTypeMaster(authTypeMaster);
            existing.setLastModifiedDate(LocalDateTime.now());

            repository.save(existing);

            log.info("AuthType id={} updated by user={} role={}", id, modifiedByUser, role);
            return existing;

        } catch (Exception e) {
            log.error("Unexpected error while updating AuthType id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while updating AuthType with id " + id, e);
        }
    }

    @Override
    public AuthType deleteAuthType(Long id, String token) {
        try {
            AuthType existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("AuthType not found with id " + id));

            String deletedByUser = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);

            log.info("AuthType id={} deleted by user={} role={}", id, deletedByUser, role);
            return existing;

        } catch (Exception e) {
            log.error("Unexpected error while deleting AuthType id={}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error occurred while deleting AuthType with id " + id, e);
        }
    }


}
