package com.identity.service;

import com.identity.dto.ProjectRegisterDto;
import com.identity.entity.ProjectRegistrationMaster;

import java.util.List;
import java.util.Optional;

public interface ProjectRegistrationService {

    String saveProject(ProjectRegisterDto dto, String token);

    List<ProjectRegistrationMaster> getAllProjects();

    Optional<ProjectRegistrationMaster> getProjectById(Long id);

    String updateProject(Long id, ProjectRegistrationMaster updatedProject, Long authTypeId, String token);

    String deleteProject(Long id, String token);
}
