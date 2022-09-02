package com.tc.tcapi.model;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


@Getter
@Setter
public class MyUserDetail extends User implements UserDetails {


    public MyUserDetail(User user) {
        super(user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getPassword(),
                user.getVerificationCode(),
                user.getAvt(),
                user.getActive(),
                user.getStatus(),
                user.getCreateDate(),
                user.getUpdateDate(),
                user.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = super.getRole();
        return Collections.singleton(new SimpleGrantedAuthority(role.getName().name()));
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return super.getActive() == 1;
    }
}
