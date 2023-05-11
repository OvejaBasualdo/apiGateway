package com.microservices.paymentmodule.apiagateway.security;

import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@EnableWebFluxSecurity
public class SpringSecurityConfig {
    @Autowired
    private JwtAuthenticationFilter authenticationFilter;

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/api/security/oauth/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/api/users/createUsers").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/accounts/**", "/api/transaction/**").hasRole("USER")
                .pathMatchers(HttpMethod.POST, "/api/accounts/**", "/api/transaction/**").hasRole("USER")
                .pathMatchers(HttpMethod.PUT, "/api/accounts/**", "/api/transaction/**").hasRole("USER")
                .pathMatchers(HttpMethod.GET, "/api/users/**", "/api/accounts/deleteUserAccounts").hasAnyRole("ADMIN", "USER")
                .pathMatchers(HttpMethod.PUT, "/api/users/**", "/api/accounts/deleteUserAccounts").hasAnyRole("ADMIN", "USER")
                .pathMatchers(HttpMethod.GET, "/api/users/obtener-config", "/api/users/deleteUser/").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT, "/api/users/obtener-config", "/api/users/deleteUser/").hasRole("ADMIN")
                .anyExchange().authenticated()
                .and()
                .oauth2Login()
                .and().addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf().disable()
                .build();
    }

    @Bean
    public WebFilter addCsrfTokenFilter() {
        return (exchange, next) -> Mono.just(exchange)
                .flatMap(ex -> ex.<Mono<CsrfToken>>getAttribute(CsrfToken.class.getName()))
                .doOnNext(ex -> {
                })
                .then(next.filter(exchange));
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
    }
}
