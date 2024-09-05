package com.clap.pause.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class MockSecurityContextFactory implements WithSecurityContextFactory<MockMember> {
    @Override
    public SecurityContext createSecurityContext(MockMember annotation) {
        var authorities = List.of(new SimpleGrantedAuthority(annotation.memberRole().name()));
        var authToken = new UsernamePasswordAuthenticationToken(annotation.memberId(), annotation.email(), authorities);

        var securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authToken);
        return securityContext;
    }
}
