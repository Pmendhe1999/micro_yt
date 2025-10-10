package com.identity.config;

import com.identity.entity.UserCredential;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Data
@Getter
@Setter
public class CustomUserDetails implements UserDetails {

    private final UserCredential user;

    // ✅ Add this getter manually
    public UserCredential getUser() {
        return user;
    }

    public CustomUserDetails(UserCredential user) {
        this.user = user;
    }

    public Long getUserId() {
        return user.getUserId();
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public Boolean getSelfAuthentication() {
        return user.getSelfAuthentication();
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities().stream()
                .map(auth -> new SimpleGrantedAuthority("ROLE_" + auth.getName())) // ensure ROLE_ prefix
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }



    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == UserCredential.Status.ACTIVE;
    }
}
