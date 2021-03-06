package com.y3r9.c47.easy.config;

import javax.servlet.Filter;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.y3r9.c47.easy.api.config.ApiConfig;
import com.y3r9.c47.easy.main.config.MainConfig;

/**
 * The class WebAppInitializer.
 *
 * @version 1.0
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {
                MainConfig.class, ApiConfig.class, SecurityConfig.class
        };
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] {
                new HiddenHttpMethodFilter(),
                new CharacterEncodingFilter("utf-8", true)
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {
                "/"
        };
    }
}
