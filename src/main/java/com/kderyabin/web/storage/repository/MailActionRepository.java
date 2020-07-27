package com.kderyabin.web.storage.repository;

import com.kderyabin.web.storage.entity.MailActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO for mail table
 */
@Repository
public interface MailActionRepository extends JpaRepository<MailActionEntity, Long> {
    /**
     * Fetch a record by token
     * @param token Token to fetch
     * @return      MailActionEntity instance or null if no record with given token exists in database
     */
    public MailActionEntity findByToken( String token);
}
