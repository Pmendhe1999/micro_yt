package com.identity.serviceImpl;

import com.identity.dto.ProjectRegisterDto;
import com.identity.entity.AuthTypes;
import com.identity.entity.ProjectRegistrationMaster;
import com.identity.reository.AuthTypesRepository;
import com.identity.reository.ProjectRegistrationRepository;
import com.identity.service.JwtService;
import com.identity.service.ProjectRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectRegistrationServiceImpl implements ProjectRegistrationService {
    @Autowired
    private ProjectRegistrationRepository repository;

    @Autowired
    private AuthTypesRepository authTypeRepo;

    @Autowired
    private JwtService jwtService;

    @Override
    public String saveProject(ProjectRegisterDto dto, String token) {
        if (repository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Project name already exists");
        }

        AuthTypes authType = authTypeRepo.findById(dto.getAuthTypeId())
                .orElseThrow(() -> new NoSuchElementException("Authentication type not found with id " + dto.getAuthTypeId()));

        String createdByUser = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        ProjectRegistrationMaster project = new ProjectRegistrationMaster();
        project.setName(dto.getName());
        project.setStatus(dto.isStatus());
        project.setAuthenticationType(authType);
        project.setCreatedBy(createdByUser + " (" + role + ")");
        project.setCreatedDate(LocalDateTime.now());
        project.setLastModifiedBy(createdByUser + " (" + role + ")");
        project.setLastModifiedDate(LocalDateTime.now());

        repository.save(project);
        return "Project '" + project.getName() + "' created successfully by " + createdByUser;
    }

    @Override
    public List<ProjectRegistrationMaster> getAllProjects() {
        return repository.findAll();
    }

    @Override
    public Optional<ProjectRegistrationMaster> getProjectById(Long id) {
        return repository.findById(id);
    }

    @Override
    public String updateProject(Long id, ProjectRegistrationMaster updatedProject, Long authTypeId, String token) {
        ProjectRegistrationMaster existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found with id " + id));

        String modifiedByUser = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        existing.setName(updatedProject.getName());
        existing.setStatus(updatedProject.isStatus());

        if (authTypeId != null) {
            AuthTypes authType = authTypeRepo.findById(authTypeId)
                    .orElseThrow(() -> new NoSuchElementException("Authentication type not found with id " + authTypeId));
            existing.setAuthenticationType(authType);
        }

        existing.setLastModifiedBy(modifiedByUser + " (" + role + ")");
        existing.setLastModifiedDate(LocalDateTime.now());

        repository.save(existing);
        return "Project updated successfully by " + modifiedByUser;
    }

    @Override
    public String deleteProject(Long id, String token) {
        ProjectRegistrationMaster existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found with id " + id));

        String deletedByUser = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        repository.delete(existing);
        return "Project deleted successfully by " + deletedByUser + " (" + role + ")";
    }
}
