package com.rtrevorrow.user.service.impl;

import com.rtrevorrow.user.dto.UserDto;
import com.rtrevorrow.user.model.User;
import com.rtrevorrow.user.repository.UserRepository;
import com.rtrevorrow.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Page<UserDto> list(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        return users.map(User::toDto);
    }

    @Override
    public UserDto update(User user) {
        return userRepository.save(user).toDto();
    }

    @Override
    public UserDto create(User user) {
        userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User already exists"));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        user.setPassword(null);

        return user.toDto();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
