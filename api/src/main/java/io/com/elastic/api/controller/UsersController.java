package io.com.elastic.api.controller;

import io.com.elastic.api.dto.UsersDTO;
import io.com.elastic.api.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService;

    @PostMapping("")
    public String create(
            @RequestBody UsersDTO request
            ) {
        usersService.createUser(request);
        return "OK";
    }

    @PatchMapping("/{id}")
    public String modify(
            @PathVariable Long id,
            @RequestBody UsersDTO request
    ) {
        usersService.modify(id, request);
        return "OK";
    }

}
