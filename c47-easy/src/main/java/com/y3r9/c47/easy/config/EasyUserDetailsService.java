package com.y3r9.c47.easy.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * The class EasyUserDetailsService.
 *
 * @version 1.0
 */
final class EasyUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(final String s) throws UsernameNotFoundException {
        return null;
    }


}
