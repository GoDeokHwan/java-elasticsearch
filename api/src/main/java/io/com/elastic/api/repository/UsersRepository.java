package io.com.elastic.api.repository;

import io.com.elastic.api.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
