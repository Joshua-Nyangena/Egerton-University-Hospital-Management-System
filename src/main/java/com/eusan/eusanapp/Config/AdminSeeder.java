package com.eusan.eusanapp.Config;

import com.eusan.eusanapp.Entity.Role;
import com.eusan.eusanapp.Entity.Staff;
import com.eusan.eusanapp.Repository.StaffRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder {

    @Bean
    public CommandLineRunner createAdmin(StaffRepository staffRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (staffRepository.findByRole(Role.ADMIN).isEmpty()) { // Correct Role Check
                Staff admin = new Staff();
                admin.setName("System Admin");
                admin.setContact("0700000000");
                admin.setNationalId("12345678");
                admin.setRole(Role.ADMIN); // Correct Role Assignment
                admin.setWorkId("ADMIN001"); // Fixed Work ID
                admin.setWorkEmail("admin@hospital.com");
                admin.setPassword(passwordEncoder.encode("admin123")); // Encrypt password

                staffRepository.save(admin);
                System.out.println("✅ Admin user created successfully!");
            } else {
                System.out.println("⚠️ Admin user already exists. Skipping seeding.");
            }
        };
    }
}
