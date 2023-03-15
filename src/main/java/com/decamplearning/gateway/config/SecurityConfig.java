package com.decamplearning.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("#{${allowed.domains}}")
    private List<String> allowedDomains;

    @Bean
    public SecurityWebFilterChain sprinSecurityWebFilterChain(ServerHttpSecurity serverHttpSecurity){
        serverHttpSecurity.csrf().disable()
                .cors().configurationSource(request-> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(allowedDomains);
                    configuration.setAllowedMethods(Arrays.asList("GET","POST"));
                    configuration.setAllowedHeaders(List.of("*"));
                    return configuration;
                }).and().authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec
                                .pathMatchers("/**")
                                .permitAll()
                                .anyExchange()
                                .authenticated()
                ).oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
        return serverHttpSecurity.build();
    }

}
