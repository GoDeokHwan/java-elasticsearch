package io.com.elastic.api.controller;

import io.com.elastic.api.dto.UsersDTO;
import io.com.elastic.api.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/users")
    public String createUsers(
            @RequestBody UsersDTO request
            ) {
        usersService.createUser(request);
        return null;
    }
}
