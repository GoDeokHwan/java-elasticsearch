package io.com.elastic.api.repository;

import io.com.elastic.api.dto.BoardsDTO;
import io.com.elastic.api.entity.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardsRepository extends JpaRepository<Boards, Long> {
    List<BoardsDTO> findAllByTitleAndComment(String title, String comment);

}
