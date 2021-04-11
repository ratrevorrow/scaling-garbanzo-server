package com.rtrevorrow.user.service.impl;

import com.rtrevorrow.user.config.UserServiceConfig;
import com.rtrevorrow.user.dto.UserDto;
import com.rtrevorrow.user.repository.UserRepository;
import com.rtrevorrow.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { UserServiceConfig.class })
@WebAppConfiguration
//@WebMvcTest(controllers = UserController.class)
@DataJpaTest
class UserServiceImplTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testGetUsers() {
        Pageable pageable = PageRequest.of(0, 100);
        Page<UserDto> users = userService.list(pageable);
        assertEquals(1, users.getSize());
    }
}