package com.servicehub.config;

import com.servicehub.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultAdminInitializer {

    @Bean
    CommandLineRunner createDefaultAdmin(
            UserService userService,
            @Value("${servicehub.admin.name:ServiceHub Admin}") String name,
            @Value("${servicehub.admin.email:admin@servicehub.local}") String email,
            @Value("${servicehub.admin.phone:9999999999}") String phone,
            @Value("${servicehub.admin.password:Admin@12345}") String password) {
        return args -> userService.createAdminIfMissing(name, email, phone, password);
    }
}
