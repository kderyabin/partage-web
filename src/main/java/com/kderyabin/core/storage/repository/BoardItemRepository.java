package com.kderyabin.core.storage.repository;

import com.kderyabin.core.storage.entity.BoardItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardItemRepository extends JpaRepository<BoardItemEntity, Long> {
    List<BoardItemEntity> findAllByBoardId(Long boardId);

    List<Object[]> getBoardPersonTotal(Long boardId);
}
