package ru.erulaev.restaurantvoting.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.erulaev.restaurantvoting.app.AuthUser;
import ru.erulaev.restaurantvoting.common.util.JsonUtil;
import ru.erulaev.restaurantvoting.user.model.Role;
import ru.erulaev.restaurantvoting.user.model.User;
import ru.erulaev.restaurantvoting.user.repository.UserRepository;

import java.util.Optional;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Slf4j
public class SecurityConfig {

    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final UserRepository userRepository;

    @Autowired
    private void setMapper(ObjectMapper objectMapper) {
        JsonUtil.setMapper(objectMapper);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PASSWORD_ENCODER;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            log.debug("Authenticating '{}'", email);
            Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
            return new AuthUser(optionalUser.orElseThrow(
                    () -> new UsernameNotFoundException("User '" + email + "' was not found")));
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/**").authorizeHttpRequests(authz ->
                        authz.requestMatchers("/api/admin/**").hasRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.POST, "/api/profile").anonymous()
                                .requestMatchers("/", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                                .requestMatchers("/api/**").authenticated())
                .httpBasic(withDefaults())
                .sessionManagement(smc -> smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}