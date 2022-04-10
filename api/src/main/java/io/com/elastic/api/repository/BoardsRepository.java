package io.com.elastic.api.repository;

import io.com.elastic.api.entity.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardsRepository extends JpaRepository<Boards, Long> {
}
