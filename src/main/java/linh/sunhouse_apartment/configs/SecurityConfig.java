package linh.sunhouse_apartment.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(AnyRequestMatcher.INSTANCE) // Áp dụng cho tất cả request
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .logout(logout -> logout.disable())
                .formLogin(form -> form.disable()) // deprecated nhưng vẫn chạy được
                .httpBasic(basic -> basic.disable()); // deprecated nhưng vẫn chạy được

        return http.build();
    }
}

