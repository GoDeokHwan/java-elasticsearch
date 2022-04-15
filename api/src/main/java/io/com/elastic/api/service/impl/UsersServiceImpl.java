package io.com.elastic.api.service.impl;

import io.com.elastic.api.dto.UsersDTO;
import io.com.elastic.api.entity.Users;
import io.com.elastic.api.event.UsersModifyEvent;
import io.com.elastic.api.repository.UsersRepository;
import io.com.elastic.api.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final ApplicationEventPublisher publisher;

    @Override
    public void createUser(UsersDTO usersDTO) {
        Users users = usersDTO.convertUsers();
        usersRepository.save(users);
    }

    @Override
    public void modify(Long id, UsersDTO usersDTO) {
        Users users = usersRepository.getById(id);
        users.changeUsers(usersDTO);
        publisher.publishEvent(new UsersModifyEvent(users));
    }
}
