package com.rtrevorrow.user.controller;

import com.rtrevorrow.user.model.User;
import com.rtrevorrow.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@EnableSpringDataWebSupport
public class UserController implements BaseController<User> {

    @Autowired
    UserService userService;

    @GetMapping("/list")
    public Page<User> list(@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
        return userService.list(pageable);
    }

    @PostMapping
    public User create(@Validated @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Validated @RequestBody User user) { return userService.update(user); }

    @DeleteMapping
    public void delete(@RequestParam("id") Long id) {
        userService.delete(id);
    }
}
