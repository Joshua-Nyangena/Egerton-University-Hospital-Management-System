package com.eusan.eusanapp.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private StaffUserDetailsService staffUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ KEEP THIS
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers(
                    "/doctor/dashboard",
                    "/doctor/patientQueue",
                    "/doctor/lab-results",
                    "/doctor/allPatientsQueue",
                    "/doctor/pendingTriagedQueue",
                    "/doctor/labResults/general"
                ).hasRole("DOCTOR")
                .requestMatchers("/triage/**").hasRole("TRIAGE_OFFICER")
                .requestMatchers("/receptionist/**").hasRole("RECEPTIONIST")
                .requestMatchers("/lab/**").hasRole("LAB_TECHNICIAN")
                .requestMatchers("/pharmacy/**").hasRole("PHARMACIST")
                .requestMatchers("/dentist/**").hasRole("DENTIST")
                .requestMatchers("/vct/**").hasRole("INFECTIOUS_DOCTOR")
                .requestMatchers("/cmo/**").hasRole("CHIEF_MEDICAL_OFFICER")
                .requestMatchers(
                    "/doctor/prescription/**",
                    "/doctor/viewMedicalHistory/**",
                    "/doctor/view/**",
                    "/doctor/labTest/**",
                    "/doctor/admitPatient",
                    "/doctor/saveAdmit",
                    "/doctor/saveLabTest",
                    "/doctor/searchAllPatientsQueue",
                    "/doctor/searchPendingTriagedQueue",
                    "/doctor/savePrescription",
                    "/profile/change-password"
                ).hasAnyRole("DOCTOR", "DENTIST", "INFECTIOUS_DOCTOR")
                .requestMatchers("/login", "/css/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/redirect", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
