package com.rtrevorrow.user.service.impl;

import com.rtrevorrow.user.model.User;
import com.rtrevorrow.user.repository.UserRepository;
import com.rtrevorrow.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Page<User> list(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public User create(User user) {
        userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User already exists"));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        user.setPassword(null);

        return user;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
