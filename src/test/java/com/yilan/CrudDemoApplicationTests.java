package com.yilan;

import com.yilan.controller.UserController;
import com.yilan.domain.entity.User;
import com.yilan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class CrudDemoApplicationTests {

    @Autowired
    private UserService userService;


    @Test
    void contextLoads() {

    }

    @Test
    void testUserService() {
        List<User> list = userService.list();
        for (User user : list) {
            log.info(String.valueOf(user));
        }
    }

}
