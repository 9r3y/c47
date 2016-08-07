package com.y3r9.c47.easy.main.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * The class MVCConfig.
 *
 * @version 1.0
 */
@Configuration
@ImportResource({
        "classpath:config/spring/main-context.xml"
})
public class MainConfig {
}
