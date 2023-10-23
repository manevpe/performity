package com.performity.useradmin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@Configuration
public class FilterConfig {
  @Autowired
  private JwtFilter jwtFilter;

  @Bean
  public FilterRegistrationBean<JwtFilter> jwtFilterConfig() {
    FilterRegistrationBean<JwtFilter> filter = new FilterRegistrationBean<>();
    filter.setFilter(jwtFilter);
    // Provide endpoints which needs to be restricted.
    // All Endpoints would be restricted if unspecified.
    filter.addUrlPatterns("/useradmin/v1/*");
    return filter;
  }
}
