package com.y3r9.c47.easy.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * The class EasyAuthenticationProvider.
 *
 * @version 1.0
 */
final class EasyAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(final Class<?> aClass) {
        return false;
    }
}
