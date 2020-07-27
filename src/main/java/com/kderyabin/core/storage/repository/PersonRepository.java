package com.kderyabin.core.storage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kderyabin.core.storage.entity.PersonEntity;

/**
 * DAO for persons table.
 */
@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {
    List<PersonEntity> findAllByBoardId(Long boardId);
}
