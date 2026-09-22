package com.servicehub.service;

import com.servicehub.model.RegistrationForm;
import com.servicehub.model.Role;
import com.servicehub.model.User;
import com.servicehub.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegistrationForm form) {
        String email = normalizeEmail(form.getEmail());
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("An account with that email already exists");
        }
        if (!Objects.equals(form.getPassword(), form.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = new User(
                form.getName().trim(),
                email,
                form.getPhone().trim(),
                passwordEncoder.encode(form.getPassword()),
                Role.USER
        );
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> authenticate(String email, String password) {
        return userRepository.findByEmailIgnoreCase(normalizeEmail(email))
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    @Transactional
    public User createAdminIfMissing(String name, String email, String phone, String password) {
        return userRepository.findByEmailIgnoreCase(normalizeEmail(email)).orElseGet(() ->
                userRepository.save(new User(name, normalizeEmail(email), phone, passwordEncoder.encode(password), Role.ADMIN))
        );
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }
}
