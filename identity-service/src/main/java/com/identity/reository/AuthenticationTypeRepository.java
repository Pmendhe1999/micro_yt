package com.identity.reository;

import com.identity.entity.AuthenticationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationTypeRepository extends JpaRepository<AuthenticationType, Long> {

    boolean existsByType(String type);
}
