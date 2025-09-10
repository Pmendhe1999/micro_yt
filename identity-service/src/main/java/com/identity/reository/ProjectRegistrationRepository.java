package com.identity.reository;

import com.identity.entity.ProjectRegistrationMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRegistrationRepository extends JpaRepository<ProjectRegistrationMaster, Long> {
    boolean existsByName(String name);
}
