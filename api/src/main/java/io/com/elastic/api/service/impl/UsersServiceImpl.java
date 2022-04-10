package io.com.elastic.api.service.impl;

import io.com.elastic.api.dto.UsersDTO;
import io.com.elastic.api.entity.Users;
import io.com.elastic.api.repository.UsersRepository;
import io.com.elastic.api.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    @Override
    public void createUser(UsersDTO usersDTO) {
        Users users = usersDTO.convertUsers();
        usersRepository.save(users);
    }
}
