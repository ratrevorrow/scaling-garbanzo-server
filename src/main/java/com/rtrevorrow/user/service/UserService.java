package com.rtrevorrow.user.service;

import com.rtrevorrow.user.dto.UserDto;
import com.rtrevorrow.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public interface UserService {

    Page<UserDto> list(Pageable pageable);
    UserDto update(User user);
    UserDto create(User user);
    void delete(Long id);
}