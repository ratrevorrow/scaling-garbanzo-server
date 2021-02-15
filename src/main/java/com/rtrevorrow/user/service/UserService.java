package com.rtrevorrow.user.service;

import com.rtrevorrow.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public interface UserService {

    Page<User> list(Pageable pageable);
    User update(User user);
    User create(User user);
    void delete(Long id);
}