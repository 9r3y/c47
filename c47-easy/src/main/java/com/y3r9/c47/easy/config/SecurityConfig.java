package com.y3r9.c47.easy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * The class SecurityConfig.
 *
 * @version 1.0
 */
@Configuration
@ImportResource({
        "classpath:config/spring/security.xml"
})
@EnableWebSecurity
public class SecurityConfig {

}
