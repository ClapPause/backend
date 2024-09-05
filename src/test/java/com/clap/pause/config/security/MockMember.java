package com.clap.pause.config.security;

import com.clap.pause.model.MemberRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockSecurityContextFactory.class)
public @interface MockMember {
    long memberId() default 1L;

    String email() default "test@naver.com";

    MemberRole memberRole() default MemberRole.MEMBER;
}
