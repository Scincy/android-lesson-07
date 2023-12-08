package kr.easw.lesson07.configurations;

import kr.easw.lesson07.auth.JWTFilterChain;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//import kr.easw.lesson06.Constants;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SpringSecurityConfig {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final JWTFilterChain filterChain;

    @Bean
    @SneakyThrows
    SecurityFilterChain configureHttpSecurity(HttpSecurity security) {
        security.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/manage").hasAnyAuthority("ADMIN", "GUEST")
                            .requestMatchers("/api/v1/user/**").hasAnyAuthority("ADMIN")
                            .requestMatchers("/api/v1/auth/**").permitAll()
                            .anyRequest().permitAll()
                    ;
                })
                .logout(customizer -> {
                    customizer.logoutUrl("/logout");
                    customizer.logoutSuccessUrl("/?logout=true");
                })
                .formLogin(customizer -> {
                    customizer
                            .loginPage("/login")
                            .permitAll()
                            .defaultSuccessUrl("/manage")
                            .failureUrl("/login?error=true");
                })
                .addFilterBefore(filterChain, UsernamePasswordAuthenticationFilter.class)
        ;
        return security.build();
    }
    @Bean
    public BCryptPasswordEncoder encoder() {
        return encoder;
    }

}