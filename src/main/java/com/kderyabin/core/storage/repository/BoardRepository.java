package com.kderyabin.core.storage.repository;

import com.kderyabin.core.storage.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO for board table.
 */
@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    List<BoardEntity> loadRecent(Integer limit);
}
