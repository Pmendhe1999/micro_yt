package com.identity.serviceImpl;

import com.identity.dto.NotificationTechDetailsDTO;
import com.identity.entity.NotificationTechDetails;
import com.identity.entity.ServiceProviderMaster;
import com.identity.reository.NotificationTechDetailsRepository;
import com.identity.reository.ServiceProviderMasterRepository;
import com.identity.service.JwtService;
import com.identity.service.NotificationTechDetailsService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class NotificationTechDetailsServiceImpl implements NotificationTechDetailsService {


    @Autowired
    private NotificationTechDetailsRepository repository;

    @Autowired
    private ServiceProviderMasterRepository serviceProviderMasterRepository;

    @Autowired
    private JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(NotificationTechDetailsServiceImpl.class);

    @Override
    public NotificationTechDetails save(NotificationTechDetailsDTO dto, String token) {
        try {
            String createdBy = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            ServiceProviderMaster spm = serviceProviderMasterRepository.findById(dto.getServiceProviderMasterId())
                    .orElseThrow(() -> new NoSuchElementException("ServiceProviderMaster not found"));

            NotificationTechDetails entity = new NotificationTechDetails();
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setNotes(dto.getNotes());
            entity.setStatus(dto.getStatus());
            entity.setCreatedDate(LocalDateTime.now());
            entity.setLastModifiedDate(LocalDateTime.now());
            entity.setServiceProviderMaster(spm);

            NotificationTechDetails saved = repository.save(entity);

            log.info("NotificationTechDetails '{}' created for ServiceProviderMasterId={} by user={} role={}",
                    saved.getName(), spm.getId(), createdBy, role);

            return saved;
        } catch (Exception e) {
            log.error("Error occurred while saving NotificationTechDetails: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while saving NotificationTechDetails: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<NotificationTechDetails> getAll(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return repository.findByNameContainingIgnoreCase(search, pageable);
        }
        return repository.findAll(pageable);
    }

    @Override
    public Optional<NotificationTechDetails> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public NotificationTechDetails updateReturnEntity(Long id, NotificationTechDetailsDTO dto, String token) {
        try {
            NotificationTechDetails existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("NotificationTechDetails not found with id " + id));

            String modifiedBy = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            ServiceProviderMaster spm = serviceProviderMasterRepository.findById(dto.getServiceProviderMasterId())
                    .orElseThrow(() -> new NoSuchElementException("ServiceProviderMaster not found"));

            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setNotes(dto.getNotes());
            existing.setStatus(dto.getStatus());
            existing.setLastModifiedDate(LocalDateTime.now());
            existing.setServiceProviderMaster(spm);

            NotificationTechDetails updated = repository.save(existing);

            log.info("NotificationTechDetails id={} updated by user={} role={}", id, modifiedBy, role);

            return updated;
        } catch (Exception e) {
            log.error("Error updating NotificationTechDetails: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating NotificationTechDetails: " + e.getMessage(), e);
        }
    }

    @Override
    public NotificationTechDetails deleteReturnEntity(Long id, String token) {
        try {
            NotificationTechDetails existing = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("NotificationTechDetails not found with id " + id));

            String deletedBy = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            repository.delete(existing);

            log.info("NotificationTechDetails id={} deleted by user={} role={}", id, deletedBy, role);

            return existing;
        } catch (Exception e) {
            log.error("Error deleting NotificationTechDetails: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting NotificationTechDetails: " + e.getMessage(), e);
        }
    }

    @Override
    public List<NotificationTechDetails> getByServiceProviderMasterId(Long serviceProviderMasterId) {
        return repository.findByServiceProviderMaster_Id(serviceProviderMasterId);
    }
}
