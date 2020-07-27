package com.kderyabin.core.storage.repository;

import com.kderyabin.core.storage.entity.BoardItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO for items table.
 */
@Repository
public interface BoardItemRepository extends JpaRepository<BoardItemEntity, Long> {
    /**
     * Fetches list of items by board id.
     *
     * @param boardId Board ID
     * @return List of BoardItemEntity instances
     */
    List<BoardItemEntity> findAllByBoardId(Long boardId);

    /**
     * Fetches total amount spent by each participant for some board.
     *
     * @param boardId Board ID
     * @return List of objects
     */
    List<Object[]> getBoardPersonTotal(Long boardId);

    /**
     * Delete items of a board attached to a participant.
     *
     * @param boardId  Board ID
     * @param personId Person ID
     */
    @Modifying
    void removeByBoardAndPerson(Long boardId, Long personId);
}
