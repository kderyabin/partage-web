package com.kderyabin.core.storage.repository;

import com.kderyabin.core.storage.entity.BoardItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardItemRepository extends JpaRepository<BoardItemEntity, Long> {
    List<BoardItemEntity> findAllByBoardId(Long boardId);

    List<Object[]> getBoardPersonTotal(Long boardId);

    /**
     * Delete items of a board attached to a participant.
     * @param boardId   Board ID
     * @param personId  Person ID
     */
    @Modifying
    void removeByBoardAndPerson(Long boardId, Long personId);
}
